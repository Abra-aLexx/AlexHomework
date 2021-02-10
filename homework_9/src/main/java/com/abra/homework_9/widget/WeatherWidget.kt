package com.abra.homework_9.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.abra.homework_9.R
import com.abra.homework_9.functions.getIconId
import com.abra.homework_9.network.RetrofitInitialization
import com.abra.homework_9.network.WeatherApi
import com.abra.homework_9.network.WeatherRootObject
import com.abra.homework_9.repositories.DatabaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Date
import kotlin.math.roundToInt

class WeatherWidget : AppWidgetProvider() {
    private val retrofit = RetrofitInitialization.getInstance()
    private val widgetScope = CoroutineScope(Dispatchers.Main + Job())
    private val repository = DatabaseRepository(widgetScope)
    private var checkedCityId = 0

    override fun onUpdate(context: Context?, appWidgetManager: AppWidgetManager?, appWidgetIds: IntArray?) {
        super.onUpdate(context, appWidgetManager, appWidgetIds)
        loadLastCityId(context)
        widgetScope.launch {
            val cityData = repository.getCityById(checkedCityId)
            appWidgetIds?.forEach {
                loadForecast(cityData.name, context, appWidgetManager, it)
            }
        }

    }

    private fun loadForecast(cityName: String, context: Context?, appWidgetManager: AppWidgetManager?, widgetId: Int) {
        val widgetView = RemoteViews(context?.packageName, R.layout.widget)
        val whetherApi = retrofit.create(WeatherApi::class.java)
        val whetherApiCall = whetherApi.getWhetherForFiveDays(cityName)
        whetherApiCall.enqueue(object : Callback<WeatherRootObject> {
            override fun onResponse(call: Call<WeatherRootObject>, response: Response<WeatherRootObject>) {
                if (response.isSuccessful) {
                    response.body()?.run {
                        with(widgetView) {
                            setTextViewText(R.id.tvCityNameWidget, "${city.name}, ${city.country}")
                            setTextViewText(R.id.tvDateWidget, SimpleDateFormat.getDateInstance().format(Date()))
                            setTextViewText(R.id.tvTemperatureWidget, list[0].main.temp.roundToInt().toString())
                            setImageViewResource(R.id.ivCurrentWeatherWidget, getIconId(list[0].weather[0].icon))
                            appWidgetManager?.updateAppWidget(widgetId, this@with)
                        }
                    }
                }
            }

            override fun onFailure(call: Call<WeatherRootObject>, t: Throwable) {}

        })
    }

    private fun loadLastCityId(context: Context?) {
        context?.run {
            checkedCityId = getSharedPreferences("lastCityChecked", Context.MODE_PRIVATE).getInt("id", 0)
        }
    }
}