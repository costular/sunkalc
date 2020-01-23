package com.costular.sunkalc

import io.kotlintest.TestCase
import io.kotlintest.shouldBe
import io.kotlintest.specs.StringSpec
import org.threeten.bp.LocalDateTime

class SunkalcTest : StringSpec() {

    val date = LocalDateTime.of(2013, 3, 5, 0, 0)
    val latitude = 50.5
    val longitude = 30.5

    lateinit var sunkalc: SunKalc

    override fun beforeTest(testCase: TestCase) {
        super.beforeTest(testCase)
        sunkalc = SunKalc(latitude, longitude, date)
    }

    init {
        "getPosition returns azimuth and altitude for the given time and location" {
            val sunPosition = sunkalc.getSunPosition()
            sunPosition.azimuth.shouldBe(-2.5003175907168385)
            sunPosition.altitude.shouldBe(-0.7000406838781611)
        }

        "getTimes returns sun phases for the given date and location" {
            // TODO
        }

        "getTimes adjusts sun phases when additionally given the observer height" {
            // TODO
        }

        "getMoonPosition returns moon position data given time and location" {
            val moonPosition = sunkalc.getMoonPosition()

            moonPosition.azimuth.shouldBe(-0.9783999522438226)
            moonPosition.altitude.shouldBe(0.014551482243892251)
            moonPosition.distanceKm.shouldBe(364121.37256256194)
        }

        "getMoonIllumination returns fraction and angle of moon's illuminated limb and phase" {
            val moonIllumination = sunkalc.getMoonIllumination()

            moonIllumination.fraction.shouldBe(0.4848068202456373)
            moonIllumination.phase.shouldBe(0.7548368838538762)
            moonIllumination.angle.shouldBe(1.6732942678578346)
        }
     }

}