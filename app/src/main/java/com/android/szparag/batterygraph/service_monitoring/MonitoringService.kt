package com.android.szparag.batterygraph.service_monitoring

import android.app.Notification
import android.app.PendingIntent
import com.android.szparag.batterygraph.base.views.View

interface MonitoringService : View {

  fun startServiceAsForegroundService()

  fun setupBackToAppIntent()
  fun setupNotificationChannel()
  fun setupNotification(backToAppIntent: PendingIntent)
  fun showNotification(foregroundNotification: Notification)

  fun registerSystemEventsReceivers()
  fun unregisterSystemEventsReceivers()

  fun notificationChannelId(): String
  fun notificationId(): Int

  fun registerBatteryStatusReceiver()
  fun registerConnectivityReceiver()
  fun registerDevicePowerReceiver()
  fun registerFlightModeListener()

  fun unregisterBatteryStatusReceiver()
  fun unregisterConnectivityReceiver()
  fun unregisterDevicePowerReceiver()
  fun unregisterFlightModeListener()

}