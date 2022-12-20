package com.example.bambino.home

import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.example.bambino.R
import com.example.bambino.database.weight.WeightDatabase
import com.example.bambino.databinding.DialogAddWeightMeasurementBinding
import com.example.bambino.databinding.FragmentHomeBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var measureDialogBinding: DialogAddWeightMeasurementBinding

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    var date: Long = 0
    var weight: Double = 0.0

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        val application = requireNotNull(this.activity).application
        val dataSource = WeightDatabase.getInstance(application).weightDatabaseDao
        val viewModelFactory = HomeViewModelFactory(dataSource)

        homeViewModel = ViewModelProvider(this, viewModelFactory)[HomeViewModel::class.java]
        binding.homeViewModel = homeViewModel
        binding.lifecycleOwner = this


        //INITIALIZING PHOTOS
        Glide
            .with(this)
            .load(R.drawable.welcome_back_image)
            .centerCrop()
            .into(binding.familyPhoto)

        Glide
            .with(this)
            .load(R.drawable.kid_eating)
            .centerCrop()
            .into(binding.kidEatingPhoto)

        Glide
            .with(this)
            .load(R.drawable.camera_photo_mommy)
            .centerCrop()
            .into(binding.makingPhotoPhoto)


        //CHART

        val entriesWeight = homeViewModel.allMeasurements
        val entries = mutableListOf<Entry>()

        entries.add(Entry(1.67106236E12f, 3.5f))
        entries.add(Entry(1.67114886E12f, 4.5f))
        entries.add(Entry(1.67123524E12f, 5f))
        entries.add(Entry(1.67132162E12f, 2.5f))
        entries.add(Entry(1.67140799E12f, 3.5f))
        entries.add(Entry(1.67149437E12f, 4.5f))
        entries.add(Entry(1.67158075E12f, 5f))


        if (entriesWeight.value != null) {
            for (measurement in entriesWeight.value!!) {
                val entry = Entry(
                    measurement.measurement_date.toFloat(),
                    measurement.measurementResult.toFloat()
                )
                entries.add(entry)
            }
        }

        val dataSet = LineDataSet(entries, "Weight chart")
        styleLineDataSet(dataSet)
        val lineData = LineData(dataSet)

        binding.chart.data = lineData
        styleChart(binding.chart)
        binding.chart.invalidate()


        //BUTTONS

        binding.clearChart.setOnClickListener {
            uiScope.launch {
                homeViewModel.clearChart()
            }
        }

        binding.addToChart.setOnClickListener {
            val dialog = MaterialAlertDialogBuilder(binding.addToChart.context)
            dialog.setTitle("Add new weight measurement")

            measureDialogBinding =
                DialogAddWeightMeasurementBinding.inflate(LayoutInflater.from(binding.addToChart.context))

            measureDialogBinding.fullNumber.minValue = 0
            measureDialogBinding.fullNumber.maxValue = 40
            measureDialogBinding.fullNumber.value = 0

            measureDialogBinding.decimalNumber.minValue = 0
            measureDialogBinding.decimalNumber.maxValue = 9
            measureDialogBinding.decimalNumber.value = 0


            val datePicker =
                MaterialDatePicker.Builder.datePicker()
                    .setTitleText("Select date")
                    .build()

            measureDialogBinding.measurementDateTextInput.setEndIconOnClickListener {
                datePicker.show(parentFragmentManager, "tag")
            }

            measureDialogBinding.measurementDateTextInput.setOnClickListener {
                datePicker.show(parentFragmentManager, "tag")
            }

            datePicker.addOnPositiveButtonClickListener {
                date = datePicker.selection!!
                measureDialogBinding.measurementDateTextInput.editText?.text =
                    Editable.Factory.getInstance().newEditable(
                        SimpleDateFormat("dd-MM-yyyy", Locale.UK)
                            .format(date).toString()
                    )
            }


            dialog.setPositiveButton("Add") { _, _ ->
                weight += measureDialogBinding.fullNumber.value.toFloat()
                weight += (measureDialogBinding.fullNumber.value * 0.1).toFloat()

                uiScope.launch {
                    homeViewModel.addNewMeasurement(date, weight)
                }
            }
            dialog.setNegativeButton("Cancel", null)
            dialog.setView(measureDialogBinding.root)
            dialog.show()
            weight = 0.0
        }

        binding.goToActionsAdd.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_actionFragment)
        }

        binding.goToActionsList.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_trackFragment)
        }

        binding.goToMemoAdd.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_memoryEntryFragment)
        }

        binding.goToMemoList.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_memoriesFragment)
        }


        return binding.root
    }

    //CHART STYLING AND FORMATTING

    private fun styleChart(lineChart: LineChart) = lineChart.apply {
        axisRight.isEnabled = false
        axisLeft.apply {
            isEnabled = true
            textColor = ContextCompat.getColor(requireContext(), R.color.md_theme_dark_onBackground)
            setDrawGridLines(false)
            axisMinimum = 0f
            axisMaximum = 10f
            granularity = 0.1f
        }

        xAxis.apply {
            setDrawGridLines(false)
            position = XAxis.XAxisPosition.BOTTOM
            textColor = ContextCompat.getColor(requireContext(), R.color.md_theme_dark_onBackground)
            valueFormatter = DateValueFormatter()
            granularity = 86400000f
        }

        setTouchEnabled(true)
        isDragEnabled = true
        setScaleEnabled(true)
        setPinchZoom(true)
        description = null
        legend.isEnabled = false
    }


    private fun styleLineDataSet(lineDataSet: LineDataSet) = lineDataSet.apply {
        color = ContextCompat.getColor(requireContext(), R.color.md_theme_dark_onPrimaryContainer)
        valueTextColor =
            ContextCompat.getColor(requireContext(), R.color.md_theme_dark_onBackground)
        setDrawValues(false)
        lineWidth = 3f
        isHighlightEnabled = true
        setDrawHighlightIndicators(false)
        setDrawCircles(false)
        mode = LineDataSet.Mode.CUBIC_BEZIER

        setDrawFilled(true)
        fillDrawable = ContextCompat.getDrawable(requireContext(), R.drawable.chart_line_gradient)
    }

    private class DateValueFormatter : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            return SimpleDateFormat("dd.MM.yy", Locale.UK)
                .format(value).toString()
        }
    }

}

