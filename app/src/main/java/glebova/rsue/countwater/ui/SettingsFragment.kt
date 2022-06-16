package glebova.rsue.countwater.ui

import SharedPreferencesSingleton
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.fragment.findNavController
import glebova.rsue.countwater.MainActivity
import glebova.rsue.countwater.R
import glebova.rsue.countwater.base.BaseFragment
import glebova.rsue.countwater.databinding.FragmentSettingsBinding
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException


@DelicateCoroutinesApi
class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {

    private val client = OkHttpClient()
    var api_key = ""
    override fun initializeBinding() = FragmentSettingsBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        response = ""
        GlobalScope.launch(Dispatchers.IO) { response = get() }
        while (response == "") { continue }
        when (response) {
            "Exception" -> {
                Toast.makeText(activity?.applicationContext, "проверьте подключение к сети", Toast.LENGTH_LONG)
                    .show()
                findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToSplashFragment5())
            } else -> {
            binding.textInputFio.setText(JSONObject(response).getString("fullname"))
            binding.textInputAddress.setText(JSONObject(response).getString("place"))
            binding.textInputTelephone.setText(JSONObject(response).getString("number_phone"))
            binding.textInputDate.setText(JSONObject(response).getString("day_of_metersdata"))
            api_key = JSONObject(response).getString("api_key")
            SharedPreferencesSingleton.write("day_of_metersdata", binding.textInputDate.text.toString())

            binding.buttonAdd.setOnClickListener {
                findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToSmartCounts())
            }

            binding.logout.setOnClickListener {
                SharedPreferencesSingleton.init(requireActivity())
                SharedPreferencesSingleton.write("token", "")
                NavDeepLinkBuilder(requireContext()).setComponentName(MainActivity::class.java)
                    .setGraph(R.navigation.graph_main).setDestination(R.id.authFragment).createPendingIntent().send()
            }
            binding.arrow.setOnClickListener {
                findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToProfileFragment())
            }
            binding.buttonSend.setOnClickListener {
                if (binding.textInputTelephone.text.length == 11 && binding.textInputDate.text.length == 2){
                    if(binding.textInputDate.text.toString().toInt() <= 31 && binding.textInputDate.text.toString().toInt()!= 0 ){
                        GlobalScope.launch(Dispatchers.IO) { post() }
                    }
                }else {
                    Toast.makeText(activity?.applicationContext, "некорректное значение", Toast.LENGTH_LONG).show()
                }
                }
            }
        }

    }

    private fun get(): String {
        try {
            val request = Request.Builder()
                .url("$url/water/editprofile/")
                .get()
                .addHeader("Authorization", "Token $token")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) { return "Exception" }
                val result = response.body!!.string()
                return result
            }
        } catch (e: Exception) { return "Exception" }
    }
    private fun post(): String {
        try {
            val formBody = FormBody.Builder()
                .add("fullname", binding.textInputFio.text.toString())
                .add("place", binding.textInputAddress.text.toString())
                .add("number_phone", binding.textInputTelephone.text.toString())
                .add("api_key", api_key)
                .add("day_of_metersdata", binding.textInputDate.text.toString())
                .build()

            val request = Request.Builder()
                .url("$url/water/editprofile/")
                .post(formBody)
                .addHeader("Authorization", "Token $token")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                val result = response.body!!.string()
                SharedPreferencesSingleton.init(requireActivity())
                SharedPreferencesSingleton.write("fullname", binding.textInputFio.text.toString())
                SharedPreferencesSingleton.write("place", binding.textInputAddress.text.toString())
                SharedPreferencesSingleton.write("number_phone", binding.textInputTelephone.text.toString())
                SharedPreferencesSingleton.write("day_of_metersdata", binding.textInputDate.text.toString())
                findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToProfileFragment())
                return result
            }
        } catch (e: Exception) { return "Exception" }
    }
}