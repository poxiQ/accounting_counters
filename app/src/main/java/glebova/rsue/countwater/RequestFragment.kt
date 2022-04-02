package glebova.rsue.countwater

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import glebova.rsue.countwater.databinding.FragmentRequestBinding
import glebova.rsue.countwater.databinding.MasterFragmentBinding
import glebova.rsue.countwater.ui.master.MasterFragmentDirections

class RequestFragment : Fragment() {
    private var _binding: FragmentRequestBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentRequestBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonSend.setOnClickListener {
            findNavController().navigate(RequestFragmentDirections.actionRequestFragmentToMasterFragment())
        }
    }
}