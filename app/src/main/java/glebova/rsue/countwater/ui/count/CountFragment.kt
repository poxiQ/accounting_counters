package glebova.rsue.countwater.ui.count

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.navigation.fragment.findNavController
import glebova.rsue.countwater.R
import glebova.rsue.countwater.adapters.CountAdapter
import glebova.rsue.countwater.base.BaseFragment
import glebova.rsue.countwater.databinding.FragmentCountBinding
import glebova.rsue.countwater.models.CountModel
import glebova.rsue.countwater.ui.master.response
import glebova.rsue.countwater.ui.splash.sPref
import glebova.rsue.countwater.ui.splash.token
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.io.IOException

@DelicateCoroutinesApi
class CountFragment : BaseFragment<FragmentCountBinding>() {

    override fun initializeBinding() = FragmentCountBinding.inflate(layoutInflater)

    private lateinit var adapter: CountAdapter
    private val client = OkHttpClient()
    val counts_list_hot: MutableList<CountModel> = ArrayList()
    val counts_list_cold: MutableList<CountModel> = ArrayList()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.name = sPref!!.getString("fullname", "").toString()
        if (counts_list_hot.count() == 0) {
            GlobalScope.launch(Dispatchers.IO) {
                response = get()
            }
            initRecyclerView()
        } else {
            counts_list_hot.clear()
            counts_list_cold.clear()
            GlobalScope.launch(Dispatchers.IO) {
                response = get()
            }
            initRecyclerView()
        }
    }

    private fun buildDisplayData(response: String) {
        val counts = JSONObject(response).getJSONArray("counters")

        for (i in 0 until counts.length()) {
            when {
                counts.getJSONObject(i).getString("typewater") == "1" -> {
                    counts_list_hot.add(CountModel(1, counts.getJSONObject(i).getString("id_counter")))
                }
                counts.getJSONObject(i).getString("typewater") == "0" -> {
                    counts_list_cold.add(CountModel(0, counts.getJSONObject(i).getString("id_counter")))
                }
            }
        }
    }

    private fun get(): String {
        val request = Request.Builder()
//            .url("http://192.168.43.35:8080/water/counters")
            .url("https://6c72-178-76-226-214.eu.ngrok.io/water/counters/")
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
        CountAdapter(counts_list_hot) { onButtonCLicked(it) }.let {
            binding.countsRecyclerHot.adapter = it
            adapter = it
        }
        CountAdapter(counts_list_cold) { onButtonCLicked(it) }.let {
            adapter = it
            binding.countsRecyclerCold.adapter = it
        }
        response = ""
    }

    private fun onButtonCLicked(count: String) {
        val bundle = Bundle()
        with(bundle) {
            putString("count", count)
        }
        findNavController().navigate(R.id.action_countFragment_to_blankFragment, bundle)
    }
}