package glebova.rsue.countwater

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import glebova.rsue.countwater.databinding.FragmentAuthBinding
import glebova.rsue.countwater.ui.master.token
import glebova.rsue.countwater.ui.pofile.ProfileFragmentDirections


class AuthFragment : Fragment() {

    private var _binding: FragmentAuthBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

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
                    findNavController().navigate(AuthFragmentDirections.actionAuthFragmentToProfileFragment3())
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}