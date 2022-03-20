package glebova.rsue.countwater.ui.count

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import glebova.rsue.countwater.adapters.CountAdapter
import glebova.rsue.countwater.databinding.CountFragmentBinding
import glebova.rsue.countwater.models.CountModel

class CountFragment : Fragment() {

    companion object {
        fun newInstance() = CountFragment()
    }

    private lateinit var viewModel: CountViewModel
    private lateinit var adapter_hot: CountAdapter
    private lateinit var adapter_cold: CountAdapter
    val counts_list_hot: MutableList<CountModel> = ArrayList()
    val counts_list_cold: MutableList<CountModel> = ArrayList()

    private var _binding: CountFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = CountFragmentBinding.inflate(inflater, container, false)
        val view = binding.root
        binding.name = "Глебова Ульяна"
        binding.facecount = "123456789"
        bildDisplayData()
        initRecyclerView(view)
        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun bildDisplayData() {
        counts_list_hot.add(CountModel(1, "218363Н16"))
        counts_list_hot.add(CountModel(2, "213563Н16"))
        counts_list_cold.add(CountModel(1, "287563Н16"))
        counts_list_cold.add(CountModel(2, "213223Н16"))
    }

    private fun initRecyclerView(view: View){
        binding.countsRecyclerHot.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        binding.countsRecyclerCold.layoutManager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)
        adapter_hot = CountAdapter(counts_list_hot)
        adapter_cold = CountAdapter(counts_list_cold)
        binding.countsRecyclerHot.adapter = adapter_hot
        binding.countsRecyclerCold.adapter = adapter_cold
    }


//        override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
//            super.onViewCreated(itemView, savedInstanceState)
//            R.id.countsRecycler.apply {
//                layoutManager = LinearLayoutManager(activity)
//                // set the custom adapter to the RecyclerView
////                countAdapter = CountAdapter()
//            }
//        }
    }

//
//    override fun onActivityCreated(savedInstanceState: Bundle?) {
//        super.onActivityCreated(savedInstanceState)
//        viewModel = ViewModelProvider(this).get(CountViewModel::class.java)
//    }

//}