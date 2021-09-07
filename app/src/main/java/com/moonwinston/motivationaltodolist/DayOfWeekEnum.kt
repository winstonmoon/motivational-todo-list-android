package com.moonwinston.motivationaltodolist

import androidx.annotation.StringRes

enum class DayOfWeekEnum(@StringRes val dayNumberOfWeek: Int,
                         @StringRes val dayOfWeek: Int
) {
    None(R.string.none_number,R.string.none_string),
    Monday(R.string.mon_number, R.string.jan_abbreviation),
    Tuesday(R.string.tue_number, R.string.feb_abbreviation),
    Wednesday(R.string.wed_number, R.string.mar_abbreviation),
    Thursday(R.string.thu_number, R.string.apr_abbreviation),
    Friday(R.string.fri_number, R.string.may_abbreviation),
    Saturday(R.string.sat_number, R.string.jun_abbreviation),
    Sunday(R.string.sun_number, R.string.jul_abbreviation)
}