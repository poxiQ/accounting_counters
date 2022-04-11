package glebova.rsue.countwater

import android.R
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.navigation.fragment.findNavController
import glebova.rsue.countwater.base.BaseFragment
import glebova.rsue.countwater.databinding.FragmentRequestBinding
import glebova.rsue.countwater.ui.splash.token
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
import kotlin.collections.ArrayList


@DelicateCoroutinesApi
class RequestFragment : BaseFragment<FragmentRequestBinding>() {

    private val client = OkHttpClient()
    private lateinit var service: String

    override fun initializeBinding() = FragmentRequestBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var data = ArrayList<String>()
        data.add("Сантехник")
        data.add("Электрик")
        data.add("Муж на час")
        val convert_from_spinner: Spinner = binding.spinner
        convert_from_spinner.adapter =
            activity?.let { ArrayAdapter<String>(it, android.R.layout.simple_list_item_1, data) }

        convert_from_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
                val num = when (convert_from_spinner.selectedItem.toString()) {
                    "Сантехник" -> service = "1"
                    "Электрик" -> service = "2"
                    "Муж на час" -> service = "3"
                    else -> service = "1"
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }


        binding.buttonSend.setOnClickListener {
            GlobalScope.launch(Dispatchers.IO) {
                run()
            }
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
            .url("http://192.168.43.35:8080/water/service/")
            .post(formBody)
            .addHeader("Authorization", "Token $token")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            Log.d("JSON", response.body!!.string())
        }
    }
}
