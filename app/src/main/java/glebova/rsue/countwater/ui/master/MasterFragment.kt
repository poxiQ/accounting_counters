package glebova.rsue.countwater.ui.master

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import glebova.rsue.countwater.adapters.MasterAdapter
import glebova.rsue.countwater.databinding.MasterFragmentBinding
import glebova.rsue.countwater.models.MasterModel
import java.util.*

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
        if (lists.count() == 0) {
            buildDisplayData()
        }
        initRecyclerView()
        binding.request.setOnClickListener {
            findNavController().navigate(MasterFragmentDirections.actionMasterFragmentToRequestFragment())
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun buildDisplayData() {
        lists.add(MasterModel("поверка счетчиков", SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(Date()).toString()))
    }

    private fun initRecyclerView() {
        MasterAdapter(lists).let {
            binding.masterRecycler.adapter = it
            adapter = it
        }
    }
}
