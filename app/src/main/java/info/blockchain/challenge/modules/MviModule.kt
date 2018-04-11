package info.blockchain.challenge.modules

import info.blockchain.challenge.ui.WalletMviDialog
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider

val mviModule = Kodein.Module {

    bind<WalletMviDialog>() with provider { WalletMviDialog(instance()) }
}
