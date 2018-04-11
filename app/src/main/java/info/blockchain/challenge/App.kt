package info.blockchain.challenge

import android.app.Application
import info.blockchain.challenge.modules.networkModule
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import timber.log.Timber
import timber.log.Timber.DebugTree

class App : Application(), KodeinAware {

    override val kodein: Kodein = Kodein {
        import(networkModule)
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }
}
