package com.android.szparag.batterygraph.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build.VERSION
import android.os.Build.VERSION_CODES
import android.os.IBinder
import android.support.annotation.RequiresApi
import android.support.v7.app.NotificationCompat
import com.android.szparag.batterygraph.R
import com.android.szparag.batterygraph.dagger.DaggerGlobalScopeWrapper
import com.android.szparag.batterygraph.events.BatteryStatusEvent
import com.android.szparag.batterygraph.screenChart.BatteryGraphChartActivity
import com.android.szparag.batterygraph.screenChart.ChartModel
import com.android.szparag.batterygraph.utils.asString
import com.android.szparag.batterygraph.utils.createRegisteredBroadcastReceiver
import com.android.szparag.batterygraph.utils.mapToBatteryStatusEvent
import com.android.szparag.batterygraph.utils.toPendingIntent
import com.android.szparag.batterygraph.utils.ui
import com.android.szparag.batterygraph.utils.unregisterReceiverFromContext
import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject
import timber.log.Timber
import java.util.concurrent.TimeUnit
import javax.inject.Inject

const val EVENTS_PERSISTENCE_SAMPLING_VALUE_SECS = 5L

class BatteryGraphMonitoringService : Service(), MonitoringService {

  @Inject lateinit var model: ChartModel //todo this cant be chartmodel, more like MonitoringEventsInteractor or sth
  private lateinit var batteryChangedActionReceiver: BroadcastReceiver
  private lateinit var batteryChangedSubject: Subject<BatteryStatusEvent>
  private val notificationManager: NotificationManager by lazy {
    applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
  }
  private var notificationChannel: NotificationChannel? = null

  override fun onBind(intent: Intent?): IBinder {
    Timber.d("onBind, intent: $intent")
    throw NotImplementedError()
  }

  override fun onCreate() {
    super.onCreate()
    Timber.d("onCreate")
    DaggerGlobalScopeWrapper.getComponent(this).inject(this)
    registerBatteryStatusReceiver()
    subscribeBatteryStatusChanged()
    startServiceAsForegroundService()
  }

  override fun registerBatteryStatusReceiver() {
    Timber.d("registerBatteryStatusReceiver")
    batteryChangedSubject = PublishSubject.create()
    batteryChangedActionReceiver = createRegisteredBroadcastReceiver(
        intentFilterActions = Intent.ACTION_BATTERY_CHANGED,
        callback = { intent ->
          Timber.d("batteryChangedActionReceiver.callback, intent: ${intent.asString()}")
          batteryChangedSubject.onNext(intent.extras.mapToBatteryStatusEvent())
        }
    )
  }

  override fun unregisterBatteryStatusReceiver() {
    Timber.d("unregisterBatteryStatusReceiver")
    batteryChangedActionReceiver.unregisterReceiverFromContext(this)
  }

  private fun subscribeBatteryStatusChanged() {
    Timber.d("subscribeBatteryStatusChanged")
    batteryChangedSubject
        .subscribeOn(ui())
        .sample(EVENTS_PERSISTENCE_SAMPLING_VALUE_SECS, TimeUnit.SECONDS, true)
        .observeOn(ui())
        .subscribe(this::onBatteryStatusChanged)
    //todo remember about disposing in ondestroy!
  }

  private fun startServiceAsForegroundService() {
    Timber.d("startServiceAsForegroundService")
    val backToAppIntent = Intent(this, BatteryGraphChartActivity::class.java)
        .toPendingIntent(context = this, requestCode = requestCode())

    startForeground(
        requestCode(),
        if (VERSION.SDK_INT >= VERSION_CODES.O) {
          createNotificationChannel()
          createForegroundNotificationWithChannel(backToAppIntent)
        } else
          createForegroundNotificationWithoutChannel(backToAppIntent)
    )
  }

  @RequiresApi(VERSION_CODES.O)
  private fun createNotificationChannel() {
    Timber.d("createNotificationChannel")
    if (notificationChannel == null) {
      notificationChannel = NotificationChannel(
          notificationChannelId(),
          getText(R.string.service_notification_channel_name),
          NotificationManager.IMPORTANCE_DEFAULT)
      notificationManager.createNotificationChannel(notificationChannel)
    }
  }

  @RequiresApi(VERSION_CODES.O)
  private fun createForegroundNotificationWithChannel(onClickIntent: PendingIntent) =
      Notification.Builder(this, notificationChannelId())
          .setContentTitle(getText(R.string.service_notification_title))
          .setContentText(getText(R.string.service_notification_text))
          .setContentIntent(onClickIntent)
          .setSmallIcon(R.drawable.mock_icon)
          .build()
          .also { Timber.d("createForegroundNotificationWithChannel, return: $it") }


  private fun createForegroundNotificationWithoutChannel(onClickIntent: PendingIntent) =
      NotificationCompat.Builder(this)
          .setContentTitle(getText(R.string.service_notification_title))
          .setContentText(getText(R.string.service_notification_text))
          .setContentIntent(onClickIntent)
          .setSmallIcon(R.drawable.mock_icon)
          .build()
          .also { Timber.d("createForegroundNotificationWithoutChannel, return: $it") }


  private fun onBatteryStatusChanged(batteryStatusEvent: BatteryStatusEvent) {
    Timber.d("onBatteryStatusChanged, batteryStatusEvent: $batteryStatusEvent")
    model.insertBatteryEvent(batteryStatusEvent)
  }

  override fun onDestroy() {
    super.onDestroy()
    Timber.d("onDestroy")
    //todo think about proper disposing
    unregisterBatteryStatusReceiver()
  }

  override fun onLowMemory() {
    super.onLowMemory()
    Timber.d("onLowMemory")
  }

  private fun requestCode() = Math.abs(this.packageName.hashCode())

  private fun notificationChannelId() = requestCode().toString()

  private fun notificationId() = requestCode()

}
