package glebova.rsue.countwater.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import glebova.rsue.countwater.R
import glebova.rsue.countwater.base.BaseFragment
import glebova.rsue.countwater.databinding.FragmentProfileBinding
import glebova.rsue.countwater.databinding.FragmentSettingsBinding
import glebova.rsue.countwater.ui.SettingsFragmentDirections.Companion.actionSettingsFragmentToProfileFragment
import glebova.rsue.countwater.ui.pofile.ProfileFragmentDirections
import glebova.rsue.countwater.ui.splash.sPref
import glebova.rsue.countwater.ui.splash.token
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


@DelicateCoroutinesApi
class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {

    private val client = OkHttpClient()

    override fun initializeBinding() = FragmentSettingsBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.logout.setOnClickListener {
            sPref = activity?.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
            val ed: SharedPreferences.Editor = sPref!!.edit()
            ed.putString("token", "")
            ed.apply()
            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToMainGraph2())
        }
        binding.arrow.setOnClickListener {
            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToProfileFragment())
        }
        binding.buttonSend.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                send()
            }
        }
    }
    private fun send() {
        val formBody = FormBody.Builder()
            .add("username", binding.textInputFio.text.toString())
            .add("address", binding.textInputAddress.text.toString())
            .add("telephone", binding.textInputTelephone.text.toString())
            .build()
        val request = Request.Builder()
            .url("http://192.168.43.35:8080/water/service/")
            .put(formBody)
            .addHeader("Authorization", "Token $token")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToProfileFragment())
        }
    }

}
