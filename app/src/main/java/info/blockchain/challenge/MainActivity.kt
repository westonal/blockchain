package info.blockchain.challenge

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import info.blockchain.challenge.api.MultiAddress
import info.blockchain.challenge.ui.CardAdapter
import info.blockchain.challenge.ui.WalletModule
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {

    private val disposable = CompositeDisposable()

    // Note: I would normally inject this, but chose not to set up Dagger for this demo
    private val retrofit = Retrofit.Builder()
            .baseUrl("https://blockchain.info/")
            .addConverterFactory(GsonConverterFactory.create())
            // Note: this line puts all Rx calls on the IO scheduler, so no need to specify subscribeOn later
            .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
            .client(OkHttpClient.Builder()
                    .addInterceptor(timberHttpLoggingInterceptor())
                    .build())
            .build()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recycler_view.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()

        val walletModule = WalletModule(retrofit.create(MultiAddress::class.java))

        disposable += walletModule
                .cardViewModels
                // Note: Only now do I switch to the main thread, just before we need to update the UI
                // the Module has mapped the service call to card view models for me already and is fully test driven
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy {
                    recycler_view.adapter = CardAdapter(it)
                    recycler_view.scheduleLayoutAnimation()
                }

        // Note: this causes the initial request
        walletModule.xpub = "xpub6CfLQa8fLgtouvLxrb8EtvjbXfoC1yqzH6YbTJw4dP7srt523AhcMV8Uh4K3TWSHz9oDWmn9MuJogzdGU3ncxkBsAC9wFBLmFrWT9Ek81kQ"
    }

    override fun onPause() {
        super.onPause()
        // Note: Tidy up RX, abort any in progress calls and prevent memory leaks
        disposable.clear()
    }

}


