package com.android.szparag.batterygraph.common.presenters

import com.android.szparag.batterygraph.common.views.View

/**
 * Created by Przemyslaw Jablonski (github.com/sharaquss, pszemek.me) on 02/11/2017.
 */
interface Presenter<V : View> {

  var view: V?

  /**
   *  Attaches given View to this presenter, allowing two-way communication.
   *  Should be implemented in some abstract base Presenter as a final method.
   *
   *  Calls Presenter#onAttached() method when succeeded.
   */
  fun attach(view: V)

  /**
   *  Called when View attachment (#attach() method) was successful.
   *
   *  Should be left blank in abstract base Presenter, so that non-abstract
   *  classes extending the Presenter interface can perform additional mandatory setup operations
   *  like permission/network checks here.
   */
  fun onAttached()

  /**
   *  Detaching View from this Presenter.
   *  Should be called upon View decomposition (onStop / onDestroy methods etc.).
   *  Should be implemented in some abstract base Presenter as a final method.
   *
   *  Calls Presenter#onBeforeDetached() just before actually decomposing
   *  aggregated references and dependencies.
   */
  fun detach()

  /**
   *  Called just before View detachment operation (#detach() method) is completed.
   *
   *  Should be left blank in abstract base Presenter, so that non-abstract
   *  classes extending the Presenter interface can perform mandatory cleaning before
   *  View is detached and Presenter decomposed.
   */
  fun onBeforeDetached()

  /**
   * Setting subscriptions for all of the events coming from Interactor class.
   *
   * In order for subscriptions to be properly cleaned up, those Disposables should be aggregated inside #modelDisposables variable
   * (.toModelDisposable() extension function comes handy here).
   */
  fun subscribeModelEvents()

  /**
   * Setting subscriptions for user triggered events coming from View class.
   *
   * In order for subscriptions to be properly cleaned up, those Disposables should be aggregated inside #viewDisposables variable
   * (.toViewDisposable() extension function comes handy here).
   */
  fun subscribeViewUserEvents()

}
