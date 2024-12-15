package com.vladislaviliev.weatherdemo.list

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import com.vladislaviliev.weatherdemo.R
import com.vladislaviliev.weatherdemo.weather.Sensor

class Adapter : ListAdapter<Sensor, Row>(Diff()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        Row(LayoutInflater.from(parent.context).inflate(R.layout.list_row, parent, false))

    override fun onBindViewHolder(holder: Row, position: Int) {
        holder.bind(getItem(position))
    }
}