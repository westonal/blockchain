package info.blockchain.challenge.api

import com.google.gson.Gson
import info.blockchain.challenge.resource
import org.amshove.kluent.`should be`
import org.amshove.kluent.`should equal`
import org.junit.Test

class ApiResponseDeserializationTests {

    @Test
    fun `can deserialize wallet balance`() {
        givenResponse().wallet.finalBalance `should equal` 929236
    }

    @Test
    fun `can deserialize transactions`() {
        givenResponse().apply {
            transactions.size `should equal` 2
            transactions[0].apply {
                result `should equal` -21121
                timeStamp `should equal` 1523550526
            }
            transactions[1].apply {
                result `should equal` -40863
                timeStamp `should equal` 1511799512
            }
        }
    }

    @Test
    fun `can deserialize transaction outputs`() {
        givenResponse().transactions[0].apply {
            outputs.size `should equal` 2
            outputs[0].apply {
                address `should equal` "1HRP4HW2qjumc8WSgKXzCN6Kv41XwjVKeE"
                xpub!!.m `should equal` "xpub6CfLQa8fLgtouvLxrb8EtvjbXfoC1yqzH6YbTJw4dP7srt523AhcMV8Uh4K3TWSHz9oDWmn9MuJogzdGU3ncxkBsAC9wFBLmFrWT9Ek81kQ"
            }
            outputs[1].apply {
                address `should equal` "1FGyXV6P2GCYaAjX8MtSM7pEEBLW9S3XT3"
                xpub `should be` null
            }
        }
    }

    private fun givenResponse() =
            Gson().fromJson(resource("multiaddressResponse.json"), Result::class.java)
}
