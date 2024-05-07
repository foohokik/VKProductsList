package com.example.vkproductslist.core

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat

fun Context.showKeyBoard(view: View?) {
  view?.let {
    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(view, 0)
  }
}

fun Context.hideKeyboard(view: View) {
  val inputMethodManager =
      ContextCompat.getSystemService(view.context, InputMethodManager::class.java)
  inputMethodManager?.hideSoftInputFromWindow(view.windowToken, 0)
}

fun Activity.hideKeyboard() {
  hideKeyboard(currentFocus ?: View(this))
}
