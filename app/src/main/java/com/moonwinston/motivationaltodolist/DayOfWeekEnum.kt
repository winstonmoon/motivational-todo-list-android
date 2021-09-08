package com.moonwinston.motivationaltodolist

import androidx.annotation.StringRes

enum class DayOfWeekEnum(@StringRes val dayNumberOfWeek: Int,
                         @StringRes val dayOfWeek: Int
) {
    None(R.string.none_number,R.string.none_string),
    Sunday(R.string.sun_number, R.string.sun_string),
    Monday(R.string.mon_number, R.string.mon_string),
    Tuesday(R.string.tue_number, R.string.tue_string),
    Wednesday(R.string.wed_number, R.string.wed_string),
    Thursday(R.string.thu_number, R.string.thu_string),
    Friday(R.string.fri_number, R.string.fri_string),
    Saturday(R.string.sat_number, R.string.sat_string)
}