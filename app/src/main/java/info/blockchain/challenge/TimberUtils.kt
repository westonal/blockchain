package info.blockchain.challenge

import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber

fun timberHttpLoggingInterceptor() =
        HttpLoggingInterceptor(timberHttpLogger()).apply {
            level = HttpLoggingInterceptor.Level.BASIC
        }

// Note: By using Timber and my Timber setup, this is not logged in release mode
fun timberHttpLogger() =
        HttpLoggingInterceptor.Logger { message -> Timber.d(message) }