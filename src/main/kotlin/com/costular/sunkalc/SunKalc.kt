package com.costular.sunkalc

import com.costular.sunkalc.Constants.rad
import com.costular.sunkalc.MathUtils.astroRefraction
import com.costular.sunkalc.MathUtils.azimuth
import org.threeten.bp.LocalDateTime
import kotlin.math.*

class SunKalc(
    private val latitude: Double,
    private val longitude: Double,
    private val date: LocalDateTime = LocalDateTime.now()
) {

    /**
     * Returns the sun position
     * @return {@link com.costular.sunkalc.SunPosition} which represents the Sun position
     */
    fun getSunPosition(): SunPosition {
        val lw = rad * -longitude
        val phi = rad * latitude
        val d = MathUtils.toDays(date)

        val c = MathUtils.getSunCoords(d)
        val H = MathUtils.siderealTime(d, lw) - c.second;

        return SunPosition(
            MathUtils.azimuth(H, phi, c.first),
            MathUtils.altitude(H, phi, c.first)
        )
    }

    /**
     * Returns the moon position
     * @return {@link com.costular.sunkalc.MoonPosition} which represents the moon position
     */
    fun getMoonPosition(date: LocalDateTime = this.date): MoonPosition {
        val lw = rad * -longitude
        val phi = rad * latitude
        val d = MathUtils.toDays(date)

        val c = MathUtils.getMoonCords(d)
        val H = MathUtils.siderealTime(d, lw) - c.ra
        var h = MathUtils.altitude(H, phi, c.dec)
        // formula 14.1 of "Astronomical Algorithms" 2nd edition by Jean Meeus (Willmann-Bell, Richmond) 1998.
        val pa = atan2(sin(H), tan(phi) * cos(c.dec) - sin(c.dec) * cos(H));

        h += astroRefraction(h); // altitude correction for refraction

        return MoonPosition(
            azimuth(H, phi, c.dec),
            h,
            c.dist,
            pa
        )
    }

    /**
     * Gets the moon illumination
     * @return {@link com.costular.sunkalc.MoonIllumination} which represents the moon illumination
     */
    fun getMoonIllumination(): MoonIllumination {
        val d = MathUtils.toDays(date)
        val s = MathUtils.getSunCoords(d)
        val m = MathUtils.getMoonCords(d)

        val sdist = 149598000 // distance from Earth to Sun in km

        val phi = acos(sin(s.first) * sin(m.dec) + cos(s.first) * cos(m.dec) * cos(s.second - m.ra))
        val inc = atan2(sdist * sin(phi), m.dist - sdist * cos(phi))
        val angle = atan2(
            cos(s.first) * sin(s.second - m.ra), sin(s.first) * cos(m.dec) -
                    cos(s.first) * sin(m.dec) * cos(s.second - m.ra)
        );

        return MoonIllumination(
            (1 + cos(inc) / 2).toFloat(),
            (0.5 + 0.5 * inc * (if (angle < 0) -1 else 1) / Math.PI).toFloat(),
            angle
        )
    }

    /**
     * Returns the moon times
     * @return {@link com.costular.sunkalc.MoonTime} which represents the times
     */
    fun getMoonTimes(): MoonTime {
        var hc = 0.133 * rad
        var h0 = getMoonPosition().altitude - hc
        var h1: Double
        var h2: Double
        var rise = 0.0
        var set = 0.0
        var a: Double
        var b: Double
        var xe: Double
        var ye = 0.0
        var d: Double
        var roots: Int
        var x1 = 0.0
        var x2 = 0.0
        var dx: Double

        // go in 2-hour chunks, each time seeing if a 3-point quadratic curve crosses zero (which means rise or set)
        for (i in 0..24 step 2) {
            h1 = getMoonPosition(MathUtils.hoursLater(date, i)).altitude - hc;
            h2 = getMoonPosition(MathUtils.hoursLater(date, i + 1)).altitude - hc;

            a = (h0 + h2) / 2 - h1
            b = (h2 - h0) / 2
            xe = -b / (2 * a)
            ye = (a * xe + b) * xe + h1
            d = b * b - 4 * a * h1
            roots = 0

            if (d >= 0) {
                dx = Math.sqrt(d) / (Math.abs(a) * 2)
                x1 = xe - dx
                x2 = xe + dx
                if (Math.abs(x1) <= 1) roots++
                if (Math.abs(x2) <= 1) roots++
                if (x1 < -1) x1 = x2
            }

            if (roots == 1) {
                if (h0 < 0) rise = i + x1
                else set = i + x1

            } else if (roots == 2) {
                rise = i + (if (ye < 0) x2 else x1)
                set = i + (if (ye < 0) x1 else x2)
            }

            if (rise != 0.0 && set != 0.0) break;

            h0 = h2
        }

        val alwaysUp = (rise != 0.0 && set != 0.0 && ye > 0.0)
        val alwaysDown = (rise != 0.0 && set != 0.0 && ye <= 0.0)

        return MoonTime(
            if (rise != 0.0) MathUtils.hoursLater(date, rise.toInt()) else date,
            if (set != 0.0) MathUtils.hoursLater(date, set.toInt()) else date,
            alwaysUp,
            alwaysDown
        )
    }

}