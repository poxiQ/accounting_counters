package glebova.rsue.countwater.ui.master

import android.os.Bundle
import android.view.View
import androidx.navigation.fragment.findNavController
import glebova.rsue.countwater.adapters.MasterAdapter
import glebova.rsue.countwater.base.BaseFragment
import glebova.rsue.countwater.databinding.FragmentMasterBinding
import glebova.rsue.countwater.models.MasterModel
import glebova.rsue.countwater.ui.splash.token
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException
import java.util.*


class MasterFragment : BaseFragment<FragmentMasterBinding>() {

    override fun initializeBinding() = FragmentMasterBinding.inflate(layoutInflater)

    private lateinit var adapter: MasterAdapter
    val lists: MutableList<MasterModel> = ArrayList()
    val client = OkHttpClient()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (lists.count() == 0) {
//            run()
        }
        initRecyclerView()
        binding.request.setOnClickListener {
            findNavController().navigate(MasterFragmentDirections.actionMasterFragmentToRequestFragment())
        }
    }

    private fun run() {
        val request = Request.Builder()
            .url("http://192.168.1.203:8080/masterlists/api/getzaiavki?token=$token")
            .get()
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")

            println(response.body!!.string())
        }
    }

    private fun initRecyclerView() {
        MasterAdapter(lists).let {
            binding.masterRecycler.adapter = it
            adapter = it
        }
    }
}
