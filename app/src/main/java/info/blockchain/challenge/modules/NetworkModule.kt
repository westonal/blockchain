package info.blockchain.challenge.modules

import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.singleton
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber

val networkModule = Kodein.Module {

    bind() from singleton {
        // Note: Shared OkHttpClient instance is best practice, only used in another singleton, but should be
        // singleton in its own right anyway
        OkHttpClient.Builder()
                .addInterceptor(timberHttpLoggingInterceptor())
                .build()
    }

    bind() from singleton {
        Retrofit.Builder()
                .baseUrl("https://blockchain.info/")
                .addConverterFactory(GsonConverterFactory.create())
                // Note: this line puts all Rx calls on the IO scheduler, so no need to specify subscribeOn later
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .client(instance())
                .build()
    }
}

private fun timberHttpLoggingInterceptor() =
        HttpLoggingInterceptor(timberHttpLogger()).apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

// Note: By using Timber and my Timber setup, this is not logged in release mode
private fun timberHttpLogger() =
        HttpLoggingInterceptor.Logger { message -> Timber.d(message) }
