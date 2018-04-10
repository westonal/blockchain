package info.blockchain.challenge.ui

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import info.blockchain.challenge.databinding.TransactionLayoutBinding
import info.blockchain.challenge.ui.viewmodel.Transaction

class TransactionsAdapter(private val transactions: List<Transaction>) :
        RecyclerView.Adapter<TransactionViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup,
                                    viewType: Int): TransactionViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val itemBinding = TransactionLayoutBinding.inflate(layoutInflater, parent, false)
        return TransactionViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: TransactionViewHolder, position: Int) {
        val item = transactions[position]
        holder.bind(item)
    }

    override fun getItemCount() = transactions.size
}

class TransactionViewHolder(private val binding: TransactionLayoutBinding) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: info.blockchain.challenge.ui.viewmodel.Transaction) {
        binding.item = item
        binding.executePendingBindings()
    }
}