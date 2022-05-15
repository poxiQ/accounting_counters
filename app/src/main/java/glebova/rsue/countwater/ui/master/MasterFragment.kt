package glebova.rsue.countwater.ui.master

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import glebova.rsue.countwater.adapters.MasterAdapter
import glebova.rsue.countwater.base.BaseFragment
import glebova.rsue.countwater.databinding.FragmentMasterBinding
import glebova.rsue.countwater.models.MasterModel
import glebova.rsue.countwater.ui.splash.token
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException

var response = ""

@DelicateCoroutinesApi
class MasterFragment : BaseFragment<FragmentMasterBinding>() {

    override fun initializeBinding() = FragmentMasterBinding.inflate(layoutInflater)

    private lateinit var adapter: MasterAdapter
    private val lists: MutableList<MasterModel> = ArrayList()
    private val client = OkHttpClient()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (lists.count() == 0) {
            GlobalScope.launch(Dispatchers.IO) {
                response = get()
            }
            initRecyclerView()
        }else {
            lists.clear()
            GlobalScope.launch(Dispatchers.IO) {
                response = get()
            }
            initRecyclerView()
        }
        binding.request.setOnClickListener {
            findNavController().navigate(MasterFragmentDirections.actionMasterFragmentToRequestFragment())
        }
    }

    private fun buildDisplayData(response: String) {
        val hell = JSONObject(response).getJSONArray("hell")

        for (i in 0 until hell.length()) {
            when {
                hell.getJSONObject(i).getString("service") == "1" -> {
                    lists.add(
                        MasterModel(
                            "сантехник",
                            hell.getJSONObject(i).getString("DateTime").substring(0, 10)
                        )
                    )
                }
                hell.getJSONObject(i).getString("service") == "2" -> {
                    lists.add(
                        MasterModel(
                            "электрик",
                            hell.getJSONObject(i).getString("DateTime").substring(0, 10)
                        )
                    )
                }
                hell.getJSONObject(i).getString("service") == "3" -> {
                    lists.add(
                        MasterModel(
                            "муж на час",
                            hell.getJSONObject(i).getString("DateTime").substring(0, 10)
                        )
                    )
                }
            }
        }
        Log.d("-------------------", lists.toString())
    }

    private fun get(): String {
        val request = Request.Builder()
//            .url("http://192.168.43.35:8080/water/service/")
            .url("https://24b6-178-76-226-214.eu.ngrok.io/water/service/")
            .get()
            .addHeader("Authorization", "Token $token")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            val result = response.body!!.string()
            Log.d("JSON", result)
            buildDisplayData(result)
            return result
        }

    }

    private fun initRecyclerView() {
        while (response == "") {
            continue
        }
        MasterAdapter(lists).let {
            binding.masterRecycler.adapter = it
            adapter = it
        }
        response = ""
    }
}
