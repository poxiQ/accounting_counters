package glebova.rsue.countwater.ui

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
import glebova.rsue.countwater.ui.pofile.ProfileFragmentDirections
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
            GlobalScope.launch(Dispatchers.IO) {
                logout()
            }
        }
        binding.arrow.setOnClickListener {
            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragment2ToProfileFragment2())
        }
        binding.buttonSend.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                send()
            }
        }
    }
    private fun logout() {
        val formBody = FormBody.Builder()
            .add("token", token)
            .build()
        val request = Request.Builder()
            .url("http://192.168.43.35:8080/water/logout/")
            .addHeader("Authorization", "Token $token")
            .delete(formBody)
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragment2ToAuthFragment2())
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
            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragment2ToProfileFragment2())
        }
    }

}
