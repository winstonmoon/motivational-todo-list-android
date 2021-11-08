package com.moonwinston.motivationaltodolist

import androidx.annotation.StringRes

enum class MonthEnum(
    @StringRes val monthNumber: Int,
    @StringRes val monthAbbreviation: Int
) {
    January(R.string.jan_number, R.string.jan_abbreviation),
    February(R.string.feb_number, R.string.feb_abbreviation),
    March(R.string.mar_number, R.string.mar_abbreviation),
    April(R.string.apr_number, R.string.apr_abbreviation),
    May(R.string.may_number, R.string.may_abbreviation),
    June(R.string.jun_number, R.string.jun_abbreviation),
    July(R.string.jul_number, R.string.jul_abbreviation),
    August(R.string.aug_number, R.string.aug_abbreviation),
    September(R.string.sep_number, R.string.sep_abbreviation),
    October(R.string.oct_number, R.string.oct_abbreviation),
    November(R.string.nov_number, R.string.nov_abbreviation),
    December(R.string.dec_number, R.string.dec_abbreviation)
}