package com.android.szparag.batterygraph.base.entities

import com.android.szparag.batterygraph.base.events.BatteryStateEvent
import com.android.szparag.batterygraph.base.events.UnixTimestamp
import com.android.szparag.batterygraph.base.utils.invalidFloatValue
import com.android.szparag.batterygraph.base.utils.invalidIntValue
import com.android.szparag.batterygraph.base.utils.invalidLongValue
import io.realm.RealmObject
import io.realm.annotations.PrimaryKey
import io.realm.annotations.RealmClass


@RealmClass open class RealmBatteryEvent(
    @PrimaryKey var unixTimestamp: UnixTimestamp = invalidLongValue(),
    var batteryStatus: Int = invalidIntValue(),
    var batteryHealth: Int = invalidIntValue(),
    var batteryPowerSource: Int = invalidIntValue(),
    var batteryPercentage: Int = invalidIntValue(),
    var batteryVoltage: Float = invalidFloatValue(),
    var batteryTemperature: Int = invalidIntValue()
) : RealmObject() {

  fun toBatteryStatusEvent() = BatteryStateEvent(
      unixTimestamp, batteryStatus, batteryHealth, batteryPowerSource, batteryPercentage,
      batteryVoltage, batteryTemperature
  )

}

fun BatteryStateEvent.toRealmEvent() = RealmBatteryEvent(
    unixTimestamp = eventUnixTimestamp,
    batteryStatus = batteryStatus.statusInt,
    batteryHealth = batteryHealth.healthInt,
    batteryPowerSource = batteryPowerSource.sourceInt,
    batteryPercentage = batteryPercentage,
    batteryVoltage = batteryVoltage,
    batteryTemperature = batteryTemperature
)