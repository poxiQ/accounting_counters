package glebova.rsue.countwater.ui

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.navigation.NavDeepLinkBuilder
import androidx.navigation.fragment.findNavController
import glebova.rsue.countwater.MainActivity
import glebova.rsue.countwater.R
import glebova.rsue.countwater.adapters.SmartCountAdapter
import glebova.rsue.countwater.adapters.counts
import glebova.rsue.countwater.base.BaseFragment
import glebova.rsue.countwater.databinding.FragmentSettingsBinding
import glebova.rsue.countwater.models.CountModel
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException


@DelicateCoroutinesApi
class SettingsFragment : BaseFragment<FragmentSettingsBinding>() {

    private val client = OkHttpClient()
    private lateinit var adapter: SmartCountAdapter
    val lists: MutableList<Int> = ArrayList()
    override fun initializeBinding() = FragmentSettingsBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textInputFio.setText(SharedPreferencesSingleton.read("fullname", "").toString())
        binding.textInputAddress.setText(SharedPreferencesSingleton.read("place", "").toString())
        binding.textInputTelephone.setText(SharedPreferencesSingleton.read("number_phone", "").toString())

        binding.checkBox.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                binding.scroll2.visibility = View.VISIBLE
                binding.buttonAdd.visibility = View.VISIBLE
                if (lists.size == 0){ lists.add(1) }
                initRecyclerView()
                binding.buttonAdd.setOnClickListener {
                    lists.add(1)
                    initRecyclerView()
                }
            }else{
                binding.scroll2.visibility = View.INVISIBLE
                binding.buttonAdd.visibility = View.INVISIBLE
            }
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
            if (binding.checkBox.isChecked){ GlobalScope.launch(Dispatchers.IO) { sendSmart() }}
            else {GlobalScope.launch(Dispatchers.IO) { send() }}
        }
    }

    private fun send() {
        val formBody = FormBody.Builder()
            .add("username", binding.textInputFio.text.toString())
            .add("address", binding.textInputAddress.text.toString())
            .add("telephone", binding.textInputTelephone.text.toString())
            .add("smart_count", "False")
            .build()
        val request = Request.Builder()
            .url("$url/water/service/")
            .put(formBody)
            .addHeader("Authorization", "Token $token")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToProfileFragment())
        }
    }
    private fun sendSmart() {
        val formBody = FormBody.Builder()
            .add("username", binding.textInputFio.text.toString())
            .add("address", binding.textInputAddress.text.toString())
            .add("telephone", binding.textInputTelephone.text.toString())
            .add("smart_count", counts.toString())
            .build()
        val request = Request.Builder()
            .url("$url/water/service/")
            .put(formBody)
            .addHeader("Authorization", "Token $token")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            findNavController().navigate(SettingsFragmentDirections.actionSettingsFragmentToProfileFragment())
        }
    }
    private fun initRecyclerView() {
        SmartCountAdapter(lists).let {
            binding.countsRecycler.adapter = it
            adapter = it
        }
    }
}