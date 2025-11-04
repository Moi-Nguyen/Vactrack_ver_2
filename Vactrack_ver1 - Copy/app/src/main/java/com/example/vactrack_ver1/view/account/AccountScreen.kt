package com.example.vactrack_ver1.view.account

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.tooling.preview.Preview
import com.example.vactrack_ver1.R
import com.example.vactrack_ver1.design.BrandPalette
import com.example.vactrack_ver1.ui.theme.Vactrack_ver1Theme
import com.example.vactrack_ver1.view.components.MainBottomNavItem
import com.example.vactrack_ver1.view.components.MainBottomNavigationBar

@Composable
fun AccountScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onTicketClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onAccountClick: () -> Unit = {},
    onLogout: () -> Unit = {}
) {
    var showLogoutConfirm by rememberSaveable { mutableStateOf(false) }

    val entries = listOf(
        "Quy định sử dụng",
        "Chính sách bảo mật",
        "Điều khoản dịch vụ",
        "Xem thông tin sức khoẻ",
        "Đánh giá ứng dụng",
        "Chia sẻ ứng dụng",
        "Một số câu hỏi thường gặp"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(color = Color(0xFFF2F8FF))
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = 20.dp,
                end = 20.dp,
                top = 220.dp,
                bottom = 140.dp
            ),
            verticalArrangement = Arrangement.spacedBy(28.dp)
        ) {
            item {
                AccountCard(entries = entries)
            }
            item {
                LogoutActionButton(onClick = { showLogoutConfirm = true })
            }
        }

        HeaderSection(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth()
                .height(220.dp),
            onBackClick = onBackClick,
            onLogoutClick = { showLogoutConfirm = true }
        )

        MainBottomNavigationBar(
            activeItem = MainBottomNavItem.Account,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 12.dp),
            onHomeClick = onHomeClick,
            onProfileClick = onProfileClick,
            onTicketClick = onTicketClick,
            onNotificationClick = onNotificationClick,
            onAccountClick = onAccountClick
        )
    }

    if (showLogoutConfirm) {
        LogoutConfirmDialog(
            onDismiss = { showLogoutConfirm = false },
            onConfirm = {
                showLogoutConfirm = false
                onLogout()
            }
        )
    }
}

@Composable
private fun HeaderSection(
    modifier: Modifier,
    onBackClick: () -> Unit,
    onLogoutClick: () -> Unit
) {
    Surface(
        modifier = modifier,
        color = BrandPalette.OceanBlue,
        shape = RoundedCornerShape(bottomStart = 40.dp, bottomEnd = 40.dp),
        shadowElevation = 12.dp,
        tonalElevation = 6.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp, vertical = 20.dp)
        ) {
            androidx.compose.material3.IconButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.TopStart)
            ) {
                androidx.compose.material3.Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Quay lại",
                    tint = Color.White
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(top = 8.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Surface(
                    shape = CircleShape,
                    color = Color.White,
                    shadowElevation = 0.dp
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_tai_khoan),
                        contentDescription = "Ảnh đại diện",
                        modifier = Modifier
                            .size(110.dp)
                            .clip(CircleShape)
                    )
                }
                Text(
                    text = "Nhóm1",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                )
                Surface(
                    onClick = onLogoutClick,
                    shape = RoundedCornerShape(22.dp),
                    color = Color.White.copy(alpha = 0.24f),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.45f))
                ) {
                    Text(
                        text = "Đăng xuất",
                        color = Color.White,
                        modifier = Modifier.padding(horizontal = 26.dp, vertical = 8.dp),
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
                    )
                }
            }
        }
    }
}

@Composable
private fun AccountCard(entries: List<String>) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = Color.White,
        shadowElevation = 20.dp,
        tonalElevation = 6.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp, vertical = 26.dp),
            verticalArrangement = Arrangement.spacedBy(22.dp)
        ) {
            Text(
                text = "Điều khoản và quy định",
                color = BrandPalette.OceanBlue,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                entries.forEach { item ->
                    AccountItemRow(title = item)
                }
            }
        }
    }
}

@Composable
private fun AccountItemRow(title: String) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color(0xFFF8FBFF),
        border = BorderStroke(1.2.dp, BrandPalette.OceanBlue.copy(alpha = 0.35f)),
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = title,
                color = BrandPalette.DeepBlue,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Medium
                ),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            androidx.compose.material3.Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = BrandPalette.OceanBlue
            )
        }
    }
}

@Composable
private fun LogoutActionButton(onClick: () -> Unit) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        shape = RoundedCornerShape(28.dp),
        border = BorderStroke(1.5.dp, Color(0xFFD32F2F)),
        colors = ButtonDefaults.outlinedButtonColors(
            containerColor = Color.White,
            contentColor = Color(0xFFD32F2F)
        )
    ) {
        Text(
            text = "ĐĂNG XUẤT",
            style = MaterialTheme.typography.labelLarge.copy(
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        )
    }
}

@Composable
private fun LogoutConfirmDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(26.dp),
            color = Color.White,
            tonalElevation = 8.dp,
            shadowElevation = 14.dp
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 26.dp),
                verticalArrangement = Arrangement.spacedBy(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "XÁC NHẬN",
                    color = BrandPalette.OceanBlue,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold
                    ),
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Bạn có chắc chắn muốn đăng xuất?",
                    color = BrandPalette.DeepBlue,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 15.sp),
                    textAlign = TextAlign.Center
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(22.dp),
                        border = BorderStroke(1.4.dp, BrandPalette.OceanBlue),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = BrandPalette.OceanBlue)
                    ) {
                        Text("Quay lại")
                    }
                    Button(
                        onClick = onConfirm,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(22.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BrandPalette.OceanBlue,
                            contentColor = Color.White
                        )
                    ) {
                        Text("Đồng ý")
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun AccountScreenPreview() {
    Vactrack_ver1Theme { AccountScreen() }
}
