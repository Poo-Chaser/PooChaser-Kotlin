package com.poochaser.poochaser.ui.calendar

import android.app.Activity
import android.graphics.drawable.Drawable
import com.poochaser.poochaser.R
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.prolificinteractive.materialcalendarview.DayViewDecorator
import com.prolificinteractive.materialcalendarview.DayViewFacade

class DateSelectorDecorator(context: Activity) : DayViewDecorator {
    var drawable: Drawable = context.getDrawable(R.drawable.date_selector)!!

    override fun decorate(view: DayViewFacade?) {
        view!!.setSelectionDrawable(drawable)
    }

    override fun shouldDecorate(day: CalendarDay?): Boolean {
        return true
    }

}