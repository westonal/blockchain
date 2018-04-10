package info.blockchain.challenge.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import info.blockchain.challenge.databinding.AccountLayoutBinding
import info.blockchain.challenge.databinding.TransactionLayoutBinding
import info.blockchain.challenge.ui.viewmodel.Account
import info.blockchain.challenge.ui.viewmodel.Card
import info.blockchain.challenge.ui.viewmodel.Transaction

class CardAdapter(private val transactions: List<Card>) :
        RecyclerView.Adapter<CardViewHolder>() {

    override fun getItemViewType(position: Int) =
            when (transactions[position]) {
                is Account -> 0
                is Transaction -> 1
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            0 -> {
                val itemBinding = AccountLayoutBinding.inflate(layoutInflater, parent, false)
                AccountViewHolder(itemBinding)
            }
            1 -> {
                val itemBinding = TransactionLayoutBinding.inflate(layoutInflater, parent, false)
                TransactionViewHolder(itemBinding)
            }
            else -> throw RuntimeException("Unexpected viewType")
        }
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val item = transactions[position]
        // Note: I'm able to ignore viewtype here in kotlin, just using this when block instead which does some of
        // the casting for me too
        when (holder) {
            is AccountViewHolder ->
                holder.bind(item as Account)
            is TransactionViewHolder ->
                holder.bind(item as Transaction)
        }
    }

    override fun getItemCount() = transactions.size
}

sealed class CardViewHolder(val view: View) : RecyclerView.ViewHolder(view)

private class TransactionViewHolder(
        private val binding: TransactionLayoutBinding
) : CardViewHolder(binding.root) {

    fun bind(item: Transaction) {
        binding.item = item
        binding.executePendingBindings()
    }
}

private class AccountViewHolder(
        private val binding: AccountLayoutBinding
) : CardViewHolder(binding.root) {

    fun bind(item: Account) {
        binding.item = item
        binding.executePendingBindings()
    }
}
