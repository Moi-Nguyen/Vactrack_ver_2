package com.example.vactrack_ver1.view.onboarding

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.draw.shadow
import com.example.vactrack_ver1.R
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import com.example.vactrack_ver1.controller.OnboardingController
import com.example.vactrack_ver1.design.BrandPalette
import com.example.vactrack_ver1.model.OnboardingPage
import com.example.vactrack_ver1.ui.theme.Vactrack_ver1Theme
import com.example.vactrack_ver1.view.onboarding.components.VacTrackBackground
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged

private const val AUTO_PLAY_DELAY_MS = 2_000L

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun OnboardingScreen(
    controller: OnboardingController,
    modifier: Modifier = Modifier,
    onPrimaryAction: () -> Unit = {}
) {
    val pages = controller.pages
    val activePage by controller.activePage
    val pagerState = rememberPagerState(
        initialPage = activePage,
        pageCount = { pages.size }
    )

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .distinctUntilChanged()
            .collect { controller.onPageChanged(it) }
    }

    LaunchedEffect(activePage) {
        if (activePage != pagerState.currentPage) {
            pagerState.animateScrollToPage(activePage)
        }
        delay(AUTO_PLAY_DELAY_MS)
        controller.onNextPage(withWrap = true)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BrandPalette.MistWhite)
    ) {
        VacTrackBackground(
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.35f)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 28.dp, vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(12.dp))
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
            ) { pageIndex ->
                OnboardingSlide(
                    page = pages[pageIndex],
                    pageIndex = pageIndex
                )
            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                DotsIndicator(
                    totalDots = pages.size,
                    selectedIndex = activePage,
                    modifier = Modifier
                )

                val currentPage = pages[activePage]
                if (currentPage.buttonLabel != null) {
                    ActionButton(
                        label = currentPage.buttonLabel,
                        onClick = {
                            if (controller.isLastPage()) {
                                onPrimaryAction()
                            } else {
                                controller.onNextPage()
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun OnboardingSlide(
    page: OnboardingPage,
    pageIndex: Int,
    modifier: Modifier = Modifier
) {
    when (pageIndex) {
        0 -> WelcomeSlide(page, modifier)
        1 -> LogoSlide(page, modifier)
        else -> FinalSlide(page, modifier)
    }
}

@Composable
private fun WelcomeSlide(
    page: OnboardingPage,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(260.dp)
                .background(BrandPalette.OceanBlue.copy(alpha = 0.14f), shape = CircleShape)
                .alpha(0.8f)
        )

        Box(
            modifier = Modifier
                .shadow(elevation = 18.dp, shape = RoundedCornerShape(36.dp), clip = false)
                .background(Color.White.copy(alpha = 0.96f), shape = RoundedCornerShape(36.dp))
                .padding(horizontal = 64.dp, vertical = 28.dp)
        ) {
            Text(
                text = page.headline,
                color = BrandPalette.DeepBlue,
                style = MaterialTheme.typography.displaySmall.copy(
                    fontWeight = FontWeight.Black,
                    letterSpacing = 6.sp
                )
            )
        }
    }
}

@Composable
private fun LogoSlide(
    page: OnboardingPage,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(260.dp)
                .shadow(24.dp, shape = CircleShape, clip = false)
                .background(Color.White.copy(alpha = 0.92f), shape = CircleShape)
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.onboarding_logo),
                contentDescription = "VacTrack logo",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Fit
            )
        }
        Spacer(modifier = Modifier.height(28.dp))
        Text(
            text = page.headline,
            color = BrandPalette.DeepBlue,
            style = MaterialTheme.typography.displaySmall.copy(
                fontWeight = FontWeight.Black,
                letterSpacing = 4.sp
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        if (page.subheadline.isNotBlank()) {
            Text(
                text = page.subheadline,
                color = BrandPalette.SlateGrey,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    textAlign = TextAlign.Center
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ActionButton(
    label: String,
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(
            containerColor = BrandPalette.OceanBlue,
            contentColor = Color.White
        ),
        shape = RoundedCornerShape(20.dp),
        contentPadding = PaddingValues(vertical = 16.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        )
    }
}

@Composable
private fun FinalSlide(
    page: OnboardingPage,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp)
                .shadow(18.dp, RoundedCornerShape(32.dp), clip = false)
                .background(Color.White.copy(alpha = 0.95f), RoundedCornerShape(32.dp))
                .padding(vertical = 24.dp, horizontal = 20.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = R.drawable.onboarding_wordmark),
                contentDescription = "VacTrack wordmark",
                modifier = Modifier.fillMaxWidth(),
                contentScale = ContentScale.Fit
            )
        }
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = page.headline,
            color = BrandPalette.DeepBlue,
            style = MaterialTheme.typography.headlineLarge.copy(
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 3.sp
            ),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = page.subheadline,
            color = BrandPalette.SlateGrey,
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 18.sp,
                lineHeight = 26.sp,
                textAlign = TextAlign.Center
            ),
            textAlign = TextAlign.Center
        )
        page.body?.let {
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = it,
                color = BrandPalette.SlateGrey.copy(alpha = 0.85f),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 14.sp,
                    lineHeight = 22.sp,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.padding(horizontal = 12.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun DotsIndicator(
    totalDots: Int,
    selectedIndex: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(totalDots) { index ->
            Surface(
                modifier = Modifier.size(
                    width = if (index == selectedIndex) 26.dp else 8.dp,
                    height = 8.dp
                ),
                shape = RoundedCornerShape(12.dp),
                color = if (index == selectedIndex) BrandPalette.OceanBlue else BrandPalette.OceanBlue.copy(alpha = 0.25f)
            ) {}
        }
    }
}

@Preview(showBackground = true, device = "spec:width=412dp,height=892dp")
@Composable
private fun OnboardingScreenPreview() {
    Vactrack_ver1Theme(darkTheme = false) {
        OnboardingScreen(controller = OnboardingController())
    }
}
