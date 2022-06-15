package glebova.rsue.countwater.ui.pofile

import SharedPreferencesSingleton
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import glebova.rsue.countwater.base.BaseFragment
import glebova.rsue.countwater.databinding.FragmentProfileBinding


class ProfileFragment : BaseFragment<FragmentProfileBinding>() {

    override fun initializeBinding() = FragmentProfileBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.fio.text = SharedPreferencesSingleton.read("fullname", "")
        binding.address.text = SharedPreferencesSingleton.read("place", "")
        binding.telephone.text = SharedPreferencesSingleton.read("number_phone", "")
        binding.date.text = SharedPreferencesSingleton.read("day_of_metersdata", "")
        binding.settings.setOnClickListener {
            findNavController().navigate(ProfileFragmentDirections.actionProfileFragmentToSettingsFragment())
        }
    }
}
