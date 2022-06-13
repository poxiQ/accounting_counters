package glebova.rsue.countwater.ui.statistics

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import com.github.mikephil.charting.data.*
import glebova.rsue.countwater.R
import glebova.rsue.countwater.base.BaseFragment
import glebova.rsue.countwater.databinding.FragmentStatisticsBinding
import glebova.rsue.countwater.models.CountModel
import glebova.rsue.countwater.ui.response
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
class StatisticsFragment : BaseFragment<FragmentStatisticsBinding>() {

    private val client = OkHttpClient()

    override fun initializeBinding() = FragmentStatisticsBinding.inflate(layoutInflater)

    @SuppressLint("ResourceType")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val radioGroup: RadioGroup = binding.radioGroup
        sendPieChart("1")
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.month -> sendPieChart("1")
                R.id.three_month -> sendPieChart("6")
                R.id.year -> sendPieChart("12")
            }
        }
        response = ""
        GlobalScope.launch(Dispatchers.IO) { response = getBarChartValues() }
        while (response == "") { continue }
        setBarChartValues()

        response = ""
        GlobalScope.launch(Dispatchers.IO) { response = getCounters() }
        while (response == "") { continue }
        val counts = JSONObject(response).getJSONArray("counters")
        val radioGroup2 = binding.radioGroup2
        val counts_list: MutableList<CountModel> = ArrayList()
        for (i in 0 until counts.length()) {
            val radioButton1 = RadioButton(requireContext())
            radioButton1.layoutParams = LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
            radioButton1.text = counts.getJSONObject(i).getString("id_counter")
            radioButton1.id = i
            radioButton1.setTextColor(resources.getColor(R.color.black))
            if (radioButton1.id == 0) radioButton1.setChecked(true)
            radioGroup2.addView(radioButton1)
            counts_list.add(CountModel(radioButton1.id, radioButton1.text as String))
        }
        sendLineChartData(counts_list[0].title)
        radioGroup2.setOnCheckedChangeListener { group, checkedId ->
            for (i in counts_list){
                when (checkedId) {
                    i.id -> sendLineChartData(i.title)
                }
            }
        }
    }

    private fun sendLineChartData(str: String) {
        response = ""
        GlobalScope.launch(Dispatchers.IO) { response = getLineChartData(str) }
        while (response == "") { continue }
        setLineChartData()
    }

    private fun sendPieChart(str: String) {
        response = ""
        GlobalScope.launch(Dispatchers.IO) { response = getPieChart(str) }
        while (response == "") { continue }
        setPieChart()
    }
    private fun getCounters(): String {
        val request = Request.Builder()
            .url("$url/water/counters/")
            .get()
            .addHeader("Authorization", "Token $token")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            val result = response.body!!.string()
            return result
        }
    }

    private fun setPieChart() {
        val values = arrayOf<String>("Холодная", "Горячая")
        val piechartentry = ArrayList<Entry>()
        val piechart = binding.pieChart
        piechartentry.clear()
        piechart.invalidate()
        piechart.clear()

        piechartentry.add(Entry(JSONObject(response).getString("cold").toFloat(), 0))
        piechartentry.add(Entry(JSONObject(response).getString("hot").toFloat(), 1))

        val colors: ArrayList<Int> = ArrayList()
        colors.add(resources.getColor(R.color.cold))
        colors.add(resources.getColor(R.color.hot))

        val piedataset = PieDataSet(piechartentry, "")
        val data = PieData(values, piedataset)

        piedataset.colors = colors
        piedataset.sliceSpace = 3f
        data.setValueTextSize(15f)
        data.setValueTextColor(resources.getColor(R.color.black))
        piechart.data = data
        piechart.holeRadius = 5f
        piechart.setBackgroundColor(resources.getColor(R.color.white))
    }

    private fun setBarChartValues() {
        val values = arrayOf<String>(
            "Январь",
            "Февраль",
            "Март",
            "Апрель",
            "Май",
            "Июнь",
            "Июль",
            "Август",
            "Сентябрь",
            "Октябрь",
            "Ноябрь",
            "Декабрь"
        )
        val result = JSONObject(response).getJSONObject("result")
        val barentries = ArrayList<BarEntry>()
        var count = -1
        for (i in result.keys()){
            count+=1
            barentries.add(BarEntry(result.getString(i).toFloat(), count))
        }
        val bardataset = BarDataSet(barentries, "Общее потребление воды")
        bardataset.color = resources.getColor(R.color.lavand)
        val data = BarData(values, bardataset)
        val barChart = binding.barChart
        data.setValueTextSize(10f)
        barChart.data = data
        barChart.setBackgroundColor(resources.getColor(R.color.white))
        barChart.animateXY(3000, 3000)
    }

    private fun setLineChartData() {

        val xvalue = arrayOf<String>(
            "Январь",
            "Февраль",
            "Март",
            "Апрель",
            "Май",
            "Июнь",
            "Июль",
            "Август",
            "Сентябрь",
            "Октябрь",
            "Ноябрь",
            "Декабрь"
        )
        val result = JSONObject(response).getJSONObject("result")
        val lineentry = ArrayList<Entry>()
        var count = -1
        for (i in result.keys()){
            count+=1
            lineentry.add(Entry(result.getString(i).toFloat(), count))
        }

        val linedataset = LineDataSet(lineentry, "")
        linedataset.color = resources.getColor(R.color.purple_200)

        val data = LineData(xvalue, linedataset)
        val lineChart = binding.lineChart
        lineChart.data = data
        lineChart.setBackgroundColor(resources.getColor(R.color.white))
        lineChart.animateXY(3000, 3000)
    }


    private fun getPieChart(str: String): String {
        val request = Request.Builder()
            .url("$url/water/ciclediagrams/$str")
            .get()
            .addHeader("Authorization", "Token $token")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            val result = response.body!!.string()
            return result
        }
    }
    private fun getLineChartData(str: String): String {
        val request = Request.Builder()
            .url("$url/water/linediagrams/$str")
            .get()
            .addHeader("Authorization", "Token $token")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            val result = response.body!!.string()
            return result
        }
    }

    private fun getBarChartValues(): String {
        val request = Request.Builder()
            .url("$url/water/purplediagrams")
            .get()
            .addHeader("Authorization", "Token $token")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            val result = response.body!!.string()
            return result
        }
    }

}
