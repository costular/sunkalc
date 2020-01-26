package com.costular.sunkalc

import kotlin.math.abs

internal fun near(a: Double, b: Double, margin: Double = 1E-15): Boolean {
    return abs(a - b) < margin
}