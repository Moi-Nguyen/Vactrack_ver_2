package com.example.vactrack_ver1.view.account

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vactrack_ver1.R
import com.example.vactrack_ver1.design.BrandPalette
import com.example.vactrack_ver1.ui.theme.Vactrack_ver1Theme
import com.example.vactrack_ver1.view.components.MainBottomNavItem
import com.example.vactrack_ver1.view.components.MainBottomNavigationBar
import androidx.compose.ui.tooling.preview.Preview
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

    val sectionTitle = "Điều khoản và quy định"
    val menuItems = listOf(
        "Quy định sử dụng",
        "Chính sách bảo mật",
        "Điều khoản dịch vụ",
        "Xem thông tin sức khoẻ",
        "Đánh giá ứng dụng",
        "Chia sẻ ứng dụng",
        "Một số câu hỏi thường gặp"
    )

    Surface(modifier = modifier.fillMaxSize(), color = Color(0xFFF2F8FF)) {
        Column(modifier = Modifier.fillMaxSize()) {
            ProfileHeader(
                onBackClick = onBackClick,
                onLogout = { showLogoutConfirm = true }
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 24.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                item {
                    Text(
                        text = sectionTitle,
                        color = BrandPalette.OceanBlue,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    )
                }
                items(menuItems) { title ->
                    AccountMenuRow(title = title)
                }
                item {
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedButton(
                        onClick = { showLogoutConfirm = true },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(28.dp),
                        border = BorderStroke(1.5.dp, Color(0xFFD32F2F)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFD32F2F))
                    ) {
                        Text(
                            text = "ĐĂNG XUẤT",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        )
                    }
                }
                item { Spacer(modifier = Modifier.height(12.dp)) }
            }

            MainBottomNavigationBar(
                activeItem = MainBottomNavItem.Account,
                modifier = Modifier.padding(bottom = 12.dp),
                onHomeClick = onHomeClick,
                onProfileClick = onProfileClick,
                onTicketClick = onTicketClick,
                onNotificationClick = onNotificationClick,
                onAccountClick = onAccountClick
            )
        }
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
private fun ProfileHeader(
    onBackClick: () -> Unit,
    onLogout: () -> Unit
) {
    Surface(
        color = BrandPalette.OceanBlue,
        shape = RoundedCornerShape(bottomStart = 36.dp, bottomEnd = 36.dp),
        shadowElevation = 6.dp,
        tonalElevation = 6.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            IconButton(onClick = onBackClick, modifier = Modifier.align(Alignment.TopStart)) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Quay lại",
                    tint = Color.White
                )
            }

            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(top = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.img_tai_khoan),
                    contentDescription = "Ảnh đại diện",
                    modifier = Modifier
                        .size(96.dp)
                        .clip(CircleShape)
                )
                Text(
                    text = "Nhóm1",
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                )
                Surface(
                    onClick = onLogout,
                    shape = RoundedCornerShape(20.dp),
                    color = Color.White,
                    shadowElevation = 0.dp,
                    tonalElevation = 0.dp,
                    border = BorderStroke(0.dp, Color.Transparent)
                ) {
                    Text(
                        text = "Đăng xuất",
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 8.dp),
                        color = BrandPalette.OceanBlue,
                        style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Medium)
                    )
                }
            }
        }
    }
}

@Composable
private fun AccountMenuRow(title: String) {
    Surface(
        onClick = { },
        shape = RoundedCornerShape(18.dp),
        border = BorderStroke(1.2.dp, BrandPalette.OceanBlue.copy(alpha = 0.3f)),
        color = Color.White,
        shadowElevation = 0.dp,
        tonalElevation = 0.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 18.dp, vertical = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = title,
                color = BrandPalette.DeepBlue,
                style = MaterialTheme.typography.bodyLarge.copy(fontSize = 15.sp)
            )
            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                contentDescription = null,
                tint = BrandPalette.OceanBlue
            )
        }
    }
}

@Composable
private fun LogoutConfirmDialog(
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "XÁC NHẬN",
                color = BrandPalette.OceanBlue,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 20.sp
                ),
                textAlign = TextAlign.Center
            )
        },
        text = {
            Text(
                text = "Bạn có chắc chắn muốn đăng xuất?",
                color = BrandPalette.DeepBlue,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 15.sp),
                textAlign = TextAlign.Center
            )
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandPalette.OceanBlue,
                    contentColor = Color.White
                )
            ) {
                Text("Đồng ý")
            }
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.5.dp, BrandPalette.OceanBlue),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = BrandPalette.OceanBlue)
            ) {
                Text("Quay lại")
            }
        },
        shape = RoundedCornerShape(24.dp),
        containerColor = Color.White,
        tonalElevation = 6.dp
    )
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun AccountScreenPreview() {
    Vactrack_ver1Theme {
        AccountScreen()
    }
}
