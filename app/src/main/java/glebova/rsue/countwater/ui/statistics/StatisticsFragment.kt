package glebova.rsue.countwater.ui.statistics

import android.graphics.Color
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.View
import com.github.mikephil.charting.charts.ScatterChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.interfaces.datasets.IScatterDataSet
import glebova.rsue.countwater.R
import glebova.rsue.countwater.base.BaseFragment
import glebova.rsue.countwater.databinding.FragmentStatisticsBinding
import glebova.rsue.countwater.ui.splash.token
import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.IOException


class StatisticsFragment : BaseFragment<FragmentStatisticsBinding>() {

    private val client = OkHttpClient()

    override fun initializeBinding() = FragmentStatisticsBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        val data = arrayOf<String>("За три месяца", "За шесть месяцев", "За год")
//        var a = ""
//        val convert_from_spinner: Spinner = binding.spinner
//        convert_from_spinner.adapter =
//            activity?.let { ArrayAdapter<String>(it, android.R.layout.simple_list_item_1, data) }
//
//        convert_from_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>, view: View, position: Int, id: Long) {
//                val num = when (convert_from_spinner.selectedItem.toString()) {
//                    "За три месяца" -> a = "1"
//                    "За шесть месяцев" -> a = "2"
//                    "За год" -> a = "3"
//                    else -> a = "1"
//                }
//            }
//
//            override fun onNothingSelected(p0: AdapterView<*>?) {
//                TODO("Not yet implemented")
//            }
//        }
        setCombinedChart()
        setPieChart()
        scatterChartData()
        setBarChartValues()
        setLineChartData()
        setCandleStickChart()
    }

    private fun setPieChart() {
        val xvalues = ArrayList<String>()
        xvalues.add("Coal")
        xvalues.add("Petrolium")
        xvalues.add("Natural Gas")
        xvalues.add("Renewable Energy")
        xvalues.add("Nuclear")

        val piechartentry = ArrayList<Entry>()
        piechartentry.add(Entry(23.5f, 0))
        piechartentry.add(Entry(45.5f, 1))
        piechartentry.add(Entry(68.5f, 2))

        val piedataset = PieDataSet(piechartentry, " Consumption")
        val data = PieData(xvalues, piedataset)
        val piechart = binding.piechart
        piedataset.color = resources.getColor(R.color.red)
        piedataset.sliceSpace = 3f
        piechart.data = data
        piechart.holeRadius = 5f
        piechart.setBackgroundColor(resources.getColor(R.color.grey))
    }

    private fun scatterChartData() {
        val scatterentry = ArrayList<Entry>()

        scatterentry.add(Entry(20f, 0))
        scatterentry.add(Entry(50f, 1))
        scatterentry.add(Entry(70f, 2))
        scatterentry.add(Entry(10f, 3))
        scatterentry.add(Entry(30f, 4))

        val scatterentry1 = ArrayList<Entry>()

        scatterentry1.add(Entry(10f, 0))
        scatterentry1.add(Entry(40f, 1))
        scatterentry1.add(Entry(20f, 2))
        scatterentry1.add(Entry(70f, 3))
        scatterentry1.add(Entry(50f, 4))

        val xval = ArrayList<String>()
        xval.add("11:00 am")
        xval.add("12:00 am")
        xval.add("3:00 pm")
        xval.add("6:00 pm")
        xval.add("8:00 pm")

        val scatterDataSet = ScatterDataSet(scatterentry, "first")
        scatterDataSet.color = Color.parseColor("#152368")
        scatterDataSet.scatterShape = ScatterChart.ScatterShape.TRIANGLE

        val scatterDataSet1 = ScatterDataSet(scatterentry1, "second")
        scatterDataSet1.scatterShape = ScatterChart.ScatterShape.CIRCLE

        val scatterlistfinal = ArrayList<ScatterDataSet>()
        scatterlistfinal.add(scatterDataSet)
        scatterlistfinal.add(scatterDataSet1)

        val scatterData = ScatterData(xval, scatterlistfinal as List<IScatterDataSet>?)
        val scatterchart = binding.scatterchart
        scatterchart.data = scatterData
        scatterchart.setBackgroundColor(resources.getColor(R.color.white))
        scatterchart.animateXY(3000, 3000)

        val xaxis = scatterchart.xAxis
        xaxis.position = XAxis.XAxisPosition.BOTTOM
        xaxis.setDrawGridLines(false)
    }

    private fun setBarChartValues() {
        val xvalues = ArrayList<String>()
        xvalues.add("Monday")
        xvalues.add("Tuesday")
        xvalues.add("Wednesday")
        xvalues.add("Thursday")
        xvalues.add("Friday")
        xvalues.add("Saturday")
        xvalues.add("Sunday")

        val barentries = ArrayList<BarEntry>()

        barentries.add(BarEntry(4f, 0))
        barentries.add(BarEntry(3.5f, 1))
        barentries.add(BarEntry(8.2f, 2))
        barentries.add(BarEntry(5.6f, 3))
        barentries.add(BarEntry(2f, 4))
        barentries.add(BarEntry(6f, 5))
        barentries.add(BarEntry(9f, 6))

        val bardataset = BarDataSet(barentries, "First")
        bardataset.color = resources.getColor(R.color.lavand)
        val data = BarData(xvalues, bardataset)
        val barChart = binding.barChart
        barChart.data = data
        barChart.setBackgroundColor(resources.getColor(R.color.grey))
        barChart.animateXY(3000, 3000)
    }

    private fun setCombinedChart() {
        val xvalue = ArrayList<String>()
        xvalue.add("11.00 AM")
        xvalue.add("12.00 AM")
        xvalue.add("1.00 AM")
        xvalue.add("3.00 PM")
        xvalue.add("7.00 PM")

        val xvalue1 = ArrayList<String>()
        xvalue1.add("12.00 AM")
        xvalue1.add("1.00 AM")
        xvalue1.add("3.00 AM")
        xvalue1.add("5.00 PM")
        xvalue1.add("8.00 PM")

        val lineentry = ArrayList<Entry>();
        lineentry.add(Entry(60f, 0))
        lineentry.add(Entry(40f, 1))
        lineentry.add(Entry(20f, 2))
        lineentry.add(Entry(10f, 3))
        lineentry.add(Entry(5f, 4))

        val linedataset = LineDataSet(lineentry, "First")
        linedataset.color = resources.getColor(R.color.red)
        val data = LineData(xvalue, linedataset)
        data.addDataSet(linedataset)

        val lineentry1 = ArrayList<Entry>();
        lineentry1.add(Entry(10f, 0))
        lineentry1.add(Entry(40f, 1))
        lineentry1.add(Entry(20f, 2))
        lineentry1.add(Entry(70f, 3))
        lineentry1.add(Entry(50f, 4))

        val linedataset1 = LineDataSet(lineentry, "Second")
        linedataset1.color = resources.getColor(R.color.red)
        data.addDataSet(linedataset1)
        val datacombine = CombinedData()
        datacombine.setData(data)
        val combinechart = binding.combinechart
        combinechart.data = datacombine
        combinechart.setBackgroundColor(resources.getColor(R.color.white))
        combinechart.animateXY(3000, 3000)

        val xval = combinechart.xAxis
        xval.position = XAxis.XAxisPosition.BOTTOM
        xval.setDrawGridLines(false)

    }

    private fun setLineChartData() {

        val xvalue = ArrayList<String>()
        xvalue.add("11.00 AM")
        xvalue.add("12.00 AM")
        xvalue.add("1.00 AM")
        xvalue.add("3.00 PM")
        xvalue.add("7.00 PM")

        val lineentry = ArrayList<Entry>();
        lineentry.add(Entry(20f, 0))
        lineentry.add(Entry(50f, 1))
        lineentry.add(Entry(60f, 2))
        lineentry.add(Entry(30f, 3))
        lineentry.add(Entry(10f, 4))

        val linedataset = LineDataSet(lineentry, "First")
        linedataset.color = resources.getColor(R.color.purple_200)

        val data = LineData(xvalue, linedataset)
        val lineChart = binding.lineChart
        lineChart.data = data
        lineChart.setBackgroundColor(resources.getColor(R.color.white))
        lineChart.animateXY(3000, 3000)
    }

    private fun setCandleStickChart() {
        val xvalue = ArrayList<String>()
        xvalue.add("10:00 AM")
        xvalue.add("11:00 AM")
        xvalue.add("12:00 AM")
        xvalue.add("3:00 PM")
        xvalue.add("5:00 PM")
        xvalue.add("8:00 PM")
        xvalue.add("10:00 PM")
        xvalue.add("12:00 PM")

        val candlestickentry = ArrayList<CandleEntry>()
        candlestickentry.add(CandleEntry(0, 225.0f, 219.84f, 224.94f, 226.41f))
        candlestickentry.add(CandleEntry(1, 228.0f, 222.14f, 223.00f, 212.41f))
        candlestickentry.add(CandleEntry(2, 226.84f, 217.84f, 222.9f, 229.41f))
        candlestickentry.add(CandleEntry(3, 222.0f, 216.12f, 214.14f, 216.41f))
        candlestickentry.add(CandleEntry(4, 226.56f, 212.84f, 224.33f, 229.41f))
        candlestickentry.add(CandleEntry(5, 221.12f, 269.84f, 228.14f, 216.41f))
        candlestickentry.add(CandleEntry(6, 220.96f, 237.84f, 224.94f, 246.41f))

        val candledataset = CandleDataSet(candlestickentry, "first")
        candledataset.color = Color.rgb(80, 80, 80)
        candledataset.shadowColor = resources.getColor(R.color.lavand)
        candledataset.shadowWidth = 1f
        candledataset.decreasingColor = resources.getColor(R.color.red)
        candledataset.decreasingPaintStyle = Paint.Style.FILL
        candledataset.increasingColor = resources.getColor(R.color.lavand)
        candledataset.increasingPaintStyle = Paint.Style.FILL

        val candledata = CandleData(xvalue, candledataset)
        val candlechart = binding.candlechart
        candlechart.data = candledata
        candlechart.setBackgroundColor(resources.getColor(R.color.white))
        candlechart.animateXY(3000, 3000)

        val xval = candlechart.xAxis
        xval.position = XAxis.XAxisPosition.BOTTOM
        xval.setDrawGridLines(false)
    }

    private fun getStatic(n: Int) {
        val formBody = FormBody.Builder()
            .add("description", "")
            .build()
        val request = Request.Builder()
            .url("http://192.168.43.35:8080/water/service/")
            .post(formBody)
            .addHeader("Authorization", "Token $token")
            .build()

        client.newCall(request).execute().use { response ->
            if (!response.isSuccessful) throw IOException("Unexpected code $response")
            val result = response.body!!.string()
            Log.d("JSON", result)
        }
    }
}
