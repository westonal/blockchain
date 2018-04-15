package info.blockchain.challenge.modules

import info.blockchain.challenge.ui.WalletIntent
import info.blockchain.challenge.ui.WalletMviDialog
import io.reactivex.Observable
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.factory
import org.kodein.di.generic.instance

val mviModule = Kodein.Module {

    bind<WalletMviDialog>() with factory { intents: Observable<WalletIntent> -> WalletMviDialog(instance(), intents) }
}
