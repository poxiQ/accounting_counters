package glebova.rsue.countwater

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import glebova.rsue.countwater.base.BaseFragment
import glebova.rsue.countwater.databinding.FragmentRequestBinding
import glebova.rsue.countwater.models.CountModel
import glebova.rsue.countwater.ui.splash.token
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException

@DelicateCoroutinesApi
class RequestFragment : BaseFragment<FragmentRequestBinding>() {

    private val client = OkHttpClient()

    override fun initializeBinding() = FragmentRequestBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonSend.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
//                run()
            }
            findNavController().navigate(RequestFragmentDirections.actionRequestFragmentToMasterFragment())
        }
    }


    private fun run() {
        val formBody = FormBody.Builder()
            .add("description", binding.textInput.text.toString())
            .build()
        val request = Request.Builder()
            .url("http://192.168.43.35:8080/water/service")
            .post(formBody)
            .addHeader("Authorization", "Token $token")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            Log.d("JSON", response.body!!.string())
        }
    }
}
