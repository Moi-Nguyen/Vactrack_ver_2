package com.example.vactrack_ver1.view.notification

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.NotificationsNone
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vactrack_ver1.R
import com.example.vactrack_ver1.design.BrandPalette
import com.example.vactrack_ver1.view.components.MainBottomNavItem
import com.example.vactrack_ver1.view.components.MainBottomNavigationBar
import com.example.vactrack_ver1.ui.theme.Vactrack_ver1Theme

@Composable
fun NotificationListScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onTicketClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onAccountClick: () -> Unit = {}
) {
    var selectedTab by rememberSaveable { mutableStateOf(NotificationTab.Ticket) }

    Surface(modifier = modifier.fillMaxSize(), color = Color(0xFFF2F8FF)) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                Surface(color = BrandPalette.OceanBlue, shadowElevation = 6.dp) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        androidx.compose.material3.IconButton(onClick = onBackClick) {
                            androidx.compose.material3.Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Quay lại",
                                tint = Color.White
                            )
                        }
                        Text(
                            text = "Danh sách thông báo",
                            modifier = Modifier
                                .weight(1f)
                                .padding(top = 6.dp),
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.width(40.dp))
                    }
                }
            },
            bottomBar = {
                MainBottomNavigationBar(
                    activeItem = MainBottomNavItem.Notification,
                    onHomeClick = onHomeClick,
                    onProfileClick = onProfileClick,
                    onTicketClick = onTicketClick,
                    onNotificationClick = onNotificationClick,
                    onAccountClick = onAccountClick
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {
                Spacer(modifier = Modifier.height(24.dp))
                NotificationFilterTabs(
                    selected = selectedTab,
                    onSelect = { selectedTab = it }
                )
                Spacer(modifier = Modifier.height(24.dp))

                when (selectedTab) {
                    NotificationTab.Ticket -> EmptyNotificationState(
                        message = "Bạn chưa có thông báo nào"
                    )

                    NotificationTab.News -> NotificationGroupList(groups = newsNotifications)

                    NotificationTab.Alert -> NotificationGroupList(groups = alertNotifications)
                }

                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

private enum class NotificationTab(val label: String, val count: Int) {
    Ticket("Phiếu khám", 0),
    News("Tin tức", 6),
    Alert("Thông báo", 3)
}

private data class NotificationEntry(
    val title: String,
    val description: String,
    val timeLabel: String
)

private data class NotificationGroup(
    val title: String,
    val items: List<NotificationEntry>
)

private val newsNotifications = listOf(
    NotificationGroup(
        title = "Hôm nay",
        items = listOf(
            NotificationEntry(
                title = "Bạn cần tư vấn tâm lý?",
                description = "Bạn ở xa mà không thể khám trực tiếp? Đừng lo vì sẽ có các bác sĩ giỏi khám online.",
                timeLabel = "Hôm nay"
            ),
            NotificationEntry(
                title = "Trời mưa vẫn gặp được bác sĩ?",
                description = "Hãy dùng ngay tính năng gọi video với bác sĩ có trên ứng dụng này.",
                timeLabel = "Hôm nay"
            ),
            NotificationEntry(
                title = "Bạn quan tâm đến việc lấy vé sớm?",
                description = "Hãy đặt lịch trước với ứng dụng để có thể nhận được khung giờ khám thích hợp.",
                timeLabel = "Hôm nay"
            )
        )
    ),
    NotificationGroup(
        title = "Trước đó",
        items = listOf(
            NotificationEntry(
                title = "Bạn cần tư vấn tâm lý?",
                description = "Bạn ở xa mà không thể khám trực tiếp? Đừng lo vì sẽ có các bác sĩ giỏi khám online.",
                timeLabel = "1 ngày trước"
            ),
            NotificationEntry(
                title = "Trời mưa vẫn gặp được bác sĩ?",
                description = "Hãy dùng ngay tính năng gọi video với bác sĩ có trên ứng dụng này.",
                timeLabel = "2 ngày trước"
            ),
            NotificationEntry(
                title = "Bạn quan tâm đến việc lấy vé sớm?",
                description = "Hãy đặt lịch trước với ứng dụng để có thể nhận được khung giờ khám thích hợp.",
                timeLabel = "3 ngày trước"
            )
        )
    )
)

private val alertNotifications = listOf(
    NotificationGroup(
        title = "Hôm nay",
        items = emptyList()
    ),
    NotificationGroup(
        title = "Trước đó",
        items = listOf(
            NotificationEntry(
                title = "Bạn cần tư vấn tâm lý?",
                description = "Bạn ở xa mà không thể khám trực tiếp? Đừng lo vì sẽ có các bác sĩ giỏi khám online.",
                timeLabel = "1 ngày trước"
            ),
            NotificationEntry(
                title = "Trời mưa vẫn gặp được bác sĩ?",
                description = "Hãy dùng ngay tính năng gọi video với bác sĩ có trên ứng dụng này.",
                timeLabel = "2 ngày trước"
            ),
            NotificationEntry(
                title = "Bạn quan tâm đến việc lấy vé sớm?",
                description = "Hãy đặt lịch trước với ứng dụng để có thể nhận được khung giờ khám thích hợp.",
                timeLabel = "3 ngày trước"
            )
        )
    )
)

@Composable
private fun NotificationFilterTabs(
    selected: NotificationTab,
    onSelect: (NotificationTab) -> Unit
) {
    val tabs = enumValues<NotificationTab>().asList()

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        items(tabs) { tab ->
            val isSelected = tab == selected
            Surface(
                shape = RoundedCornerShape(28.dp),
                color = if (isSelected) BrandPalette.OceanBlue else Color.Transparent,
                border = if (isSelected) null else BorderStroke(
                    width = 1.5.dp,
                    color = BrandPalette.OceanBlue.copy(alpha = 0.5f)
                ),
                tonalElevation = 0.dp,
                shadowElevation = 0.dp,
                onClick = { onSelect(tab) }
            ) {
                Text(
                    text = "${tab.label} (${tab.count})",
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp),
                    color = if (isSelected) Color.White else BrandPalette.OceanBlue,
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp, fontWeight = FontWeight.Medium)
                )
            }
        }
    }
}

@Composable
private fun ColumnScope.EmptyNotificationState(message: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_thong_bao_2),
                contentDescription = null,
                modifier = Modifier.size(160.dp),
                colorFilter = ColorFilter.tint(BrandPalette.OceanBlue)
            )
            Text(
                text = message,
                color = BrandPalette.OceanBlue,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp
                ),
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun ColumnScope.NotificationGroupList(groups: List<NotificationGroup>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .weight(1f),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        items(groups) { group ->
            Column(
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Text(
                    text = group.title,
                    color = BrandPalette.DeepBlue,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                )

                if (group.items.isEmpty()) {
                    Text(
                        text = "Không có thông báo mới",
                        color = BrandPalette.SlateGrey,
                        style = MaterialTheme.typography.bodyMedium
                    )
                } else {
                    group.items.forEach { entry ->
                        NotificationCard(entry = entry)
                    }
                }
            }
        }
    }
}

@Composable
private fun NotificationCard(entry: NotificationEntry) {
    Surface(
        shape = RoundedCornerShape(18.dp),
        color = Color.White,
        border = BorderStroke(1.5.dp, BrandPalette.OceanBlue.copy(alpha = 0.4f)),
        tonalElevation = 0.dp,
        shadowElevation = 2.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            androidx.compose.material3.Icon(
                imageVector = Icons.Outlined.NotificationsNone,
                contentDescription = null,
                tint = BrandPalette.OceanBlue,
                modifier = Modifier.size(48.dp)
            )
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Text(
                    text = entry.title,
                    color = BrandPalette.DeepBlue,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                )
                Text(
                    text = entry.description,
                    color = BrandPalette.SlateGrey,
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp)
                )
                Text(
                    text = entry.timeLabel,
                    color = BrandPalette.OceanBlue,
                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Medium)
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun NotificationListPreview() {
    Vactrack_ver1Theme {
        NotificationListScreen()
    }
}
