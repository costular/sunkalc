package com.costular.sunkalc

import io.kotlintest.TestCase
import io.kotlintest.matchers.boolean.shouldBeTrue
import io.kotlintest.should
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
            val moonPhase = sunkalc.getMoonPhase()

            moonPhase.fraction.shouldBe(0.48f)
            moonPhase.phase.shouldBe(0.75f)
            near(moonPhase.angle, 1.6732942678578346).shouldBeTrue()
            moonPhase.phaseName.shouldBe(MoonPhase.LAST_QUARTER)
            moonPhase.emoji.shouldBe("\uD83C\uDF18")
        }

        "given today is 24 jan 2020 then the moon phase should be new moon" {
            val jan24 = SunKalc(latitude, longitude, LocalDateTime.of(2020, 1, 24, 0, 0, 0))
            val moonPhase = jan24.getMoonPhase()

            moonPhase.phaseName.shouldBe(MoonPhase.NEW_MOON)
            moonPhase.emoji.shouldBe("\uD83C\uDF11")
        }

        "given today is 10 jan 2020 then the moon phase should be full moon" {
            val jan10 = SunKalc(latitude, longitude, LocalDateTime.of(2020, 1, 10, 0, 0, 0))
            val moonPhase = jan10.getMoonPhase()

            moonPhase.phaseName.shouldBe(MoonPhase.FULL_MOON)
            moonPhase.emoji.shouldBe("\uD83C\uDF15")
        }

        "given today is 3 jan 2020 then the moon phase should be first quarter" {
            val jan3 = SunKalc(latitude, longitude, LocalDateTime.of(2020, 1, 3, 0, 0, 0))
            val moonPhase = jan3.getMoonPhase()

            moonPhase.phaseName.shouldBe(MoonPhase.FIRST_QUARTER)
            moonPhase.emoji.shouldBe("\uD83C\uDF13")
        }

        "given today is 17 jan 2020 then the moon phase should be last quarter" {
            val jan17 = SunKalc(latitude, longitude, LocalDateTime.of(2020, 1, 17, 0, 0, 0))
            val moonPhase = jan17.getMoonPhase()

            moonPhase.phaseName.shouldBe(MoonPhase.LAST_QUARTER)
            moonPhase.emoji.shouldBe("\uD83C\uDF17")
        }

        "given today is 30 jan 2020 then the moon phase should be waxing crescent" {
            val jan30 = SunKalc(latitude, longitude, LocalDateTime.of(2020, 1, 30, 0, 0, 0))
            val moonPhase = jan30.getMoonPhase()

            moonPhase.phaseName.shouldBe(MoonPhase.WAXING_CRESCENT)
            moonPhase.emoji.shouldBe("\uD83C\uDF12")
        }

        "getMoonTimes returns moon rise and set times" {
            val moonTimes = sunkalc.getMoonTimes()

            moonTimes.rise.shouldBe(LocalDateTime.of(2013, 3, 4, 23, 54, 29))
            moonTimes.set.shouldBe(LocalDateTime.of(2013, 3, 4, 7, 47, 58))
        }

     }

}