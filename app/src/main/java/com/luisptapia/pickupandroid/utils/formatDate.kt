package com.luisptapia.pickupandroid.utils

import android.os.Build
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

fun formatDate(isoDate: String): String {

    if(
        Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
    ){
        val parsedDate = OffsetDateTime.parse(isoDate, DateTimeFormatter.ISO_DATE_TIME)
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
        return parsedDate.format(formatter)
    }else{
        return  isoDate
    }

}
