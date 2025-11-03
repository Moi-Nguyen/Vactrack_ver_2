package com.example.vactrack_ver1.view.phieu_kham

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import com.example.vactrack_ver1.R
import com.example.vactrack_ver1.design.BrandPalette
import com.example.vactrack_ver1.ui.theme.Vactrack_ver1Theme
import com.example.vactrack_ver1.view.components.MainBottomNavItem
import com.example.vactrack_ver1.view.components.MainBottomNavigationBar

@Composable
fun TicketListScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onTicketClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onAccountClick: () -> Unit = {}
) {
    var selectedTab by rememberSaveable { mutableStateOf(TicketFilter.Paid) }

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
                            text = "Danh sách phiếu khám",
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
                    activeItem = MainBottomNavItem.Ticket,
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
                TicketFilterTabs(
                    selected = selectedTab,
                    onSelect = { selectedTab = it }
                )
                Spacer(modifier = Modifier.height(24.dp))
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
                            painter = painterResource(id = R.drawable.img_phieu_kham_trong),
                            contentDescription = "Không có phiếu khám",
                            modifier = Modifier.size(160.dp),
                            colorFilter = ColorFilter.tint(BrandPalette.OceanBlue)
                        )
                        Text(
                            text = "Bạn chưa có phiếu khám nào",
                            color = BrandPalette.OceanBlue,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.SemiBold,
                                fontSize = 18.sp
                            ),
                            textAlign = TextAlign.Center
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

private enum class TicketFilter {
    Paid, Unpaid, Completed
}

@Composable
private fun TicketFilterTabs(
    selected: TicketFilter,
    onSelect: (TicketFilter) -> Unit
) {
    val tabs = listOf(
        TicketFilter.Paid to "Đã thanh toán",
        TicketFilter.Unpaid to "Chưa thanh toán",
        TicketFilter.Completed to "Đã khám"
    )

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(horizontal = 4.dp)
    ) {
        items(tabs) { (filter, label) ->
            val isSelected = filter == selected
            Surface(
                shape = RoundedCornerShape(28.dp),
                color = if (isSelected) BrandPalette.OceanBlue else Color.Transparent,
                border = if (isSelected) null else BorderStroke(
                    width = 1.5.dp,
                    color = BrandPalette.OceanBlue.copy(alpha = 0.5f)
                ),
                tonalElevation = 0.dp,
                shadowElevation = 0.dp,
                onClick = { onSelect(filter) }
            ) {
                Text(
                    text = label,
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 10.dp),
                    color = if (isSelected) Color.White else BrandPalette.OceanBlue,
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp, fontWeight = FontWeight.Medium)
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun TicketListPreview() {
    Vactrack_ver1Theme {
        TicketListScreen()
    }
}
