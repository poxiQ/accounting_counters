package glebova.rsue.countwater.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.View.INVISIBLE
import android.view.ViewGroup
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import glebova.rsue.countwater.R
import glebova.rsue.countwater.databinding.NumberItemBinding
import glebova.rsue.countwater.models.CountModel
import java.text.SimpleDateFormat
import java.util.*

class CountAdapter(var counters: List<CountModel>, private var callback: (String) -> Unit) :
    RecyclerView.Adapter<CountAdapter.CountViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountViewHolder = CountViewHolder(
        NumberItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
    )

    override fun onBindViewHolder(holder: CountViewHolder, position: Int) = holder.bind()

    override fun getItemCount(): Int = counters.size

    inner class CountViewHolder(private val binding: NumberItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("ResourceAsColor")
        fun bind() {
            binding.number.text = counters[bindingAdapterPosition].title
            if (counters[bindingAdapterPosition].isclever == false) {
                binding.buttonAddcount.isEnabled = false
                binding.buttonAddcount.setBackgroundColor(R.color.grey)
            }
            if (SimpleDateFormat("dd",
                    Locale.getDefault()).format(Date()) != SharedPreferencesSingleton.read("day_of_metersdata", "")
            ) {
                binding.buttonAddcount.isEnabled = false
                binding.buttonAddcount.setBackgroundColor(R.color.grey)
            }
            binding.buttonAddcount.setOnClickListener {
                callback.invoke(binding.number.text as String)
            }
        }
    }
}





