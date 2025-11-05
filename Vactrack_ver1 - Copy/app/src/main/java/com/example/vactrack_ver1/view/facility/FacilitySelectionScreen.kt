package com.example.vactrack_ver1.view.facility

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vactrack_ver1.R
import com.example.vactrack_ver1.ui.theme.Vactrack_ver1Theme
import java.util.Locale

/* ==== Theme tokens ==== */
private val PrimaryColor = Color(0xFF66C3DA)
private val TextPrimary = Color(0xFF111827)
private val TextSecondary = Color(0xFF6B7280)
private val ScreenBackground = Color(0xFFF4FAFC)

/* ==== Data ==== */
enum class FacilityCategory { All, Hospital, Clinic }

data class FacilityItem(
    val id: String,
    val name: String,
    val address: String,
    val type: FacilityCategory,
    val isSelected: Boolean = false,
    // ✅ Thêm mapUrl để mở đúng điểm trên Google Maps
    val mapUrl: String? = null
)

private val facilitySamples = listOf(
    FacilityItem(
        "facility_1","Bệnh viện nhân dân Gia Định",
        "1 Nơ Trang Long, Phường 7, Bình Thạnh, TP. Hồ Chí Minh",
        FacilityCategory.Hospital, true,
        mapUrl = "https://www.google.com/maps/place/B%E1%BB%87nh+vi%E1%BB%87n+Nh%C3%A2n+d%C3%A2n+Gia+%C4%90%E1%BB%8Bnh/@10.8037471,106.6915639,17z/data=!3m1!4b1!4m6!3m5!1s0x317528c663a9375f:0x342683919bcbff10!8m2!3d10.8037471!4d106.6941388!16s%2Fg%2F1td4m5v2?hl=vi&entry=ttu"
    ),
    FacilityItem(
        "facility_2","Bệnh viện Quân Y 175",
        "Nguyễn Kiệm/786, Đ. Hạnh Thông, Phường, Gò Vấp, TP. Hồ Chí Minh",
        FacilityCategory.Hospital,
        mapUrl = "https://www.google.com/maps/place/B%E1%BB%87nh+vi%E1%BB%87n+Qu%C3%A2n+Y+175/@10.8175099,106.6757906,17z/data=!3m1!4b1!4m6!3m5!1s0x317528e2324759b7:0x6c91974ff86f05e3!8m2!3d10.8175099!4d106.6806615!16s%2Fg%2F11hbnsy1s7?hl=vi&entry=ttu"
    ),
    FacilityItem(
        "facility_3","Phòng khám Đa khoa CHAC2",
        "42 Đặng Văn Bi, Phường, Thủ Đức, Thành phố Hồ Chí Minh",
        FacilityCategory.Clinic,
        mapUrl = "https://www.google.com/maps/place/PH%C3%92NG+KH%C3%81M+%C4%90A+KHOA+CHAC2/@10.841446,106.7624721,17z/data=!3m1!4b1!4m6!3m5!1s0x317527a3154f26c3:0xa1a9e078bf4dec54!8m2!3d10.841446!4d106.765047!16s%2Fg%2F11cjj1b7_n?hl=vi&entry=ttu"
    ),
    FacilityItem(
        "facility_4","Bệnh viện Ung bướu TP. HCM",
        "47 Nguyễn Huy Lượng, Phường 14, Bình Thạnh, TP. Hồ Chí Minh",
        FacilityCategory.Hospital,
        mapUrl = "https://www.google.com/maps/place/B%E1%BB%87nh+vi%E1%BB%87n+Ung+b%C6%B0%E1%BB%9Bu+TP.+HCM/@10.8049868,106.6917835,17z/data=!3m1!4b1!4m6!3m5!1s0x317528c6b111c081:0x9545c9715dfe2cd7!8m2!3d10.8049868!4d106.6943584!16s%2Fg%2F11c562nv8y?hl=vi&entry=ttu"
    ),
    FacilityItem(
        "facility_5","Phòng khám đa khoa Hàng Xanh",
        "395 Điện Biên Phủ, Thạnh Mỹ Tây, Bình Thạnh, TP. Hồ Chí Minh",
        FacilityCategory.Clinic,
        mapUrl = "https://www.google.com/maps/place/PH%C3%92NG+KH%C3%81M+%C4%90A+KHOA+H%C3%80NG+XANH/@10.8018403,106.709809,17z/data=!3m1!4b1!4m6!3m5!1s0x317528a52ee28255:0x8cfadae18d6b5dcd!8m2!3d10.8018403!4d106.7123839!16s%2Fg%2F11bccmjy4c?hl=vi&entry=ttu"
    ),
    FacilityItem(
        "facility_6","Bệnh viện Mắt TP. HCM",
        "280 Điện Biên Phủ, Phường Võ Thị Sáu, Quận 3, Thành phố Hồ Chí Minh",
        FacilityCategory.Hospital,
        mapUrl = "https://www.google.com/maps/place/B%E1%BB%87nh+vi%E1%BB%87n+M%E1%BA%AFt+TP.+HCM/@10.7783851,106.6827679,17z/data=!3m1!4b1!4m6!3m5!1s0x31752f2579d6bfe9:0x35859d11c9f06b3a!8m2!3d10.7783851!4d106.6853428!16s%2Fg%2F1hc4h557r?hl=vi&entry=ttu"
    ),
    FacilityItem(
        "facility_7","Phòng khám sản phụ khoa Tâm Phúc",
        "164 Đ. Nguyễn Xí, Phường 26, Bình Thạnh, TP. Hồ Chí Minh",
        FacilityCategory.Clinic,
        mapUrl = "https://www.google.com/maps/place/Ph%C3%B2ng+kh%C3%A1m+s%E1%BA%A3n+ph%E1%BB%A5+khoa+T%C3%A2m+Ph%C3%BAc/@10.8148807,106.7059205,17z/data=!3m1!4b1!4m6!3m5!1s0x317529cd2ba0419f:0xc720403af3e1ba7f!8m2!3d10.8148807!4d106.7084954!16s%2Fg%2F11j3t0krsp?hl=vi&entry=ttu"
    )
)

/* ==== Screen ==== */
@Composable
fun FacilitySelectionScreen(
    modifier: Modifier = Modifier,
    facilities: List<FacilityItem> = facilitySamples,
    onBackClick: () -> Unit = {},
    onDetailClick: (FacilityItem) -> Unit = {},
    onDirectionClick: (FacilityItem) -> Unit = {},
    onBookNowClick: (FacilityItem) -> Unit = {}
) {
    val context = LocalContext.current
    var searchQuery by rememberSaveable { mutableStateOf("") }
    var activeCategory by rememberSaveable { mutableStateOf(FacilityCategory.All) }
    var expandedFacilityId by rememberSaveable { mutableStateOf<String?>(null) }

    val filteredFacilities = remember(searchQuery, activeCategory, facilities) {
        val locale = Locale.getDefault()
        val q = searchQuery.lowercase(locale)
        facilities.filter { f ->
            (activeCategory == FacilityCategory.All || f.type == activeCategory) &&
                    (q.isBlank() || f.name.lowercase(locale).contains(q))
        }
    }
    if (expandedFacilityId != null && filteredFacilities.none { it.id == expandedFacilityId }) {
        expandedFacilityId = null
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ScreenBackground)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            item { FacilitySelectionHeader(onBackClick) }
            item {
                FacilitySearchBar(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    onClear = { searchQuery = "" },
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
            item {
                FacilityFilterRow(
                    selectedCategory = activeCategory,
                    onCategorySelected = { activeCategory = it },
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
            items(filteredFacilities, key = { it.id }) { facility ->
                FacilityCard(
                    facility = facility,
                    modifier = Modifier.padding(horizontal = 20.dp),
                    isExpanded = facility.id == expandedFacilityId,
                    onToggleExpand = {
                        expandedFacilityId = if (expandedFacilityId == facility.id) null else facility.id
                    },
                    onDetailClick = {
                        Toast.makeText(context, "Xem chi tiết", Toast.LENGTH_SHORT).show()
                        onDetailClick(facility)
                    },
                    // ✅ Chỉ đổi phần xử lý "Đường đi": mở Google Maps theo mapUrl (fallback ra browser)
                    onDirectionClick = {
                        val deepLink = facility.mapUrl
                            ?: "https://www.google.com/maps/search/?api=1&query=" +
                            Uri.encode("${facility.name} ${facility.address}")
                        val gmmUri = Uri.parse(deepLink)

                        val mapsIntent = Intent(Intent.ACTION_VIEW, gmmUri).apply {
                            // cố gắng mở bằng app Google Maps
                            `package` = "com.google.android.apps.maps"
                        }
                        try {
                            context.startActivity(mapsIntent)
                        } catch (_: ActivityNotFoundException) {
                            // nếu không có app Maps thì mở bằng trình duyệt
                            context.startActivity(Intent(Intent.ACTION_VIEW, gmmUri))
                        }
                        onDirectionClick(facility) // giữ callback nếu bạn muốn bắt sự kiện ở nơi khác
                    },
                    onBookNowClick = {
                        Toast.makeText(context, "Đặt khám ngay", Toast.LENGTH_SHORT).show()
                        onBookNowClick(facility)
                    }
                )
            }
        }
    }
}

/* ==== Header ==== */
@Composable
private fun FacilitySelectionHeader(onBackClick: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = PrimaryColor,
        contentColor = Color.White,
        shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
        shadowElevation = 6.dp,
        tonalElevation = 4.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 18.dp)
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Quay lại",
                    tint = Color.White
                )
            }
            Text(
                text = "Chọn cơ sở y tế",
                style = MaterialTheme.typography.titleMedium.copy(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

/* ==== Search ==== */
@Composable
private fun FacilitySearchBar(
    value: String,
    onValueChange: (String) -> Unit,
    onClear: () -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(24.dp)),
        placeholder = {
            Text("Tìm cơ sở y tế", color = TextSecondary.copy(alpha = 0.7f))
        },
        leadingIcon = {
            Icon(Icons.Filled.Search, contentDescription = null, tint = PrimaryColor)
        },
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(onClick = onClear) {
                    Icon(Icons.Filled.Clear, contentDescription = "Xóa", tint = TextSecondary)
                }
            }
        },
        singleLine = true,
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            disabledContainerColor = Color.White,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            cursorColor = PrimaryColor
        )
    )
}

/* ==== Filter (to & đều) ==== */
@Composable
private fun FacilityFilterRow(
    selectedCategory: FacilityCategory,
    onCategorySelected: (FacilityCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    val categories = listOf(FacilityCategory.All, FacilityCategory.Hospital, FacilityCategory.Clinic)

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        categories.forEach { category ->
            val isSelected = category == selectedCategory
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp),
                shape = RoundedCornerShape(16.dp),
                color = if (isSelected) PrimaryColor else Color.White,
                contentColor = if (isSelected) Color.White else PrimaryColor,
                border = if (isSelected) null else BorderStroke(1.dp, PrimaryColor),
                tonalElevation = if (isSelected) 1.dp else 0.dp,
                shadowElevation = if (isSelected) 2.dp else 0.dp
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onCategorySelected(category) },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = when (category) {
                            FacilityCategory.All -> "Tất cả"
                            FacilityCategory.Hospital -> "Bệnh viện"
                            FacilityCategory.Clinic -> "Phòng khám"
                        },
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 15.sp
                        )
                    )
                }
            }
        }
    }
}

/* ==== Card (nút lazy bằng LazyRow) ==== */
@Composable
private fun FacilityCard(
    facility: FacilityItem,
    modifier: Modifier = Modifier,
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onDetailClick: () -> Unit,
    onDirectionClick: () -> Unit,
    onBookNowClick: () -> Unit
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        tonalElevation = 2.dp,
        shadowElevation = 8.dp,
        border = BorderStroke(1.dp, PrimaryColor.copy(alpha = 0.12f)),
        onClick = onToggleExpand
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = CircleShape,
                    color = PrimaryColor.copy(alpha = 0.12f),
                    modifier = Modifier.size(60.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.img_dat_kham_co_so),
                        contentDescription = null,
                        modifier = Modifier.padding(14.dp),
                        contentScale = ContentScale.Fit
                    )
                }
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = facility.name,
                        color = TextPrimary,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.SemiBold)
                    )
                    Text(
                        text = facility.address,
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodySmall.copy(lineHeight = 18.sp),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.img_tich_xanh),
                    contentDescription = null,
                    modifier = Modifier.size(28.dp)
                )
            }

            AnimatedVisibility(
                visible = isExpanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    contentPadding = PaddingValues(horizontal = 2.dp)
                ) {
                    item {
                        OutlinedButton(
                            onClick = onDetailClick,
                            modifier = Modifier.height(44.dp),
                            shape = RoundedCornerShape(22.dp),
                            border = BorderStroke(1.2.dp, PrimaryColor.copy(alpha = 0.85f)),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = PrimaryColor,
                                containerColor = Color.Transparent
                            ),
                            contentPadding = PaddingValues(horizontal = 14.dp)
                        ) { Text("Xem chi tiết", maxLines = 1) }
                    }
                    item {
                        OutlinedButton(
                            onClick = onDirectionClick,
                            modifier = Modifier.height(44.dp),
                            shape = RoundedCornerShape(22.dp),
                            border = BorderStroke(1.2.dp, PrimaryColor.copy(alpha = 0.85f)),
                            colors = ButtonDefaults.outlinedButtonColors(
                                contentColor = PrimaryColor,
                                containerColor = Color.Transparent
                            ),
                            contentPadding = PaddingValues(horizontal = 14.dp)
                        ) { Text("Đường đi", maxLines = 1) }
                    }
                    item {
                        Button(
                            onClick = onBookNowClick,
                            modifier = Modifier.height(44.dp),
                            shape = RoundedCornerShape(22.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = PrimaryColor, contentColor = Color.White
                            ),
                            contentPadding = PaddingValues(horizontal = 16.dp)
                        ) { Text("Đặt khám ngay", fontWeight = FontWeight.SemiBold, maxLines = 1) }
                    }
                }
            }
        }
    }
}

/* ==== Preview ==== */
@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun FacilitySelectionPreview() {
    Vactrack_ver1Theme {
        FacilitySelectionScreen()
    }
}
