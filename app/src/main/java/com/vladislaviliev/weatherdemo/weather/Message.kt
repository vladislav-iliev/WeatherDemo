package com.vladislaviliev.weatherdemo.weather

import androidx.annotation.StringRes

data class Message(@StringRes val short: Int, val details: CharSequence = "", val isImportant: Boolean = true)