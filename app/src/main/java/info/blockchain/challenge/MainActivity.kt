package info.blockchain.challenge

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import info.blockchain.challenge.api.MultiAddress
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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
                .subscribeBy {
                    Timber.d("Final balance: ${it.wallet.finalBalance}")
                    for (transaction in it.transactions) {
                        Timber.d("Transaction: ${transaction.result}")
                    }
                }
    }

    private fun timberHttpLoggingInterceptor() =
            HttpLoggingInterceptor(timberHttpLogger()).apply {
                level = HttpLoggingInterceptor.Level.BASIC
            }

    private fun timberHttpLogger() =
            HttpLoggingInterceptor.Logger { message -> Timber.d(message) }
}
