package com.example.weatherapp.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.MainViewModel
import com.example.weatherapp.WeatherModel
import com.example.weatherapp.adapters.WeatherAdapter
import com.example.weatherapp.databinding.FragmentHoursBinding
import org.json.JSONArray
import org.json.JSONObject

class HoursFragment : Fragment() {

    private lateinit var binding: FragmentHoursBinding
    private lateinit var adapter: WeatherAdapter
    private val model: MainViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHoursBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecyclerView()
        model.liveDataCurrent.observe(viewLifecycleOwner) {
            adapter.submitList(getHoursList(it))
        }
    }

    private fun getHoursList(weatherItem: WeatherModel): List<WeatherModel> {
        val hoursArray = JSONArray(weatherItem.hours)
        val list = ArrayList<WeatherModel>()
        val city = weatherItem.city
        for (i in 0 until hoursArray.length()) {
            val hours = hoursArray[i] as JSONObject
            val item = WeatherModel(
                city = city,
                time = hours.getString("time"),
                condition = hours.getJSONObject("condition").getString("text"),
                imageURL = hours.getJSONObject("condition").getString("icon"),
                currentTemp = hours.getString("temp_c").substringBefore("."),
                maxTemp = "",
                minTemp = "",
                hours = ""
            )
            list.add(item)
        }
        return list
    }

    private fun initRecyclerView() = with(binding) {
        rvContainer.layoutManager = LinearLayoutManager(activity)
        adapter = WeatherAdapter(null)
        rvContainer.adapter = adapter
    }

    companion object {
        @JvmStatic
        fun newInstance() = HoursFragment()
    }
}