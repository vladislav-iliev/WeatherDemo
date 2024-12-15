package com.vladislaviliev.weatherdemo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vladislaviliev.weatherdemo.weather.Forecast
import com.vladislaviliev.weatherdemo.weather.Location
import com.vladislaviliev.weatherdemo.weather.Message
import com.vladislaviliev.weatherdemo.weather.download.Downloader
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.json.JSONException
import java.io.IOException

class RuntimeData : ViewModel() {

    private val _message: MutableLiveData<Message> = MutableLiveData()
    val message: LiveData<Message> = _message

    private val _forecast: MutableLiveData<Forecast> = MutableLiveData()
    val forecast: LiveData<Forecast> = _forecast

    private var downloadJob: Job? = null

    private fun onDownloadException(e: Throwable) {
        val shortMsg = when (e) {
            is IOException -> R.string.error_connection
            is JSONException -> R.string.error_unknown_format
            else -> R.string.error_unexpected
        }
        _message.value = Message(shortMsg, "${e.javaClass.simpleName}: ${e.message}")
    }

    private fun download(location: Location) {
        _message.value = Message(R.string.loading)
        downloadJob?.cancel()
        downloadJob = viewModelScope.launch(CoroutineExceptionHandler { _, e -> onDownloadException(e) }) {
            _forecast.value = Downloader.download(location)
            _message.value = Message(R.string.loaded, isImportant = false)
        }
    }

    fun onRefreshClicked(currentLocation: Location) {
        download(currentLocation)
    }

    fun onLocationSelected(location: Location) {
        if (forecast.value?.location == location) return
        download(location)
    }
}