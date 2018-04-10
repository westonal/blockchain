package info.blockchain.challenge.ui

import android.content.Context
import android.databinding.DataBindingUtil
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import info.blockchain.challenge.R
import info.blockchain.challenge.databinding.TransactionLayoutBinding
import info.blockchain.challenge.ui.viewmodel.Transaction

class TransactionListAdapter(
        context: Context?,
        private val list: List<Transaction>
) : ArrayAdapter<Transaction>(context, R.layout.transaction_layout, list) {

    private val inflater = LayoutInflater.from(getContext())

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val binding: TransactionLayoutBinding =
                if (convertView != null) {
                    DataBindingUtil.bind(convertView)
                } else {
                    DataBindingUtil.inflate(inflater, R.layout.transaction_layout, parent, false)
                }
        binding.item = list[position]
        binding.executePendingBindings()
        return binding.root
    }
}