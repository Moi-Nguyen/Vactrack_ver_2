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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.vactrack_ver1.controller.Ticket
import com.example.vactrack_ver1.controller.TicketController
import com.example.vactrack_ver1.controller.TicketStatus
import com.example.vactrack_ver1.design.BrandPalette
import com.example.vactrack_ver1.ui.theme.Vactrack_ver1Theme
import com.example.vactrack_ver1.view.components.MainBottomNavItem
import com.example.vactrack_ver1.view.components.MainBottomNavigationBar
import java.text.NumberFormat
import java.util.Locale

/* ==== Theme tokens (matching booking screens) ==== */
private val PrimaryBlue = Color(0xFF5BB7CF)
private val TextPrimary = Color(0xFF1F2937)
private val TextSecondary = Color(0xFF6B7280)

/* ==== Helper function to format currency ==== */
private fun formatCurrency(amount: Long): String {
    val locale = Locale.forLanguageTag("vi-VN")
    val formatter = NumberFormat.getNumberInstance(locale)
    return "${formatter.format(amount)}đ"
}

@Composable
fun TicketListScreen(
    modifier: Modifier = Modifier,
    initialTab: TicketFilter = TicketFilter.Paid,
    onBackClick: () -> Unit = {},
    onHomeClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    onTicketClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onAccountClick: () -> Unit = {}
) {
    var selectedTab by rememberSaveable { mutableStateOf(initialTab) }
    val allTickets = TicketController.tickets
    
    // Filter tickets based on selected tab
    val filteredTickets = when (selectedTab) {
        TicketFilter.Paid -> allTickets.filter { it.status == TicketStatus.PAID }
        TicketFilter.Unpaid -> allTickets.filter { it.status == TicketStatus.PENDING }
        TicketFilter.Completed -> allTickets.filter { it.status == TicketStatus.PAID } // For now, same as paid
    }

    Surface(modifier = modifier.fillMaxSize(), color = Color(0xFFF2F8FF)) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                // FIXED: Added 20.dp top padding to move the top bar down from status bar
                Surface(
                    color = BrandPalette.OceanBlue,
                    shadowElevation = 6.dp,
                    modifier = Modifier.padding(top = 20.dp) // 20dp spacing from top edge
                ) {
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
                
                // Display tickets or empty state
                if (filteredTickets.isEmpty()) {
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
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        contentPadding = PaddingValues(bottom = 16.dp)
                    ) {
                        items(filteredTickets) { ticket ->
                            TicketCard(ticket = ticket)
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

enum class TicketFilter {
    Paid, Unpaid, Completed
}

@Composable
private fun TicketCard(ticket: Ticket) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Top row: hospital + status
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = ticket.hospitalName,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    ),
                    color = PrimaryBlue,
                    modifier = Modifier.weight(1f)
                )
                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFDCFCE7)
                ) {
                    Text(
                        text = when (ticket.status) {
                            TicketStatus.PAID -> "ĐÃ THANH TOÁN"
                            TicketStatus.PENDING -> "CHỜ THANH TOÁN"
                            TicketStatus.CANCELLED -> "ĐÃ HỦY"
                        },
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        ),
                        color = Color(0xFF16A34A),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Text(
                text = ticket.hospitalAddress,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp),
                color = TextSecondary,
                lineHeight = 18.sp
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Patient info
            Text(
                text = "Bệnh nhân: ${ticket.patientName}",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold
                ),
                color = TextPrimary
            )

            // Specialty
            Text(
                text = "Chuyên khoa: ${ticket.specialtyName}",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp),
                color = TextSecondary
            )

            // Service
            Text(
                text = "Dịch vụ: ${ticket.serviceName}",
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp),
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(4.dp))

            // Date and time
            Text(
                text = "Thời gian: ${ticket.visitDate} (${ticket.visitTime})",
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                ),
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Fee row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Tiền khám",
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                    color = TextSecondary
                )
                Text(
                    text = formatCurrency(ticket.fee),
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    color = PrimaryBlue
                )
            }
        }
    }
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
