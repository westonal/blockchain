package info.blockchain.challenge.ui

import info.blockchain.challenge.Mapper
import info.blockchain.challenge.R
import info.blockchain.challenge.api.MultiAddress
import info.blockchain.challenge.mapToCardViewModels
import info.blockchain.challenge.ui.viewmodel.CardViewModel
import info.blockchain.challenge.ui.viewmodel.ErrorCardViewModel
import info.blockchain.challenge.ui.viewmodel.WalletViewModel
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject

// Note: the events are used as input to the MVI dialog, more on that later
sealed class WalletEvent

class NewXpub(val xpub: String) : WalletEvent()

class Refresh : WalletEvent()

// Note: Data class because we're using the equality check for distinct. It also gives us the "copy" constructor.
private data class State(
        val xpub: String = "",
        val retryCount: Int = 0
)

/**
 * Note: The pattern I'm going for is Model View Intent, and the term dialog here refers to the users "dialog" with
 * the UI, the user could set xpubs as input, and this pumps out a stream of Lists of [CardViewModel] to update UI with.
 *
 * It's job is to apply events in a predictable order and emit an observable view model. It is free from race
 * conditions and very easy to test.
 */
class WalletMviDialog(
        private val service: MultiAddress,
        // Note: sending events is how you interact with the dialog
        events: Observable<WalletEvent>
) {
    // Note: Internal events are merged with external events, allowing this class to expose the ability to refresh to
    // outside
    private val internalEvents = PublishSubject.create<WalletEvent>()

    private val allEvents = events.mergeWith(internalEvents)

    // Note: this "scan" method is the key to MVI, and allows this dialog to be immutable
    private val subject = allEvents.scan(State(),
            { previousState, event ->
                when (event) {
                    is NewXpub -> State(xpub = event.xpub)
                    is Refresh -> previousState.copy(retryCount = previousState.retryCount + 1)
                }
            })

    val viewModel: Observable<WalletViewModel> = subject
            .skip(1) // Note: We know first xpub is empty string
            .distinctUntilChanged()
            .map { it.xpub }
            .flatMapSingle { xpub ->
                service.multiaddr(xpub)
                        .map { it.mapToCardViewModels(Mapper(xpub)) }
                        .onErrorReturn {
                            listOf(
                                    ErrorCardViewModel(
                                            message = R.string.generic_error_retry,
                                            // Note: the Error card View Model is told how to refresh
                                            retry = { internalEvents.onNext(Refresh()) }
                                    ))
                        }
            }
            .map { WalletViewModel(it) }
}