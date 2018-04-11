package info.blockchain.challenge

import android.app.Application
import info.blockchain.challenge.modules.apiModule
import info.blockchain.challenge.modules.mviModule
import info.blockchain.challenge.modules.networkModule
import org.kodein.di.Kodein
import org.kodein.di.KodeinAware
import timber.log.Timber
import timber.log.Timber.DebugTree

class App : Application(), KodeinAware {

    override val kodein: Kodein = Kodein {
        import(networkModule)
        // Note: this one's from the api library
        import(apiModule)
        import(mviModule)
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(DebugTree())
        }
    }
}
