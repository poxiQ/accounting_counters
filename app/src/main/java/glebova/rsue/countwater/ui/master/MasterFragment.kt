package glebova.rsue.countwater.ui.master

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import glebova.rsue.countwater.adapters.CountAdapter
import glebova.rsue.countwater.adapters.MasterAdapter
import glebova.rsue.countwater.databinding.MasterFragmentBinding
import glebova.rsue.countwater.models.MasterModel
import glebova.rsue.countwater.ui.count.CountFragmentDirections

class MasterFragment : Fragment() {

    private lateinit var adapter: MasterAdapter
    val lists: MutableList<MasterModel> = ArrayList()

    private var _binding: MasterFragmentBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = MasterFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (lists.count() == 0){
            buildDisplayData()
        }
        initRecyclerView()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun buildDisplayData() {
        lists.add(MasterModel("поверка счетчиков"))
    }

    private fun initRecyclerView() {
        MasterAdapter(lists) { onButtonCLicked() }.let {
            binding.masterRecycler.adapter = it
            adapter = it
        }
    }

    private fun onButtonCLicked() =
        findNavController().navigate(CountFragmentDirections.actionCountFragmentToBlankFragment())
}
