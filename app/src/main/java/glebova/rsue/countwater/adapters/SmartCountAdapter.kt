package glebova.rsue.countwater.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import glebova.rsue.countwater.databinding.SmartCountsBinding
val counts: MutableList<String> = ArrayList()

class SmartCountAdapter(var lists: MutableList<Int>) :
    RecyclerView.Adapter<SmartCountAdapter.CountViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountViewHolder = CountViewHolder(
        SmartCountsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: CountViewHolder, position: Int) = holder.bind()

    override fun getItemCount(): Int = lists.size

    inner class CountViewHolder(private val binding: SmartCountsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind() {
            counts.add(binding.textInputTelephone.text.toString())
        }
    }
}