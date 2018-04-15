package info.blockchain.challenge.ui

import info.blockchain.challenge.Mapper
import info.blockchain.challenge.R
import info.blockchain.challenge.api.BlockchainService
import info.blockchain.challenge.mapToCardViewModels
import info.blockchain.challenge.ui.viewmodel.CardViewModel
import info.blockchain.challenge.ui.viewmodel.ErrorCardViewModel
import info.blockchain.challenge.ui.viewmodel.WalletViewModel
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.PublishSubject

// Note: intents are used as input to the MVI dialog, more detail below
sealed class WalletIntent

class NewXpubIntent(val xpub: String) : WalletIntent()

class RefreshIntent : WalletIntent()

// Note: Data class because we're using the equality check for distinct. It also gives us the "copy" constructor.
private data class State(
        val xpub: String = "",
        val retryCount: Int = 0
)

/**
 * Note: The pattern I'm going for is Model View Intent, and the term dialog here refers to the users "dialog" with
 * the UI, the user could set xpubs as input, and this pumps out a stream of Lists of [CardViewModel] to update UI with.
 *
 * It's job is to apply intents in a predictable order and emit an observable view model. It is free from race
 * conditions and very easy to test.
 */
class WalletMviDialog(
        private val service: BlockchainService,
        // Note: sending intents is how you interact with the dialog
        intents: Observable<WalletIntent>
) {
    // Note: Internal intents are merged with external intents, allowing this class to expose the ability to refresh to
    // outside
    private val internalEvents = PublishSubject.create<WalletIntent>()

    private val allEvents = intents.mergeWith(internalEvents)

    // Note: this "scan" method is the key to MVI, and allows this dialog to be immutable
    private val subject = allEvents.scan(State(),
            { previousState, event ->
                when (event) {
                    is NewXpubIntent -> State(xpub = event.xpub)
                    is RefreshIntent -> previousState.copy(retryCount = previousState.retryCount + 1)
                }
            })

    val viewModel: Observable<WalletViewModel> = subject
            .skip(1) // Note: We know first xpub is empty string
            .distinctUntilChanged()
            .flatMapSingle { state ->
                viewModelForXpub(state.xpub)
            }

    private fun viewModelForXpub(xpub: String): Single<WalletViewModel> {
        return service.multiAddress(xpub)
                .map { it.mapToCardViewModels(Mapper(xpub)) }
                .onErrorReturn {
                    listOf(
                            ErrorCardViewModel(
                                    message = R.string.generic_error_retry,
                                    // Note: the Error card View Model is told how to refresh
                                    retry = { internalEvents.onNext(RefreshIntent()) }
                            ))
                }
                .map { WalletViewModel(it) }
    }
}