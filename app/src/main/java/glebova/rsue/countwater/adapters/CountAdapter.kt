package glebova.rsue.countwater.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import glebova.rsue.countwater.BlankFragment
import glebova.rsue.countwater.R
import glebova.rsue.countwater.models.CountModel

class CountAdapter(var counters: List<CountModel>, val callback: CountAdapterCallBack) :
    RecyclerView.Adapter<CountAdapter.CountViewHolder>() {
    fun interface CountAdapterCallBack{
        fun onClick()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountViewHolder {
        val countsItems =
            LayoutInflater.from(parent.context).inflate(R.layout.number_item, parent, false)
        return CountViewHolder(countsItems)
    }


    override fun onBindViewHolder(holder: CountViewHolder, position: Int) {
        holder.countTitle.text = counters.get(position).title
        holder.itemView.setOnClickListener(object: View.OnClickListener {
            override fun onClick(v: View?) {

                val activity = v!!.context as AppCompatActivity
                val fragment = BlankFragment()
                activity.supportFragmentManager.beginTransaction().replace(R.id.count, fragment)
                    .addToBackStack(null).commit()


            }

        })
    }

    override fun getItemCount(): Int {
        return counters.size
    }

    class CountViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var countTitle: TextView
        private var buttonAdd: Button
        private lateinit var fragment: BlankFragment
        private lateinit var fragmentManager: FragmentManager

        init {
            countTitle = itemView.findViewById(R.id.number)
            buttonAdd = itemView.findViewById(R.id.buttonAdd)
            buttonAdd.setOnClickListener {
                fragment = BlankFragment()
//                fragment.show(fragmentManager!!.beginTransaction(), null)
//                fragmentManager = getFragmentManager()

            }

        }
    }

}

