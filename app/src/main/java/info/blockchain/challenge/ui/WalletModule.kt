package info.blockchain.challenge.ui

import info.blockchain.challenge.R
import info.blockchain.challenge.api.MultiAddress
import info.blockchain.challenge.api.Result
import info.blockchain.challenge.mapToCardViewModels
import info.blockchain.challenge.ui.viewmodel.CardViewModel
import info.blockchain.challenge.ui.viewmodel.ErrorCardViewModel
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

class WalletModule(
        private val service: MultiAddress
) {
    private val subject = PublishSubject.create<String>()

    val cardViewModels: Observable<List<CardViewModel>> = subject.flatMapSingle { xpub ->
        service.multiaddr(xpub)
                .map(Result::mapToCardViewModels)
                .onErrorReturn {
                    listOf(
                            ErrorCardViewModel(
                                    message = R.string.generic_error_retry,
                                    // Note: the Error card View Model is told how to refresh
                                    retry = { subject.onNext(xpub) }
                            ))
                }
    }

    var xpub: String = ""
        set(value) {
            if (value != field) {
                subject.onNext(value)
                field = value
            }
        }
}