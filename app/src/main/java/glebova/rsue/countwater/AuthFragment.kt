package glebova.rsue.countwater


import SharedPreferencesSingleton
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.fragment.findNavController
import glebova.rsue.countwater.base.BaseFragment
import glebova.rsue.countwater.databinding.FragmentAuthBinding
import glebova.rsue.countwater.ui.response
import glebova.rsue.countwater.ui.token
import glebova.rsue.countwater.ui.url
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException


@DelicateCoroutinesApi
class AuthFragment : BaseFragment<FragmentAuthBinding>() {

    private val client = OkHttpClient()

    override fun initializeBinding() = FragmentAuthBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.enter.setOnClickListener {
            val login = binding.editTextTextPersonName.text.toString()
            val password = binding.editTextTextPassword.text.toString()
            when {
                login.trim() == "" -> {
                    Toast.makeText(activity?.applicationContext, "заполните все поля", Toast.LENGTH_LONG).show()
                    binding.editTextTextPersonName.setBackgroundResource(R.drawable.et_style_error)
                    binding.editTextTextPassword.setBackgroundResource(R.drawable.et_style)
                }
                password.trim() == "" -> {
                    Toast.makeText(activity?.applicationContext, "заполните все поля", Toast.LENGTH_LONG).show()
                    binding.editTextTextPassword.setBackgroundResource(R.drawable.et_style_error)
                    binding.editTextTextPersonName.setBackgroundResource(R.drawable.et_style)
                }
                else -> {
                    response = ""
                    GlobalScope.launch(Dispatchers.IO) { response = get_auth(login, password) }
                    while (response == "") { continue }
                    Log.d("fjireofkerf", response)
                    token = SharedPreferencesSingleton.read("token", "").toString()
                    when (response) {
                        "Exception" -> {
                            findNavController().navigate(AuthFragmentDirections.actionAuthFragmentSelf())
                            Toast.makeText(activity?.applicationContext, "неправильный логин или пароль, повторите вход", Toast.LENGTH_LONG).show()
                        }
                        "Ex" -> {
                            findNavController().navigate(AuthFragmentDirections.actionAuthFragmentSelf())
                            Toast.makeText(activity?.applicationContext, "проверьте подключение к сети и повторите вход", Toast.LENGTH_LONG).show()
                        }
                        else -> {
                            response = ""
                            GlobalScope.launch(Dispatchers.IO) { response = get_reset() }
                            while (response == "") { continue }
                            if (JSONObject(response).getString("defaultpassword") == "True") {
                                findNavController().navigate(AuthFragmentDirections.actionAuthFragmentToGraphNewLogin())
                            } else {
                                NavDeepLinkBuilder(requireContext()).setComponentName(MainActivity::class.java).setGraph(R.navigation.graph_main).setDestination(R.id.bottomNavFragment).createPendingIntent().send()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun get_auth(login: String, password: String): String {
        try {
            val request = Request.Builder()
                .url("$url/water/login/?username=$login&password=$password")
                .get()
                .build()
            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) { return "Exception" }
                val result = response.body!!.string()
                SharedPreferencesSingleton.init(requireActivity())
                SharedPreferencesSingleton.write("token", JSONObject(result).getString("token"))
                SharedPreferencesSingleton.write("fullname", JSONObject(result).getString("fullname"))
                SharedPreferencesSingleton.write("place", JSONObject(result).getString("place"))
                SharedPreferencesSingleton.write("number_phone", JSONObject(result).getString("number_phone"))
                return result
            }
        } catch (e: Exception) { return "Ex" }
    }


    private fun get_reset(): String {
        val request = Request.Builder()
            .url("$url/water/checkdefaultpassword/")
            .get()
            .addHeader("Authorization", "Token $token")
            .build()
        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            val result = response.body!!.string()
            return result
        }
    }
}