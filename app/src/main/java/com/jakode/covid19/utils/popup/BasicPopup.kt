package com.jakode.covid19.utils.popup

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.PopupWindow
import kotlin.properties.Delegates

abstract class BasicPopup {
    private lateinit var inflater: LayoutInflater
    lateinit var popupView: View
    lateinit var popupWindow: PopupWindow

    private fun inflate(anchor: View) {
        inflater =
            anchor.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    }

    private fun createView(layoutResource: Int) {
        popupView = inflater.inflate(layoutResource, null)
    }

    private fun createWindow(
        focusable: Boolean,
        elevation: Float,
        anchor: View,
        coordinates: Builder.Coordinates
    ) {
        val width = LinearLayout.LayoutParams.WRAP_CONTENT
        val height = LinearLayout.LayoutParams.WRAP_CONTENT
        popupWindow = PopupWindow(popupView, width, height, focusable).apply {
            this.elevation = elevation
            showAsDropDown(anchor, coordinates.x, coordinates.y)
        }
    }

    abstract fun newInstance(): BasicPopup
    abstract fun clickListener(listeners: List<() -> Unit>)
    abstract fun initialize(popupTexts: List<String>)

    inner class Builder(initialize: Builder.() -> Unit) {
        lateinit var anchor: View
        lateinit var popupTexts: List<String>
        var listeners: List<() -> Unit> = listOf {}

        val coordinates: Coordinates by lazy { Coordinates() }
        var layoutResource by Delegates.notNull<Int>()
        var focusable: Boolean = true
        var elevation: Float = 4F

        init {
            initialize()
        }

        fun build(): BasicPopup {
            return newInstance().apply {
                inflate(anchor)
                createView(layoutResource)
                createWindow(focusable, elevation, anchor, coordinates)

                initialize(popupTexts)
                clickListener(listeners)
            }
        }

        inner class Coordinates {
            var x: Int = 0
            var y: Int = 0
        }
    }
}