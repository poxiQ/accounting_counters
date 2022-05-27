package glebova.rsue.countwater

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import glebova.rsue.countwater.base.BaseFragment
import glebova.rsue.countwater.databinding.FragmentNewLoginBinding
import glebova.rsue.countwater.databinding.FragmentSplashBinding
import glebova.rsue.countwater.ui.master.response
import glebova.rsue.countwater.ui.splash.SplashFragmentDirections
import glebova.rsue.countwater.ui.splash.sPref
import glebova.rsue.countwater.ui.splash.token
import glebova.rsue.countwater.ui.splash.url
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException

@DelicateCoroutinesApi
class NewLogin : BaseFragment<FragmentNewLoginBinding>() {

    private val client = OkHttpClient()

    override fun initializeBinding() = FragmentNewLoginBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.enter.setOnClickListener {
            val password = binding.editTextTextPassword.text.toString()
            when {
                password.trim() == "" -> {
                    Toast.makeText(activity?.applicationContext, "заполните поле", Toast.LENGTH_LONG).show()
                }
                else -> {
                    GlobalScope.launch(Dispatchers.IO) {
                        response = post(password)
                    }
                    while (response == "") {
                        continue
                    }
                    Toast.makeText(activity?.applicationContext, "Пароль успешно изменен", Toast.LENGTH_LONG).show()
                    token = sPref!!.getString("token", "").toString()
                    findNavController().navigate(NewLoginDirections.actionNewLoginToBottomNavFragment3())
                }
            }
        }
    }

    private fun post(password: String): String {
        val request = Request.Builder()
//            .url("http://192.168.43.35:8080/water/login/?username=$login&password=$password"  )
            .url("$url/water/resetpassword/?newpassword=$password")
            .get()
            .addHeader("Authorization", "Token $token")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) {
                throw IOException("Unexpected code $response")
            }
            val result = response.body!!.string()
            sPref = activity?.getSharedPreferences("MyPref", Context.MODE_PRIVATE)
            val ed: SharedPreferences.Editor = sPref!!.edit()
            ed.putString("token", JSONObject(result).getString("token")).apply()
            return result
        }
    }
}