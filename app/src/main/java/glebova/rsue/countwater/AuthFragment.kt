package glebova.rsue.countwater

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.fragment.findNavController
import glebova.rsue.countwater.base.BaseFragment
import glebova.rsue.countwater.databinding.FragmentAuthBinding
import glebova.rsue.countwater.ui.response
import glebova.rsue.countwater.ui.sPref
import glebova.rsue.countwater.ui.token
import glebova.rsue.countwater.ui.url
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject


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
                    Toast.makeText(activity?.applicationContext, "заполните поле", Toast.LENGTH_LONG).show()
                    binding.editTextTextPersonName.setBackgroundResource(R.drawable.et_style_error)
                    binding.editTextTextPassword.setBackgroundResource(R.drawable.et_style)
                }
                password.trim() == "" -> {
                    Toast.makeText(activity?.applicationContext, "заполните поле", Toast.LENGTH_LONG).show()
                    binding.editTextTextPassword.setBackgroundResource(R.drawable.et_style_error)
                    binding.editTextTextPersonName.setBackgroundResource(R.drawable.et_style)
                }
                else -> {
                    GlobalScope.launch(Dispatchers.IO) {
                        response = get_auth(login, password)
                    }
                    while (response == "") { continue }
                    token = sPref!!.getString("token", "").toString()
                    if (response == "Exception") {
                        findNavController().navigate(AuthFragmentDirections.actionAuthFragmentSelf())
                        Toast.makeText(activity?.applicationContext, "Неправильный логин или пароль, повторите вход", Toast.LENGTH_LONG).show()
                    } else {
                        response = ""
                        GlobalScope.launch(Dispatchers.IO) {
                            response = get_reset()
                        }
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

    private fun get_auth(login: String, password: String): String {
        val request = Request.Builder()
            .url("$url/water/login/?username=$login&password=$password")
            .get()
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                return "Exception"
            }
            val result = response.body!!.string()
            Log.d("JSON", result)
            sPref = activity?.getSharedPreferences("MyPref", MODE_PRIVATE)
            val ed: SharedPreferences.Editor = sPref!!.edit()
            ed.putString("token", JSONObject(result).getString("token")).apply()
            ed.putString("fullname", JSONObject(result).getString("fullname")).apply()
            ed.putString("place", JSONObject(result).getString("place")).apply()
            ed.putString("number_phone", JSONObject(result).getString("number_phone")).apply()
            return result
        }
    }

    private fun get_reset(): String {
        val request = Request.Builder()
            .url("$url/water/checkdefaultpassword/")
            .get()
            .addHeader("Authorization", "Token $token")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                return "Exception"
            }
            val result = response.body!!.string()
            Log.d("JSON", result)
            return result
        }
    }
}