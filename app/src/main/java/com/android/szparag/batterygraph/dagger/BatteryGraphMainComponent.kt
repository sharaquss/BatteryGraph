package com.android.szparag.batterygraph.dagger

import com.android.szparag.batterygraph.screens.chart.BatteryGraphChartActivity
import com.android.szparag.batterygraph.screens.front.BatteryGraphFrontActivity
import com.android.szparag.batterygraph.services.monitoring.BatteryGraphMonitoringService
import dagger.Component
import javax.inject.Singleton

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 02/11/2017.
 */
@Singleton
@Component(modules = arrayOf(BatteryGraphMainModule::class))
interface BatteryGraphMainComponent {
  fun inject(target: BatteryGraphChartActivity)
  fun inject(target: BatteryGraphFrontActivity)
  fun inject(target: BatteryGraphMonitoringService)
}