package glebova.rsue.countwater

import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.widget.RadioGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import glebova.rsue.countwater.base.BaseFragment
import glebova.rsue.countwater.databinding.FragmentSmartCountsBinding
import glebova.rsue.countwater.ui.SettingsFragmentDirections
import glebova.rsue.countwater.ui.response
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

@DelicateCoroutinesApi
class SmartCounts : BaseFragment<FragmentSmartCountsBinding>() {
    private val client = OkHttpClient()
    var isclever = "False"
    var typewater = 0
    var id_registrator = ""

    override fun initializeBinding() = FragmentSmartCountsBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val radioGroup: RadioGroup = binding.radioGroup
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.hot -> typewater = 1
                R.id.cold -> typewater = 0
            }
        }

        val radioGroup2: RadioGroup = binding.radioGroup2
        radioGroup2.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.notsmart -> {
                    isclever = "False"
                    binding.idModem.visibility = INVISIBLE
                    binding.idRegistr.visibility = INVISIBLE
                }
                R.id.smart -> {
                    isclever = "True"
                    binding.idModem.visibility = VISIBLE
                    binding.idRegistr.visibility = VISIBLE
                }
            }
        }
        binding.arrow.setOnClickListener {
            findNavController().navigate(SmartCountsDirections.actionSmartCountsToSettingsFragment())
        }
        binding.buttonAdd.setOnClickListener {
            when {
                binding.idCount.text.toString().trim() == "" -> {
                    Toast.makeText(activity?.applicationContext, "заполните все поля", Toast.LENGTH_LONG).show()
                    binding.idCount.setBackgroundResource(R.drawable.et_style_error)
                    binding.idCount.setBackgroundResource(R.drawable.et_style)
                }
                isclever == "True" -> {
                    when {
                        binding.idModem.text.toString().trim() == "" -> {
                            Toast.makeText(activity?.applicationContext, "заполните все поля", Toast.LENGTH_LONG).show()
                            binding.idModem.setBackgroundResource(R.drawable.et_style_error)
                            binding.idModem.setBackgroundResource(R.drawable.et_style)
                        }
                        binding.idRegistr.text.toString().trim() == "" -> {
                            Toast.makeText(activity?.applicationContext, "заполните все поля", Toast.LENGTH_LONG).show()
                            binding.idRegistr.setBackgroundResource(R.drawable.et_style_error)
                            binding.idRegistr.setBackgroundResource(R.drawable.et_style)
                        }else -> {GlobalScope.launch(Dispatchers.IO) { response = send() }}
                    }
                }else -> {GlobalScope.launch(Dispatchers.IO) { response = send() }}
            }
            while (response == "") { continue }
            findNavController().navigate(SmartCountsDirections.actionSmartCountsToSettingsFragment())
        }
    }

    private fun send(): String {

            val formBody = FormBody.Builder()
                .add("id_counter", binding.idCount.text.toString())
                .add("typewater", typewater.toString())
                .add("isclever", isclever.toString())
                .add("id_modem", binding.idModem.text.toString())
                .add("id_registrator", binding.idRegistr.text.toString())
                .build()

            val request = Request.Builder()
                .url("$url/water/counters/")
                .post(formBody)
                .addHeader("Authorization", "Token $token")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) throw IOException("Unexpected code $response")
                val result = response.body!!.string()
                Log.d("___________", result)
                return result
            }

    }
}