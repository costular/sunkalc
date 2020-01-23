package com.costular.sunkalc

import io.kotlintest.TestCase
import io.kotlintest.matchers.boolean.shouldBeTrue
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

            near(moonPosition.azimuth, -0.9783999522438226).shouldBeTrue()
            near(moonPosition.altitude, 0.014551482243892251).shouldBeTrue()
            near(moonPosition.distanceKm, 364121.37256256194).shouldBeTrue()
        }

        "getMoonIllumination returns fraction and angle of moon's illuminated limb and phase" {
            val moonIllumination = sunkalc.getMoonIllumination()

            near(moonIllumination.fraction, 0.4848068202456373).shouldBeTrue()
            near(moonIllumination.phase, 0.7548368838538762).shouldBeTrue()
            near(moonIllumination.angle, 1.6732942678578346).shouldBeTrue()
        }
     }

}