package glebova.rsue.countwater.ui.pofile

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import glebova.rsue.countwater.base.BaseFragment
import glebova.rsue.countwater.databinding.FragmentProfileBinding
import glebova.rsue.countwater.ui.splash.sPref


class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    override fun initializeBinding() = FragmentProfileBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fio.text = sPref!!.getString("fullname", "").toString()
        binding.address.text = sPref!!.getString("place", "").toString()
        binding.telephone.text = sPref!!.getString("number_phone", "").toString()
        binding.settings.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToSettingsFragment())
        }

    }

}
