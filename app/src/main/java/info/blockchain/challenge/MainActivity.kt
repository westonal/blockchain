package info.blockchain.challenge

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import info.blockchain.challenge.api.MultiAddress
import info.blockchain.challenge.api.Transaction
import info.blockchain.challenge.ui.TransactionsAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber


class MainActivity : AppCompatActivity() {

    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recycler_view.layoutManager = LinearLayoutManager(this)

        val retrofit = Retrofit.Builder()
                .baseUrl("https://blockchain.info/")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .client(OkHttpClient.Builder()
                        .addInterceptor(timberHttpLoggingInterceptor())
                        .build())
                .build()

        val service = retrofit.create(MultiAddress::class.java)

        service.multiaddr("xpub6CfLQa8fLgtouvLxrb8EtvjbXfoC1yqzH6YbTJw4dP7srt523AhcMV8Uh4K3TWSHz9oDWmn9MuJogzdGU3ncxkBsAC9wFBLmFrWT9Ek81kQ")
                // Note: Left in to show in rx if we want a side effect we should use a doOn method, rather than a
                // side-effect in a map for example.
                // Note also that my Timber setup means this won't appear in release
                .doOnSuccess {
                    Timber.d("Final balance: ${it.wallet.finalBalance}")
                    for (transaction in it.transactions) {
                        Timber.d("Transaction: ${transaction.result}")
                        for (output in transaction.outputs) {
                            Timber.d(" Output: ${output.address} ${output.xpub}")
                        }
                    }
                }
                // Note: map to viewmodel while still in the background thread
                .map { it.transactions.map(Transaction::mapToViewModel) }
                // Note: Only now do I switch to the main thread, just before we need to update the UI
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy {
                    recycler_view.adapter = TransactionsAdapter(it)
                }
    }

    private fun timberHttpLoggingInterceptor() =
            HttpLoggingInterceptor(timberHttpLogger()).apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }

    private fun timberHttpLogger() =
            HttpLoggingInterceptor.Logger { message -> Timber.d(message) }
}
