package info.blockchain.challenge

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import info.blockchain.challenge.ui.CardAdapter
import info.blockchain.challenge.ui.NewXpubIntent
import info.blockchain.challenge.ui.RefreshIntent
import info.blockchain.challenge.ui.WalletIntent
import info.blockchain.challenge.ui.WalletMviDialog
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.activity_main.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.closestKodein
import org.kodein.di.generic.instance
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity(), KodeinAware {
    override val kodein by closestKodein()

    private val walletIntents = PublishSubject.create<WalletIntent>()
    private val walletMviDialog: WalletMviDialog by instance(arg = walletIntents as Observable<WalletIntent>)

    private val disposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recycler_view.layoutManager = LinearLayoutManager(this)
        swipe_refresh_layout.setOnRefreshListener { walletIntents.onNext(RefreshIntent()) }
    }

    override fun onResume() {
        super.onResume()

        swipe_refresh_layout.isRefreshing = true

        disposable += walletMviDialog
                .viewModel
                // Note: It can be too quick to see progress spinner, so just putting a false delay in for demo
                .delay(1, TimeUnit.SECONDS)
                // Note: Only now do I switch to the main thread, just before we need to update the UI
                // the Module has mapped the service call to card view models for me already and is fully test driven
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeBy {
                    recycler_view.adapter = CardAdapter(it.cards)
                    recycler_view.scheduleLayoutAnimation()
                    swipe_refresh_layout.isRefreshing = false
                }

        // Note: this causes the initial request
        walletIntents.onNext(
                NewXpubIntent("xpub6CfLQa8fLgtouvLxrb8EtvjbXfoC1yqzH6YbTJw4dP7srt523AhcMV8Uh4K3TWSHz9oDWmn9MuJogzdGU3ncxkBsAC9wFBLmFrWT9Ek81kQ")
        )
    }

    override fun onPause() {
        super.onPause()
        // Note: Tidy up RX, abort any in progress calls and prevent memory leaks
        disposable.clear()
    }

}


