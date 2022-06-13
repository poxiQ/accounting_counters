package glebova.rsue.countwater

import android.os.Bundle
import android.view.View
import android.widget.RadioGroup
import androidx.navigation.fragment.findNavController
import glebova.rsue.countwater.base.BaseFragment
import glebova.rsue.countwater.databinding.FragmentRequestBinding
import glebova.rsue.countwater.ui.token
import glebova.rsue.countwater.ui.url
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


@DelicateCoroutinesApi
class RequestFragment : BaseFragment<FragmentRequestBinding>() {

    private val client = OkHttpClient()
    private lateinit var service: String

    override fun initializeBinding() = FragmentRequestBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val radioGroup: RadioGroup = binding.radioGroup
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.master1 -> service= "1"
                R.id.master2 -> service= "2"
                R.id.master3 -> service= "3"
            }
        }

        binding.arrow.setOnClickListener {
            findNavController().navigate(RequestFragmentDirections.actionRequestFragmentToMasterFragment())
        }
        binding.buttonSend.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) { run() }
            findNavController().navigate(RequestFragmentDirections.actionRequestFragmentToMasterFragment())
        }
    }


    private fun run() {
        val formBody = FormBody.Builder()
            .add("description", binding.textInput.text.toString())
            .add("service", service)
            .add("DateTime", SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date()))
            .build()
        val request = Request.Builder()
            .url("$url/water/service/")
            .post(formBody)
            .addHeader("Authorization", "Token $token")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
        }
    }
}
