package com.preonboarding.locationhistory.common

object Constants {

    const val NAVER_CLIENT_ID: String = "inrrgd4o6g"
    const val NAVER_CLIENT_SECRET: String = "hqaWnBxBeOExrWr6lRRXGqjpvrcL7IxijtL4Jkj8"

    const val WORK_REPEAT_INTERVAL_DEFAULT: Int = 15
    const val WORK_SAVE_HISTORY: String = "workHistorySave"

    const val LOCATION_PERMISSION_REQUEST_CODE = 10

    //라이브러리 자체 설정 PERIOD 최소 15분, FLEX 최소 5분
    const val SAVE_HISTORY_PERIOD_MIN: Int = 15
    const val SAVE_HISTORY_PERIOD_MAX: Int = 60
    const val SAVE_HISTORY_FLEX_PERIOD_MIN: Int = 5
    const val SAVE_HISTORY_PERIOD_KEY = "saveHistoryPeriodKey"
    const val SHARED_PREFERENCES = "sharedPreferences"

}