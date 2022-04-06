package glebova.rsue.countwater.ui.splash

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import glebova.rsue.countwater.base.BaseFragment
import glebova.rsue.countwater.databinding.FragmentSplashBinding

var token: String = ""

class SplashFragment : BaseFragment<FragmentSplashBinding>() {

    override fun initializeBinding() = FragmentSplashBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (token == "") {
            findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToGraphAuth())
        } else {
            findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToBottomNavFragment())
        }
    }
}