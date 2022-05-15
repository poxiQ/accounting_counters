package glebova.rsue.countwater

import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import glebova.rsue.countwater.base.BaseFragment
import glebova.rsue.countwater.databinding.FragmentAuthBinding
import glebova.rsue.countwater.ui.master.response
import glebova.rsue.countwater.ui.splash.sPref
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
                    Toast.makeText(activity?.applicationContext, "заполните поле", Toast.LENGTH_LONG).show()
                }
                password.trim() == "" -> {
                    Toast.makeText(activity?.applicationContext, "заполните поле", Toast.LENGTH_LONG).show()
                }
                else -> {
                    GlobalScope.launch(Dispatchers.IO) {
                        response = run(login, password)
                    }
                    while (response == "") {
                        continue
                    }
                    if (response == "Exception") {
                        findNavController().navigate(AuthFragmentDirections.actionAuthFragmentSelf())
                        Toast.makeText(activity?.applicationContext, "Неправильный логин или пароль, повторите вход", Toast.LENGTH_LONG).show()
                    } else {
                        if (sPref!!.getInt("start", 1) == 1) {
                            findNavController().navigate(AuthFragmentDirections.actionAuthFragmentToGraphNewLogin())
                        } else {
                            findNavController().navigate(AuthFragmentDirections.actionAuthFragmentToBottomNavFragment2())
                        }
                    }
                }
            }
        }
    }

    private fun run(login: String, password: String): String {
        val request = Request.Builder()
            .url("https://24b6-178-76-226-214.eu.ngrok.io/water/login/?username=$login&password=$password")
            .get()
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                return "Exception"
            }
            val result = response.body!!.string()
            sPref = activity?.getSharedPreferences("MyPref", MODE_PRIVATE)
            val ed: SharedPreferences.Editor = sPref!!.edit()
            ed.putString("token", JSONObject(result).getString("token"))
            ed.apply()
            return result
        }
    }
}