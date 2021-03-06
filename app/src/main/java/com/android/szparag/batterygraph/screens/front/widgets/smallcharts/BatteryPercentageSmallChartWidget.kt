package com.android.szparag.batterygraph.screens.front.widgets.smallcharts

import android.content.Context
import android.util.AttributeSet
import com.android.szparag.batterygraph.common.events.BatteryStateEvent
import com.android.szparag.batterygraph.common.widgets.LineChartBaseWidget
import com.android.szparag.batterygraph.common.widgets.LineChartSmallBaseWidget
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.data.Entry
import timber.log.Timber

/**
 * Small chart that renders information about battery percentage over time, along with device power events (on/off).
 *
 * Generic type pushed to its parent class is BatteryStateEvent.
 *
 * @see BatteryStateEvent
 * @see LineChartBaseWidget
 * @see LineChart
 */
class BatteryPercentageSmallChartWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LineChartSmallBaseWidget<BatteryStateEvent>(context, attrs, defStyleAttr) {

  override fun mapDataToEntry(data: BatteryStateEvent) =
      Entry(data.eventUnixTimestamp.toFloat(), data.batteryPercentage.toFloat())
          .also { Timber.v("mapDataToEntry, data: $data, entry: $it") }

}