package com.costular.sunkalc

import com.costular.sunkalc.Constants.J1970
import com.costular.sunkalc.Constants.J2000
import com.costular.sunkalc.Constants.dayMs
import com.costular.sunkalc.Constants.e
import com.costular.sunkalc.Constants.rad
import org.threeten.bp.Instant
import org.threeten.bp.LocalDateTime
import org.threeten.bp.ZoneId
import org.threeten.bp.ZoneOffset
import kotlin.math.*

internal object MathUtils {

    fun toJulian(date: LocalDateTime): Double =
        date.toInstant(ZoneOffset.UTC).toEpochMilli() / dayMs - 0.5 + J1970

    fun fromJulian(julian: Double): LocalDateTime =
        LocalDateTime.ofInstant(Instant.ofEpochMilli(((julian + 0.5 - J1970) * dayMs).toLong()), ZoneId.systemDefault())

    fun toDays(date: LocalDateTime): Double =
        toJulian(date) - J2000

    fun rightAscension(l: Double, b: Double): Double {
        return atan2(sin(l) * cos(e) - tan(b) * sin(e), cos(l))
    }

    fun declination(l: Double, b: Double): Double {
        return asin(sin(b) * cos(e) + cos(b) * sin(e) * sin(l))
    }

    fun azimuth(H: Double, phi: Double, dec: Double): Double {
        return atan2(sin(H), cos(H) * sin(phi) - tan(dec) * cos(phi))
    }

    fun altitude(H: Double, phi: Double, dec: Double): Double {
        return asin(sin(phi) * sin(dec) + cos(phi) * cos(dec) * cos(H))
    }

    fun siderealTime(d: Double, lw: Double): Double {
        return rad * (280.16 + 360.9856235 * d) - lw
    }

    fun astroRefraction(h: Double): Double {
        var hChecked = if (h < 0) h else h // the following formula works for positive altitudes only.

        // formula 16.4 of "Astronomical Algorithms" 2nd edition by Jean Meeus (Willmann-Bell, Richmond) 1998.
        // 1.02 / tan(h + 10.26 / (h + 5.10)) h in degrees, result in arc minutes -> converted to rad:
        return 0.0002967 / tan(hChecked + 0.00312536 / (hChecked + 0.08901179))
    }

// general sun calculations

    fun solarMeanAnomaly(d: Double): Double {
        return rad * (357.5291 + 0.98560028 * d)
    }

    fun eclipticLongitude(M: Double): Double {
        var C = rad * (1.9148 * sin(M) + 0.02 * sin(2 * M) + 0.0003 * sin(3 * M)) // equation of center
        val P = rad * 102.9372; // perihelion of the Earth

        return M + C + P + PI
    }

    fun getSunCoords(d: Double): Pair<Double, Double> {
        val M = solarMeanAnomaly(d)
        val L = eclipticLongitude(M)

        return Pair(declination(L, 0.0), rightAscension(L, 0.0))
    }

    fun getMoonCords(d: Double): MoonCords {
        val L = rad * (218.316 + 13.176396 * d) // ecliptic longitude
        val M = rad * (134.963 + 13.064993 * d) // mean anomaly
        val F = rad * (93.272 + 13.229350 * d)  // mean distance

        val l = L + rad * 6.289 * sin(M) // longitude
        val b = rad * 5.128 * sin(F)     // latitude
        val dt = 385001 - 20905 * cos(M)  // distance to the moon in km

        return MoonCords(
            rightAscension(l, b),
            declination(l, b),
            dt
        )
    }

    fun hoursLater(date: LocalDateTime, hoursLater: Int): LocalDateTime =
        date.plusHours(hoursLater.toLong())

}