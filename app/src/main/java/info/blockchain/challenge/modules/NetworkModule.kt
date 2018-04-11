package info.blockchain.challenge.modules

import info.blockchain.challenge.timberHttpLoggingInterceptor
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.singleton
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

val networkModule = Kodein.Module {
    bind() from singleton { retrofit() }
}

private fun retrofit() = Retrofit.Builder()
        .baseUrl("https://blockchain.info/")
        .addConverterFactory(GsonConverterFactory.create())
        // Note: this line puts all Rx calls on the IO scheduler, so no need to specify subscribeOn later
        .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
        .client(OkHttpClient.Builder()
                .addInterceptor(timberHttpLoggingInterceptor())
                .build())
        .build()