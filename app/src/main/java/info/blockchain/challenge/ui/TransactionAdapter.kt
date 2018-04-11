package info.blockchain.challenge.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import info.blockchain.challenge.databinding.AccountLayoutBinding
import info.blockchain.challenge.databinding.ErrorLayoutBinding
import info.blockchain.challenge.databinding.TransactionLayoutBinding
import info.blockchain.challenge.ui.viewmodel.AccountCardViewModel
import info.blockchain.challenge.ui.viewmodel.CardViewModel
import info.blockchain.challenge.ui.viewmodel.ErrorCardViewModel
import info.blockchain.challenge.ui.viewmodel.TransactionCardViewModel

class CardAdapter(private val transactions: List<CardViewModel>) :
        RecyclerView.Adapter<CardViewHolder>() {

    override fun getItemViewType(position: Int) =
            when (transactions[position]) {
                is AccountCardViewModel -> 0
                is TransactionCardViewModel -> 1
                is ErrorCardViewModel -> 2
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            0 -> AccountViewHolder(AccountLayoutBinding.inflate(layoutInflater, parent, false))
            1 -> TransactionViewHolder(TransactionLayoutBinding.inflate(layoutInflater, parent, false))
            2 -> ErrorViewHolder(ErrorLayoutBinding.inflate(layoutInflater, parent, false))
            else -> throw RuntimeException("Unexpected viewType")
        }
    }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        val item = transactions[position]
        // Note: I'm able to ignore viewtype here in kotlin, just using this when block instead which does some of
        // the casting for me too
        when (holder) {
            is AccountViewHolder ->
                holder.bind(item as AccountCardViewModel)
            is TransactionViewHolder ->
                holder.bind(item as TransactionCardViewModel)
            is ErrorViewHolder ->
                holder.bind(item as ErrorCardViewModel)
        }
    }

    override fun getItemCount() = transactions.size
}

sealed class CardViewHolder(val view: View) : RecyclerView.ViewHolder(view)

// Note: The next 3 classes are quite similar, there is a way to reduce this boiler plate
private class TransactionViewHolder(
        private val binding: TransactionLayoutBinding
) : CardViewHolder(binding.root) {

    fun bind(item: TransactionCardViewModel) {
        binding.item = item
        binding.executePendingBindings()
    }
}

private class AccountViewHolder(
        private val binding: AccountLayoutBinding
) : CardViewHolder(binding.root) {

    fun bind(item: AccountCardViewModel) {
        binding.item = item
        binding.executePendingBindings()
    }
}

private class ErrorViewHolder(
        private val binding: ErrorLayoutBinding
) : CardViewHolder(binding.root) {

    fun bind(item: ErrorCardViewModel) {
        binding.item = item
        binding.executePendingBindings()
    }
}

