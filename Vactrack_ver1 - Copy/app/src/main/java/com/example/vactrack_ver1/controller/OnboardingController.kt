package com.example.vactrack_ver1.controller

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableIntStateOf
import com.example.vactrack_ver1.design.BrandPalette
import com.example.vactrack_ver1.model.OnboardingPage

class OnboardingController {

    private val _activePage = mutableIntStateOf(0)
    val activePage: State<Int> = _activePage

    val pages: List<OnboardingPage> = listOf(
        OnboardingPage(
            headline = "WELCOME",
            subheadline = "",
            body = null,
            buttonLabel = null,
            accentColor = BrandPalette.OceanBlue
        ),
        OnboardingPage(
            headline = "VACTRACK",
            subheadline = "Trusted Protection Worldwide",
            body = null,
            buttonLabel = null,
            accentColor = BrandPalette.DeepBlue
        ),
        OnboardingPage(
            headline = "VAC TRACK",
            subheadline = "Your Healthcare Journey Starts Here",
            body = "Track vaccinations, receive reminders, and keep your loved ones protected.",
            buttonLabel = "Get Started",
            accentColor = BrandPalette.OceanBlue
        )
    )

    fun onPageChanged(index: Int) {
        _activePage.intValue = index
    }

    fun onNextPage(withWrap: Boolean = false) {
        val nextIndex = _activePage.intValue + 1
        _activePage.intValue = when {
            nextIndex > pages.lastIndex && withWrap -> 0
            nextIndex > pages.lastIndex -> pages.lastIndex
            else -> nextIndex
        }
    }

    fun restart() {
        _activePage.intValue = 0
    }

    fun isLastPage(index: Int = _activePage.intValue): Boolean = index == pages.lastIndex
}
