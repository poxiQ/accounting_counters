package glebova.rsue.countwater.ui.count

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import glebova.rsue.countwater.adapters.CountAdapter
import glebova.rsue.countwater.databinding.CountFragmentBinding
import glebova.rsue.countwater.models.CountModel

class CountFragment : Fragment() {

    private lateinit var adapter_hot: CountAdapter
    private lateinit var adapter_cold: CountAdapter
    val counts_list_hot: MutableList<CountModel> = ArrayList()
    val counts_list_cold: MutableList<CountModel> = ArrayList()

    private var _binding: CountFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = CountFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.name = "Глебова Ульяна"
        binding.facecount = "123456789"
        if (counts_list_hot.count() == 0){
            buildDisplayData()
        }
        initRecyclerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun buildDisplayData() {
        counts_list_hot.add(CountModel(1, "218363Н16"))
        counts_list_hot.add(CountModel(2, "213563Н16"))
        counts_list_cold.add(CountModel(1, "287563Н16"))
        counts_list_cold.add(CountModel(2, "213223Н16"))
    }

    private fun initRecyclerView() {
        CountAdapter(counts_list_hot) { onButtonCLicked() }.let {
            binding.countsRecyclerHot.adapter = it
            adapter_hot = it
        }
        CountAdapter(counts_list_cold) { onButtonCLicked() }.let {
            adapter_cold = it
            binding.countsRecyclerCold.adapter = it
        }
    }

    private fun onButtonCLicked() =
        findNavController().navigate(CountFragmentDirections.actionCountFragmentToBlankFragment())
}