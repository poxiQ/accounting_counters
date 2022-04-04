package glebova.rsue.countwater.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import glebova.rsue.countwater.databinding.NumberItemBinding
import glebova.rsue.countwater.models.CountModel

class CountAdapter(var counters: List<CountModel>, private val callback: CountAdapterCallBack) :
    RecyclerView.Adapter<CountAdapter.CountViewHolder>() {

    fun interface CountAdapterCallBack {
        fun onClick()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountViewHolder = CountViewHolder(
        NumberItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: CountViewHolder, position: Int) = holder.bind()

    override fun getItemCount(): Int = counters.size

    inner class CountViewHolder(private val binding: NumberItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            binding.number.text = counters[bindingAdapterPosition].title
            binding.buttonAdd.setOnClickListener {
                callback.onClick()
            }
        }
    }
}





