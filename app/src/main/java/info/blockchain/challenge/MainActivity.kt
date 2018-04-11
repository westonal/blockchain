package info.blockchain.challenge

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import info.blockchain.challenge.ui.CardAdapter
import info.blockchain.challenge.ui.WalletMviDialog
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance

class MainActivity : AppCompatActivity(), KodeinAware {
    override val kodein by closestKodein()

    private val walletMviDialog: WalletMviDialog by instance()

    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recycler_view.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() {
        super.onResume()

        disposable += walletMviDialog
                .cardViewModels
                // Note: Only now do I switch to the main thread, just before we need to update the UI
                // the Module has mapped the service call to card view models for me already and is fully test driven
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy {
                    recycler_view.adapter = CardAdapter(it)
                    recycler_view.scheduleLayoutAnimation()
                }

        // Note: this causes the initial request
        walletMviDialog.xpub = "xpub6CfLQa8fLgtouvLxrb8EtvjbXfoC1yqzH6YbTJw4dP7srt523AhcMV8Uh4K3TWSHz9oDWmn9MuJogzdGU3ncxkBsAC9wFBLmFrWT9Ek81kQ"
    }

    override fun onPause() {
        super.onPause()
        // Note: Tidy up RX, abort any in progress calls and prevent memory leaks
        disposable.clear()
    }

}


