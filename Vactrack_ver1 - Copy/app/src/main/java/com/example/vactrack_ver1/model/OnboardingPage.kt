package com.example.vactrack_ver1.model

import androidx.annotation.DrawableRes
import androidx.compose.ui.graphics.Color

/**
 * Represents a single step in the onboarding carousel.
 */
data class OnboardingPage(
    val headline: String,
    val subheadline: String,
    val body: String? = null,
    val buttonLabel: String?,
    val accentColor: Color,
    @DrawableRes val badgeIcon: Int? = null
)
