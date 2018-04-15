package info.blockchain.challenge.modules

import info.blockchain.challenge.api.BlockchainService
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import retrofit2.Retrofit

val apiModule = Kodein.Module {
    bind<BlockchainService>() with provider { instance<Retrofit>().create(BlockchainService::class.java) }
}