package info.blockchain.challenge.ui

import android.databinding.ViewDataBinding
import android.support.v7.util.DiffUtil
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import info.blockchain.challenge.databinding.AccountLayoutBinding
import info.blockchain.challenge.databinding.ErrorLayoutBinding
import info.blockchain.challenge.databinding.TransactionLayoutBinding
import info.blockchain.challenge.ui.viewmodel.AccountCardViewModel
import info.blockchain.challenge.ui.viewmodel.CardViewModel
import info.blockchain.challenge.ui.viewmodel.CardViewModelDiffCallback
import info.blockchain.challenge.ui.viewmodel.ErrorCardViewModel
import info.blockchain.challenge.ui.viewmodel.TransactionCardViewModel

class CardAdapter : RecyclerView.Adapter<CardViewHolder>() {

    private val cards: MutableList<CardViewModel> = mutableListOf()

    init {
        // Note: tells it we have ids and RecyclerView can then manage the change animations
        setHasStableIds(true)
    }

    override fun getItemId(position: Int): Long {
        return (cards[position] as? TransactionCardViewModel)?.id ?: 0
    }

    override fun getItemViewType(position: Int) =
            when (cards[position]) {
                is AccountCardViewModel -> 0
                is TransactionCardViewModel -> 1
                is ErrorCardViewModel -> 2
            }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardViewHolder =
            CardViewHolder(newBinding(viewType, LayoutInflater.from(parent.context), parent))

    private fun newBinding(viewType: Int, layoutInflater: LayoutInflater, parent: ViewGroup) =
            when (viewType) {
                0 -> AccountLayoutBinding.inflate(layoutInflater, parent, false)
                1 -> TransactionLayoutBinding.inflate(layoutInflater, parent, false)
                2 -> ErrorLayoutBinding.inflate(layoutInflater, parent, false)
                else -> throw RuntimeException("Unexpected viewType")
            }

    override fun onBindViewHolder(holder: CardViewHolder, position: Int) {
        holder.bind(cards[position])
    }

    override fun getItemCount() = cards.size

    fun updateList(newCards: List<CardViewModel>) {
        // Note: technically diffUtil calculateDiff can and should happen on a background thread
        DiffUtil.calculateDiff(CardViewModelDiffCallback(cards, newCards)).dispatchUpdatesTo(this)
        cards.clear()
        cards.addAll(newCards)
    }

    fun isEmpty() = cards.isEmpty()
}

class CardViewHolder(
        private val binding: ViewDataBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: Any) {
        binding.setVariable(info.blockchain.challenge.BR.item, item)
        binding.executePendingBindings()
    }
}