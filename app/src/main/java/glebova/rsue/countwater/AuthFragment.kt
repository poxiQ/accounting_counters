package glebova.rsue.countwater

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import glebova.rsue.countwater.base.BaseFragment
import glebova.rsue.countwater.databinding.FragmentAuthBinding
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
                        run(login, password)
                        }
                    findNavController().navigate(AuthFragmentDirections.actionAuthFragmentToBottomNavFragment2())
                }
            }
        }
    }

    private fun run(login:String, password:String) {
        val request = Request.Builder()
            .url("http://192.168.43.35:8080/water/login/?username=$login&password=$password")
            .get()
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful){
                throw IOException("Unexpected code $response")
            }
            val result = response.body!!.string()
            token = JSONObject(result).getString("token")
            Log.d("JSON", token)
        }
    }
}