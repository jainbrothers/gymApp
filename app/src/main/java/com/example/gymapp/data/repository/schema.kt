package com.example.gymapp.data.repository

import com.example.gymapp.model.Gym


const val USER_TABLE_NAME = "user"
const val MOBILE_NUMBER_FIELD_NAME = "mobile_number"
const val USER_ID_FIELD_NAME = "user_id"

/**
 * Constants for [Gym] data class
 */
const val GYM_TABLE_NAME = "Gym"
const val BEGIN_HOUR_FIELD_NAME = "begin_hour"
const val BEGIN_MINUTE_FIELD_NAME = "begin_minute"
const val END_HOUR_FIELD_NAME = "end_hour"
const val END_MINUTE_FIELD_NAME = "end_minute"
const val STREET_NAME_AND_NUMBER_FIELD_NAME = "street_name_and_number"
const val PIN_CODE_FIELD_NAME = "pin_code"
const val IMAGE_URLS_FIELD_NAME = "image_urls"
const val ACTIVITY_TO_DAY_TO_SESSION_SCHEDULE_FIELD_NAME  = "default_schedule"

const val BOOKED_SESSION_TABLE_NAME = "booked_session"
const val BOOKING_TIME_EPOCH_IN_MILLIS_FIELD_NAME = "booking_time_epoch_in_millis"
const val DURATION_IN_MINUTES_FIELD_NAME = "duration_in_minutes"
const val GYM_ID_FIELD_NAME = "gym_id"
const val PAYMENT_DETAIL_FIELD_NAME = "payment_detail"
const val SESSION_START_TIME_EPOCH_IN_MILLIS_FIELD_NAME = "session_start_time_epoch_in_millis"

const val AMOUNT_PAID_FIELD_NAME = "amount_paid"
const val AMOUNT_DUE_FIELD_NAME = "amount_due"