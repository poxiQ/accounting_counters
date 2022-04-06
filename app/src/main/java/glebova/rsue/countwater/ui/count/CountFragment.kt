package glebova.rsue.countwater.ui.count

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import glebova.rsue.countwater.adapters.CountAdapter
import glebova.rsue.countwater.base.BaseFragment
import glebova.rsue.countwater.databinding.FragmentCountBinding
import glebova.rsue.countwater.models.CountModel

class CountFragment : BaseFragment<FragmentCountBinding>() {

    override fun initializeBinding() = FragmentCountBinding.inflate(layoutInflater)

    private lateinit var adapter_hot: CountAdapter
    private lateinit var adapter_cold: CountAdapter
    val counts_list_hot: MutableList<CountModel> = ArrayList()
    val counts_list_cold: MutableList<CountModel> = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.name = "Глебова Ульяна"
        binding.facecount = "123456789"
        if (counts_list_hot.count() == 0){
            buildDisplayData()
        }
        initRecyclerView()
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