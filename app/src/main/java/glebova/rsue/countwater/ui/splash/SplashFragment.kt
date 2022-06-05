package glebova.rsue.countwater.ui.splash

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import glebova.rsue.countwater.base.BaseFragment
import glebova.rsue.countwater.databinding.FragmentSplashBinding
import glebova.rsue.countwater.ui.sPref
import glebova.rsue.countwater.ui.token


class SplashFragment : BaseFragment<FragmentSplashBinding>() {

    override fun initializeBinding() = FragmentSplashBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sPref = this.activity?.getSharedPreferences("MyPref", MODE_PRIVATE)
        token = sPref!!.getString("token", "").toString()
        if (token == "") {
            findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToGraphAuth())
        } else {
            findNavController().navigate(SplashFragmentDirections.actionSplashFragmentToBottomNavFragment())
        }
    }
}