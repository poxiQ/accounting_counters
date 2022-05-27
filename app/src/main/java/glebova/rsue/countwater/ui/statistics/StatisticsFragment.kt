package glebova.rsue.countwater.ui.statistics

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioGroup
import com.github.mikephil.charting.data.*
import glebova.rsue.countwater.R
import glebova.rsue.countwater.base.BaseFragment
import glebova.rsue.countwater.databinding.FragmentStatisticsBinding
import glebova.rsue.countwater.ui.master.response
import glebova.rsue.countwater.ui.splash.token
import glebova.rsue.countwater.ui.splash.url
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val radioGroup: RadioGroup = binding.radioGroup
        send("1")
        radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.month -> send("1")
                R.id.three_month -> send("6")
                R.id.year -> send("12")
            }
        }
        response = ""
        GlobalScope.launch(Dispatchers.IO) { response = getBarChartValues() }
        while (response == "") {
            continue
        }
        setBarChartValues()
        setLineChartData()
    }

    private fun send(str: String){
        response = ""
        GlobalScope.launch(Dispatchers.IO) { response = getPieChart(str) }
        while (response == "") { continue }
        setPieChart()
    }

    private fun setPieChart() {
        val values = arrayOf<String>("Холодная", "Горячая")
        val piechartentry = ArrayList<Entry>()
        val piechart = binding.piechart
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

        barentries.add(BarEntry(result.getString("Январь").toFloat(), 0))
        barentries.add(BarEntry(result.getString("Февраль").toFloat(), 1))
        barentries.add(BarEntry(result.getString("Март").toFloat(), 2))
        barentries.add(BarEntry(result.getString("Апрель").toFloat(), 3))
        barentries.add(BarEntry(result.getString("Май").toFloat(), 4))
        barentries.add(BarEntry(result.getString("Июнь").toFloat(), 5))
        barentries.add(BarEntry(result.getString("Июль").toFloat(), 6))
        barentries.add(BarEntry(result.getString("Август").toFloat(), 7))
        barentries.add(BarEntry(result.getString("Сентябрь").toFloat(), 8))
        barentries.add(BarEntry(result.getString("Октябрь").toFloat(), 9))
        barentries.add(BarEntry(result.getString("Ноябрь").toFloat(), 10))
        barentries.add(BarEntry(result.getString("Декабрь").toFloat(), 11))

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

        val lineentry = ArrayList<Entry>();
        lineentry.add(Entry(20f, 0))
        lineentry.add(Entry(50f, 1))
        lineentry.add(Entry(60f, 2))
        lineentry.add(Entry(30f, 3))
        lineentry.add(Entry(10f, 4))

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
//            .url("http://192.168.43.35:8080/water/counters")
            .url("$url/water/ciclediagrams/$str")
            .get()
            .addHeader("Authorization", "Token $token")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            val result = response.body!!.string()
            Log.d("JSON", result)
            return result
        }
    }

    private fun getBarChartValues(): String {
        val request = Request.Builder()
//            .url("http://192.168.43.35:8080/water/counters")
            .url("$url/water/purplediagrams")
            .get()
            .addHeader("Authorization", "Token $token")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            val result = response.body!!.string()
            Log.d("JSON", result)
            return result
        }
    }

}
