package glebova.rsue.countwater.ui.count

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import glebova.rsue.countwater.AuthFragmentDirections
import glebova.rsue.countwater.R
import glebova.rsue.countwater.adapters.CountAdapter
import glebova.rsue.countwater.base.BaseFragment
import glebova.rsue.countwater.databinding.FragmentCountBinding
import glebova.rsue.countwater.models.CountModel
import glebova.rsue.countwater.ui.response
import glebova.rsue.countwater.ui.splash.SplashFragmentDirections
import glebova.rsue.countwater.ui.token
import glebova.rsue.countwater.ui.url
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
        response = ""
        binding.name = SharedPreferencesSingleton.read("fullname", "")
        if (counts_list_hot.count() == 0) {
            GlobalScope.launch(Dispatchers.IO) { response = get() }
            while (response == "") { continue }
            Log.d("___________", response)
            when (response) {
                "Exception" -> {
                    Toast.makeText(activity?.applicationContext, "проверьте подключение к сети", Toast.LENGTH_LONG).show()
                    findNavController().navigate(CountFragmentDirections.actionCountFragmentToSplashFragment2())
                }
                else -> initRecyclerView()
            }
        } else {
            counts_list_hot.clear()
            counts_list_cold.clear()
            GlobalScope.launch(Dispatchers.IO) { response = get() }
            initRecyclerView()
        }
    }

    private fun buildDisplayData(response: String) {
        val counts = JSONObject(response).getJSONArray("counters")

        for (i in 0 until counts.length()) {
            when {
                counts.getJSONObject(i).getString("typewater") == "1" -> {
                    if (counts.getJSONObject(i).getString("isclever") == "true"){
                        counts_list_hot.add(CountModel(1, counts.getJSONObject(i).getString("id_counter"), true))
                    }else counts_list_hot.add(CountModel(1, counts.getJSONObject(i).getString("id_counter"), false))
                }
                counts.getJSONObject(i).getString("typewater") == "0" -> {
                    if (counts.getJSONObject(i).getString("isclever") == "true"){
                        counts_list_cold.add(CountModel(0, counts.getJSONObject(i).getString("id_counter"), true))
                    }else counts_list_cold.add(CountModel(0, counts.getJSONObject(i).getString("id_counter"), false))
                }
            }
        }
    }

    private fun get(): String {
        try {
            val request = Request.Builder()
                .url("$url/water/counters/")
                .get()
                .addHeader("Authorization", "Token $token")
                .build()

            client.newCall(request).execute().use { response ->
                if (!response.isSuccessful) { return "Exception" }
                val result = response.body!!.string()
                buildDisplayData(result)
                return result
            }
        }catch (e: Exception) { return "Exception" }
    }

    private fun initRecyclerView() {
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
        with(bundle) { putString("count", count) }
        findNavController().navigate(R.id.action_countFragment_to_blankFragment, bundle)
    }
}