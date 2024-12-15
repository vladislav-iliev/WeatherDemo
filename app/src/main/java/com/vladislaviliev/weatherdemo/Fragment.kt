package com.vladislaviliev.weatherdemo

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.vladislaviliev.weatherdemo.list.Adapter
import com.vladislaviliev.weatherdemo.weather.Forecast
import com.vladislaviliev.weatherdemo.weather.Location
import com.vladislaviliev.weatherdemo.weather.Message

class Fragment : Fragment(), AdapterView.OnItemSelectedListener {

    private val data: RuntimeData by viewModels()
    private lateinit var spinner: Spinner
    private lateinit var messageView: TextView
    private lateinit var list: RecyclerView

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View =
        inflater.inflate(R.layout.fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        spinner = view.findViewById(R.id.spinner)
        messageView = view.findViewById(R.id.message)
        list = view.findViewById<RecyclerView>(R.id.list).apply { adapter = Adapter() }
        populateSpinner()
        view.findViewById<View>(R.id.btn_refresh).setOnClickListener { onRefreshClicked() }
        data.message.observe(viewLifecycleOwner) { onMessageChanged(it) }
        data.forecast.observe(viewLifecycleOwner) { onForecastChanged(it) }
    }

    private fun onRefreshClicked() {
        data.onRefreshClicked(spinner.selectedItem as Location)
    }

    private fun onMessageChanged(msg: Message) {
        messageView.text = getString(R.string.message_format, getString(msg.short), msg.details)
        messageView.isVisible = msg.isImportant
        list.isVisible = !msg.isImportant
    }

    private fun onForecastChanged(forecast: Forecast) {
        (list.adapter as Adapter).submitList(forecast.sensors)
    }

    private fun populateSpinner() {
        spinner.adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, Location.values()).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        spinner.onItemSelectedListener = this
        spinner.setSelection(PersistentData().load(requireActivity().getPreferences(Context.MODE_PRIVATE)).ordinal)
    }

    override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
        val location = parent.getItemAtPosition(position) as Location
        PersistentData().save(location, requireActivity().getPreferences(Context.MODE_PRIVATE))
        data.onLocationSelected(location)
    }

    override fun onNothingSelected(parent: AdapterView<*>) {}
}