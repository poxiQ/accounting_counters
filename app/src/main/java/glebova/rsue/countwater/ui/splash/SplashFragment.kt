package glebova.rsue.countwater.ui.splash

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import glebova.rsue.countwater.AuthFragment
import glebova.rsue.countwater.AuthFragmentDirections
import glebova.rsue.countwater.base.BaseFragment
import glebova.rsue.countwater.databinding.FragmentSplashBinding
import org.json.JSONObject

var sPref: SharedPreferences? = null
var token = ""
var url = "http://192.168.43.35:8000"


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