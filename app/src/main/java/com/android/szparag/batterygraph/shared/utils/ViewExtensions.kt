package com.android.szparag.batterygraph.shared.utils

import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.animation.TimeInterpolator
import android.view.View
import android.view.ViewPropertyAnimator
import android.view.animation.LayoutAnimationController.AnimationParameters

typealias Widget = View

private val animatorListenerCallbackStub: (Animator?) -> Unit = {}

fun Widget.getLocationInWindow() = IntArray(2).apply {
  this@getLocationInWindow.getLocationInWindow(this)
}

fun Widget.getLocationOnScreen() = IntArray(2).apply {
  this@getLocationOnScreen.getLocationOnScreen(this)
  return this
}

fun Widget.hide() {
  this.visibility = View.GONE
}

fun Widget.show() {
  this.visibility = View.VISIBLE
}

fun Widget.shrinkViewToZero() = configureLayoutParams(width = 0, height = 0)

fun Widget.configureLayoutParams(width: Int, height: Int,
    animationParams: AnimationParameters? = null) = this.layoutParams
    .apply {
      this.width = width
      this.height = height
      animationParams?.let { this.layoutAnimationParameters = it }
      this@configureLayoutParams.layoutParams = this
    }

fun ViewPropertyAnimator.duration(millis: Long) = this.setDuration(millis)
fun ViewPropertyAnimator.startDelay(millis: Long) = this.setStartDelay(millis)
fun ViewPropertyAnimator.interpolator(interpolator: TimeInterpolator) = this.setInterpolator(interpolator)

fun ViewPropertyAnimator.setListenerBy(
    onStart: (Animator?) -> (Unit) = animatorListenerCallbackStub,
    onEnd: (Animator?) -> (Unit) = animatorListenerCallbackStub,
    onCancel: (Animator?) -> (Unit) = animatorListenerCallbackStub,
    onRepeat: (Animator?) -> (Unit) = animatorListenerCallbackStub
) =
    this.setListener(object : AnimatorListener {
      override fun onAnimationRepeat(animation: Animator?) {
        onRepeat(animation)
      }

      override fun onAnimationEnd(animation: Animator?) {
        onEnd(animation)
      }

      override fun onAnimationCancel(animation: Animator?) {
        onCancel(animation)
      }

      override fun onAnimationStart(animation: Animator?) {
        onStart(animation)
      }

    })
