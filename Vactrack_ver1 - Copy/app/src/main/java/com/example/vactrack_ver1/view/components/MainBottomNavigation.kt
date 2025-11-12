package com.example.vactrack_ver1.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.vactrack_ver1.R
import com.example.vactrack_ver1.design.BrandPalette

enum class MainBottomNavItem {
    Home, Profile, Ticket, Notification, Account
}

@Composable
fun MainBottomNavigationBar(
    activeItem: MainBottomNavItem,
    modifier: Modifier = Modifier,
    onHomeClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onTicketClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onAccountClick: () -> Unit = {}
) {
    val items = listOf(
        NavItemConfig(
            type = MainBottomNavItem.Home,
            title = "Trang chủ",
            iconRes = R.drawable.img_trang_chu,
            onClick = onHomeClick
        ),
        NavItemConfig(
            type = MainBottomNavItem.Profile,
            title = "Hồ sơ",
            iconRes = R.drawable.img_ho_so,
            onClick = onProfileClick
        ),
        NavItemConfig(
            type = MainBottomNavItem.Ticket,
            title = "Phiếu khám",
            iconRes = R.drawable.img_phieu_kham,
            onClick = onTicketClick
        ),
        NavItemConfig(
            type = MainBottomNavItem.Notification,
            title = "Thông báo",
            iconRes = R.drawable.img_thong_bao,
            onClick = onNotificationClick
        ),
        NavItemConfig(
            type = MainBottomNavItem.Account,
            title = "Tài khoản",
            iconRes = R.drawable.img_tai_khoan,
            onClick = onAccountClick
        )
    )

    Surface(
        shape = RoundedCornerShape(28.dp),
        color = Color.White,
        tonalElevation = 6.dp,
        shadowElevation = 6.dp,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(0.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                MainBottomNavButton(
                    title = item.title,
                    iconRes = item.iconRes,
                    active = activeItem == item.type,
                    modifier = Modifier.weight(1f),
                    onClick = item.onClick
                )
            }
        }
    }
}

private data class NavItemConfig(
    val type: MainBottomNavItem,
    val title: String,
    val iconRes: Int,
    val onClick: () -> Unit
)

@Composable
private fun MainBottomNavButton(
    title: String,
    iconRes: Int,
    active: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val iconColor = if (active) BrandPalette.OceanBlue else BrandPalette.SlateGrey.copy(alpha = 0.6f)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 8.dp), // FIXED: Added consistent vertical padding for all items
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        // FIXED: Removed Surface wrapper to ensure consistent icon alignment
        // All icons now use the same size (24.dp) and contentScale for perfect alignment
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = title,
            modifier = Modifier
                .size(24.dp) // FIXED: Standardized icon size to 24.dp for all items including "Phiếu khám"
                .padding(2.dp), // FIXED: Added inner padding for balanced spacing
            contentScale = ContentScale.Fit,
            colorFilter = ColorFilter.tint(iconColor)
        )
        
        Text(
            text = title,
            color = iconColor,
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium),
            textAlign = TextAlign.Center, // FIXED: Center-align text for consistent label positioning
            maxLines = 1 // FIXED: Prevent label wrapping that could cause misalignment
        )
    }
}
