package com.android.szparag.batterygraph.screens.front.widgets

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.support.annotation.ColorInt
import android.support.constraint.ConstraintLayout
import android.util.AttributeSet
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import com.android.szparag.batterygraph.R
import com.android.szparag.batterygraph.common.utils.idName
import kotlinx.android.synthetic.main.layout_batterystats_details.view.contentHealth
import kotlinx.android.synthetic.main.layout_batterystats_details.view.contentPercentage
import kotlinx.android.synthetic.main.layout_batterystats_details.view.contentPowerSource
import kotlinx.android.synthetic.main.layout_batterystats_details.view.contentStatus
import kotlinx.android.synthetic.main.layout_batterystats_details.view.contentTemperature
import kotlinx.android.synthetic.main.layout_batterystats_details.view.contentVoltage
import kotlinx.android.synthetic.main.layout_batterystats_details.view.headerHealth
import kotlinx.android.synthetic.main.layout_batterystats_details.view.headerPercentage
import kotlinx.android.synthetic.main.layout_batterystats_details.view.headerPowerSource
import kotlinx.android.synthetic.main.layout_batterystats_details.view.headerStatus
import kotlinx.android.synthetic.main.layout_batterystats_details.view.headerTemperature
import kotlinx.android.synthetic.main.layout_batterystats_details.view.headerVoltage
import timber.log.Timber

private const val TEXTVIEW_TEXTCOLOR_PARAMETER_NAME = "textColor"

class BatteryStatsDetailsContainer @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {

  private val highlightedColor by lazy { resources.getColor(R.color.batteryGraphTextColorHighlighted) }
  private val headerColor by lazy { resources.getColor(R.color.batteryGraphTextColorHeader) }
  private val contentColor by lazy { resources.getColor(R.color.batteryGraphTextColorContent) }

  init {
    View.inflate(context, R.layout.layout_batterystats_details, this)
  }

  fun renderPercentage(value: Int) = renderSingleBatteryStat(value.toString(), headerPercentage, contentPercentage)

  fun renderHealth(value: String) = renderSingleBatteryStat(value, headerHealth, contentHealth)

  fun renderStatus(value: String) = renderSingleBatteryStat(value, headerStatus, contentStatus)

  fun renderPowerSource(value: String) = renderSingleBatteryStat(value, headerPowerSource, contentPowerSource)

  fun renderVoltage(value: Float) = renderSingleBatteryStat(String.format("%.2f", value), headerVoltage, contentVoltage)

  fun renderTemperature(value: Int) = renderSingleBatteryStat(value.toString(), headerTemperature, contentTemperature)

  @SuppressLint("BinaryOperationInTimber")
  private fun renderSingleBatteryStat(value: String, headerTextView: TextView?, contentTextView: TextView?) {
    val cachedContentText = contentTextView?.text
    Timber.d("renderSingleBatteryStat, headerTextView: ${headerTextView?.idName}, contentTextView: ${contentTextView?.idName}, " +
        "value: $value, cache: $cachedContentText")
    if (headerTextView == null || contentTextView == null) return
    contentTextView.text = value
    if (cachedContentText != null && cachedContentText.isNotEmpty() && cachedContentText != value) {
      highlightHeaderTextView(headerTextView)
      highlightContentTextView(contentTextView)
    }
  }

  private fun highlightHeaderTextView(headerTextView: TextView) = highlightTextView(headerTextView, headerColor)
  private fun highlightContentTextView(contentTextView: TextView) = highlightTextView(contentTextView, contentColor)

  private fun highlightTextView(view: TextView, @ColorInt nonHighlightedColor: Int) =
      ObjectAnimator.ofInt(view, TEXTVIEW_TEXTCOLOR_PARAMETER_NAME, highlightedColor, nonHighlightedColor)
          .apply {
            setEvaluator(ArgbEvaluator())
            duration = 1500 //todo: 1500?
            interpolator = DecelerateInterpolator(0.35f)
            start()
          }
}