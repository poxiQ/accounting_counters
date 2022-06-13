package glebova.rsue.countwater.ui.splash

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import glebova.rsue.countwater.base.BaseFragment
import glebova.rsue.countwater.databinding.FragmentSplashBinding

import glebova.rsue.countwater.ui.token


class SplashFragment : BaseFragment<FragmentSplashBinding>() {

    override fun initializeBinding() = FragmentSplashBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        SharedPreferencesSingleton.init(requireActivity())
        token = SharedPreferencesSingleton.read("token", "").toString()
        if (token == "") {
            findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToGraphAuth())
        } else {
            findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToBottomNavFragment())
        }
    }
}