package info.blockchain.challenge

import io.reactivex.observers.TestObserver

// Note: no cleaner way I know of doing this, there is a predicate function, but I want to use asserts for more
// descriptive test errors
fun <T> TestObserver<T>.applyAssertsToValue(assertFunction: (T) -> Unit) =
        this.applyAssertsToValue(0, assertFunction)

fun <T> TestObserver<T>.applyAssertsToValue(atIndex: Int, assertFunction: (T) -> Unit) =
        this.also {
            this.assertValueAt(atIndex) {
                assertFunction(it)
                true
            }
        }
