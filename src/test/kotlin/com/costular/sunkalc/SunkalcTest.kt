package com.costular.sunkalc

import io.kotlintest.specs.StringSpec
import org.threeten.bp.LocalDate
import org.threeten.bp.LocalDateTime

class Sunkalc : StringSpec() {

    val date = LocalDateTime.of(2013, 3, 5, 0, 0)
    val latitude = 50.5
    val longitude = 30.5
    val height = 2000

    init {
        "getPosition returns azimuth and altitude for the given time and location" {
            
        }
    }

}