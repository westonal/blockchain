package info.blockchain.challenge.api

import com.google.gson.annotations.SerializedName
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MultiAddress {

    @GET("multiaddr")
    fun multiaddr(@Query("active") xpub: String): Single<Result>

}

class Result(
        @SerializedName("wallet")
        val wallet: Wallet,

        @SerializedName("txs")
        val transactions: List<Transaction>
)

class Wallet(
        @SerializedName("final_balance")
        val finalBalance: Long
)

class Transaction (
    @SerializedName("result")
    val result: Long
)
