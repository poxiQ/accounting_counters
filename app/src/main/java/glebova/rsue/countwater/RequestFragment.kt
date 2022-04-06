package glebova.rsue.countwater

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import glebova.rsue.countwater.base.BaseFragment
import glebova.rsue.countwater.databinding.FragmentRequestBinding

class RequestFragment : BaseFragment<FragmentRequestBinding>() {

    override fun initializeBinding() = FragmentRequestBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonSend.setOnClickListener {
            findNavController().navigate(RequestFragmentDirections.actionRequestFragmentToMasterFragment())
        }
    }
}