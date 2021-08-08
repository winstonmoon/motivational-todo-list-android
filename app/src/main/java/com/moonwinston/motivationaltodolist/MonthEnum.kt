package com.moonwinston.motivationaltodolist

import androidx.annotation.StringRes

enum class MonthEnum(
    @StringRes val monthNumber: Int,
    @StringRes val monthAbbreviation: Int
) {
    JANUARY(R.string.jan_number, R.string.jan_abbreviation),
    FEBRUARY(R.string.feb_number, R.string.feb_abbreviation),
    MARCH(R.string.mar_number, R.string.mar_abbreviation),
    APRIL(R.string.apr_number, R.string.apr_abbreviation),
    MAY(R.string.may_number, R.string.mar_abbreviation),
    JUNE(R.string.jun_number, R.string.jun_abbreviation),
    JULY(R.string.jul_number, R.string.jul_abbreviation),
    AUGUST(R.string.aug_number, R.string.aug_abbreviation),
    SEPTEMBER8(R.string.sep_number, R.string.sep_abbreviation),
    OCTOBER(R.string.oct_number, R.string.oct_abbreviation),
    NOVEMBER(R.string.nov_number, R.string.nov_abbreviation),
    DECEMBER(R.string.dec_number, R.string.dec_abbreviation)
}