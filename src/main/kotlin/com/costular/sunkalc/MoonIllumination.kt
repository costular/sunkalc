package com.costular.sunkalc

/**
 * Represents the moon illumination
 * @property fraction illuminated fraction of the moon; varies from 0.0 (new moon) to 1.0 (full moon)
 * @property phase moon phase; varies from 0.0 to 1.0, described below
 * @property angle midpoint angle in radians of the illuminated limb of the moon reckoned eastward from the north point of the disk; the moon is waxing if the angle is negative, and waning if positive
 */
data class MoonIllumination(
    val fraction: Float,
    val phase: Float,
    val angle: Double
)