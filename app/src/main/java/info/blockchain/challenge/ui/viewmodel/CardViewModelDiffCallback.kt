package info.blockchain.challenge.ui.viewmodel

import android.support.v7.util.DiffUtil

class CardViewModelDiffCallback(
        private val oldItems: List<CardViewModel>,
        private val newItems: List<CardViewModel>
) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldCardViewModel = oldItems[oldItemPosition]
        val newCardViewModel = newItems[newItemPosition]
        if (oldCardViewModel is TransactionCardViewModel && newCardViewModel is TransactionCardViewModel) {
            return oldCardViewModel.id == newCardViewModel.id
        }
        return false
    }

    override fun getOldListSize() = oldItems.size

    override fun getNewListSize() = newItems.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            areItemsTheSame(oldItemPosition, newItemPosition)

}