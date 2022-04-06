package glebova.rsue.countwater

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import glebova.rsue.countwater.base.BaseFragment
import glebova.rsue.countwater.databinding.FragmentAuthBinding
import glebova.rsue.countwater.ui.splash.token


class AuthFragment : BaseFragment<FragmentAuthBinding>() {

    override fun initializeBinding() = FragmentAuthBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.enter.setOnClickListener {
            when {
                binding.editTextTextPersonName.text.toString().trim().equals("") -> {
                    Toast.makeText(getActivity()?.getApplicationContext(), "заполните поле", Toast.LENGTH_LONG).show()
                }
                binding.editTextTextPassword.text.toString().trim().equals("") -> {
                    Toast.makeText(getActivity()?.getApplicationContext(), "заполните поле", Toast.LENGTH_LONG).show()
                }
                else -> {
                    token = binding.editTextTextPersonName.text.toString()
                    findNavController().navigate(AuthFragmentDirections.actionAuthFragmentToBottomNavFragment2())
                }
            }
        }
    }
}