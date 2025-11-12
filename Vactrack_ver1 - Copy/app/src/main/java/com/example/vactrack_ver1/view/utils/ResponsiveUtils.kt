package com.example.vactrack_ver1.view.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Responsive utilities for adaptive layouts across different screen sizes
 */

enum class WindowSize {
    COMPACT,  // Phones in portrait (width < 600dp)
    MEDIUM,   // Phones in landscape, small tablets (600dp <= width < 840dp)
    EXPANDED  // Large tablets, desktops (width >= 840dp)
}

@Composable
fun getWindowSize(): WindowSize {
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    
    return when {
        screenWidth < 600.dp -> WindowSize.COMPACT
        screenWidth < 840.dp -> WindowSize.MEDIUM
        else -> WindowSize.EXPANDED
    }
}

@Composable
fun getScreenWidthDp(): Dp {
    val configuration = LocalConfiguration.current
    return configuration.screenWidthDp.dp
}

@Composable
fun getScreenHeightDp(): Dp {
    val configuration = LocalConfiguration.current
    return configuration.screenHeightDp.dp
}

/**
 * Responsive padding based on screen size
 */
@Composable
fun responsiveHorizontalPadding(): Dp {
    return when (getWindowSize()) {
        WindowSize.COMPACT -> 20.dp
        WindowSize.MEDIUM -> 32.dp
        WindowSize.EXPANDED -> 48.dp
    }
}

@Composable
fun responsiveVerticalPadding(): Dp {
    return when (getWindowSize()) {
        WindowSize.COMPACT -> 16.dp
        WindowSize.MEDIUM -> 24.dp
        WindowSize.EXPANDED -> 32.dp
    }
}

/**
 * Responsive spacing for consistent gaps between elements
 */
@Composable
fun responsiveSpacingSmall(): Dp {
    return when (getWindowSize()) {
        WindowSize.COMPACT -> 8.dp
        WindowSize.MEDIUM -> 12.dp
        WindowSize.EXPANDED -> 16.dp
    }
}

@Composable
fun responsiveSpacingMedium(): Dp {
    return when (getWindowSize()) {
        WindowSize.COMPACT -> 16.dp
        WindowSize.MEDIUM -> 20.dp
        WindowSize.EXPANDED -> 24.dp
    }
}

@Composable
fun responsiveSpacingLarge(): Dp {
    return when (getWindowSize()) {
        WindowSize.COMPACT -> 24.dp
        WindowSize.MEDIUM -> 32.dp
        WindowSize.EXPANDED -> 40.dp
    }
}

/**
 * Responsive grid columns
 */
@Composable
fun responsiveGridColumns(compactColumns: Int): Int {
    return when (getWindowSize()) {
        WindowSize.COMPACT -> compactColumns
        WindowSize.MEDIUM -> (compactColumns * 1.5f).toInt()
        WindowSize.EXPANDED -> compactColumns * 2
    }
}

/**
 * Max content width for large screens (prevents overly wide content)
 */
@Composable
fun getMaxContentWidth(): Dp {
    return when (getWindowSize()) {
        WindowSize.COMPACT -> Dp.Infinity
        WindowSize.MEDIUM -> 720.dp
        WindowSize.EXPANDED -> 960.dp
    }
}

/**
 * Responsive card width
 */
@Composable
fun responsiveCardWidth(): Dp {
    val screenWidth = getScreenWidthDp()
    return when (getWindowSize()) {
        WindowSize.COMPACT -> screenWidth * 0.85f
        WindowSize.MEDIUM -> screenWidth * 0.70f
        WindowSize.EXPANDED -> 480.dp
    }
}

/**
 * Check if screen is in landscape mode
 */
@Composable
fun isLandscape(): Boolean {
    val configuration = LocalConfiguration.current
    return configuration.screenWidthDp > configuration.screenHeightDp
}

/**
 * Responsive icon size
 */
@Composable
fun responsiveIconSize(baseSize: Dp = 24.dp): Dp {
    return when (getWindowSize()) {
        WindowSize.COMPACT -> baseSize
        WindowSize.MEDIUM -> baseSize * 1.2f
        WindowSize.EXPANDED -> baseSize * 1.4f
    }
}
