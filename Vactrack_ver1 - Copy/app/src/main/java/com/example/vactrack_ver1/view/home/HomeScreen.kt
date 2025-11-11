package com.example.vactrack_ver1.view.home

import android.content.res.Resources
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarHalf
import androidx.compose.material.icons.outlined.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vactrack_ver1.R
import com.example.vactrack_ver1.controller.PatientController
import com.example.vactrack_ver1.design.BrandPalette
import com.example.vactrack_ver1.ui.theme.Vactrack_ver1Theme
import com.example.vactrack_ver1.view.components.MainBottomNavItem
import com.example.vactrack_ver1.view.components.MainBottomNavigationBar
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min

private const val LOGO_LOG_TAG = "LogoLoad"

/* ===================== DATA ===================== */

data class QuickAction(val id: String, val title: String, val icon: Int)

/** Logo dùng tên drawable (không kèm .png) + rating để render sao */
data class HospitalHighlight(
    val id: String,
    val name: String,
    val address: String,
    val logoName: String,
    val rating: Float
)

data class DoctorHighlight(val name: String, val speciality: String, val fee: String)
data class PatientRecord(val name: String, val phone: String, val birthDate: String, val address: String)

private val quickActionsSample = listOf(
    QuickAction("facility_booking", "Đặt lịch khám tại cơ sở", R.drawable.img_dat_kham_co_so),
    QuickAction("specialist_booking", "Đặt lịch khám chuyên khoa", R.drawable.img_dat_kham_chuyen_khoa),
    QuickAction("video_call", "Gọi video với bác sĩ", R.drawable.img_goi_video),
    QuickAction("health_package", "Gói sức khỏe toàn diện", R.drawable.img_goi_kham_suc_khoe_toan_dien),
    QuickAction("bill_payment", "Thanh toán viện phí", R.drawable.img_thanh_toan_vien_phi),
    QuickAction("doctor_booking", "Đặt khám bác sĩ", R.drawable.img_dat_kham_bac_si),
    QuickAction("result_lookup", "Tra cứu kết quả khám bệnh", R.drawable.img_tra_cuu_ket_qua_kham_benh),
    QuickAction("hotline_booking", "Đặt khám 1900-2115", R.drawable.img_dat_kham_1900_2115)
)

/* Dùng đúng logo badge_* bạn đang có trong res/drawable */
private val hospitalsSample = listOf(
    HospitalHighlight("gia_dinh",   "Bệnh viện Nhân Dân Gia Định",     "Quận Bình Thạnh, TP.HCM", "badge_gia_dinh",   4.8f),
    HospitalHighlight("quan_y_175", "Bệnh viện Quân Y 175",            "Quận Gò Vấp, TP.HCM",     "badge_quan_y_175", 4.7f),
    HospitalHighlight("ung_buou",   "Bệnh viện Ung bướu TP. HCM",      "Quận Bình Thạnh, TP.HCM", "badge_ung_buou",   4.6f),
    HospitalHighlight("mat_hcm",    "Bệnh viện Mắt TP. HCM",           "Quận 3, TP.HCM",          "badge_mat_hcm",    4.7f),
    HospitalHighlight("chac2",      "PHÒNG KHÁM ĐA KHOA CHAC2",        "TP. Thủ Đức, TP.HCM",     "badge_chac2",      4.5f),
    HospitalHighlight("hang_xanh",  "PHÒNG KHÁM ĐA KHOA HÀNG XANH",    "Quận Bình Thạnh, TP.HCM", "badge_hang_xanh",  4.4f),
    HospitalHighlight("tam_phuc",   "Phòng khám sản phụ khoa Tâm Phúc","Quận Bình Thạnh, TP.HCM", "badge_tam_phuc",   4.8f)
)

private val doctorsSample = listOf(
    DoctorHighlight("Nguyễn Đức Lương", "Bác sĩ chuyên khoa", "200.000₫"),
    DoctorHighlight("Nguyễn Đức Mạnh", "Bác sĩ hô hấp", "220.000₫"),
    DoctorHighlight("Nguyễn Đức Long", "Bác sĩ nhi khoa", "210.000₫")
)

/* ===================== SCREEN ===================== */

@Composable
fun HomeScreenScaffold(
    quickActions: List<QuickAction> = quickActionsSample,
    hospitals: List<HospitalHighlight> = hospitalsSample,
    doctors: List<DoctorHighlight> = doctorsSample,
    onFacilityBookingClick: () -> Unit = {},
    modifier: Modifier = Modifier,
    onProfileClick: () -> Unit = {},
    onTicketClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onAccountClick: () -> Unit = {}
) {
    var showPatientRecords by rememberSaveable { mutableStateOf(false) }

    // Build patient records from PatientController
    val patientRecords = PatientController.patients.map { p ->
        val fullAddress = buildString {
            append(p.addressLine)
            if (p.ward.isNotBlank()) append(", ${p.ward}")
            if (p.district.isNotBlank()) append(", ${p.district}")
            if (p.province.isNotBlank()) append(", ${p.province}")
        }
        PatientRecord(
            name = p.name,
            phone = p.phone,
            birthDate = p.birthDate,
            address = fullAddress
        )
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            if (showPatientRecords) {
                PatientRecordsTopBar(
                    onBackClick = { showPatientRecords = false },
                    onCreateClick = { /* TODO */ }
                )
            } else {
                HomeTopBar()
            }
        },
        bottomBar = {
            MainBottomNavigationBar(
                activeItem = when {
                    showPatientRecords -> MainBottomNavItem.Profile
                    else -> MainBottomNavItem.Home
                },
                onHomeClick = { showPatientRecords = false },
                onProfileClick = {
                    showPatientRecords = true
                    onProfileClick()
                },
                onTicketClick = onTicketClick,
                onNotificationClick = onNotificationClick,
                onAccountClick = onAccountClick
            )
        },
        containerColor = Color.White
    ) { innerPadding ->
        if (showPatientRecords) {
            PatientRecordsScreen(
                records = patientRecords,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        } else {
            HomeContent(
                quickActions = quickActions,
                hospitals = hospitals,
                doctors = doctors,
                onQuickActionClick = { action ->
                    if (action.id == "facility_booking") onFacilityBookingClick()
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            )
        }
    }
}

@Composable
private fun HomeContent(
    quickActions: List<QuickAction>,
    hospitals: List<HospitalHighlight>,
    doctors: List<DoctorHighlight>,
    onQuickActionClick: (QuickAction) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp)
    ) {
        item { Spacer(modifier = Modifier.height(20.dp)) }
        item { QuickActionsCard(quickActions, onQuickActionClick) }
        item { Spacer(modifier = Modifier.height(28.dp)) }
        item { SectionTitle("ĐỐI TÁC BỆNH VIỆN TIN TƯỞNG VÀ HỢP TÁC", null, Modifier.fillMaxWidth()) }
        item { Spacer(modifier = Modifier.height(20.dp)) }
        item { HospitalsRow(hospitals = hospitals, modifier = Modifier.fillMaxWidth()) }
        item { Spacer(modifier = Modifier.height(32.dp)) }
        item { BannerCard() }
        item { Spacer(modifier = Modifier.height(32.dp)) }
        item { SectionTitle("CƠ SỞ Y TẾ", "Đặt khám nhiều nhất") }
        item { Spacer(modifier = Modifier.height(16.dp)) }
        item { HospitalHighlightsSection(hospitals) }
        item { Spacer(modifier = Modifier.height(32.dp)) }
        item { SectionTitle("BÁC SĨ TƯ VẤN", "Khám bệnh qua video") }
        item { Spacer(modifier = Modifier.height(16.dp)) }
        item { DoctorHighlightsSection(doctors) }
        item { Spacer(modifier = Modifier.height(32.dp)) }
    }
}

/* === SectionTitle === */
@Composable
private fun SectionTitle(title: String, subtitle: String?, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(
            text = title,
            color = BrandPalette.DeepBlue,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.2.sp
            )
        )
        subtitle?.let {
            Text(
                text = it,
                color = BrandPalette.SlateGrey,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

/* ===================== TOP AREA ===================== */

@Composable
private fun HomeTopBar() {
    Surface(color = Color.White) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            HeaderSection()
            SearchBarTop()
        }
    }
}

@Composable
private fun HeaderSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            shape = CircleShape,
            color = Color.Transparent,
            tonalElevation = 0.dp,
            shadowElevation = 0.dp,
            modifier = Modifier.size(64.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.onboarding_logo),
                contentDescription = "Logo ứng dụng",
                modifier = Modifier.padding(14.dp),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = "Vactrack xin chào!",
                color = BrandPalette.DeepBlue,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = "Nhóm 1,",
                color = BrandPalette.SlateGrey,
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Surface(
            shape = CircleShape,
            color = Color.Transparent,
            tonalElevation = 0.dp,
            shadowElevation = 0.dp
        ) {
            Image(
                painter = painterResource(id = R.drawable.onboarding_logo),
                contentDescription = "Ảnh đại diện",
                modifier = Modifier.size(48.dp),
                contentScale = ContentScale.Fit
            )
        }
    }
}

@Composable
private fun SearchBarTop() {
    Surface(
        shape = RoundedCornerShape(30.dp),
        color = Color.White,
        tonalElevation = 6.dp,
        shadowElevation = 6.dp,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Tìm kiếm",
                tint = BrandPalette.OceanBlue
            )
            Text(
                text = "Tìm CSYT/ bác sĩ/ chuyên khoa/ dịch vụ",
                color = BrandPalette.SlateGrey.copy(alpha = 0.7f),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

/* ===================== QUICK ACTIONS ===================== */

@Composable
private fun QuickActionsCard(
    actions: List<QuickAction>,
    onActionClick: (QuickAction) -> Unit
) {
    Surface(
        shape = RoundedCornerShape(28.dp),
        color = Color.White,
        tonalElevation = 6.dp,
        shadowElevation = 6.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp, horizontal = 18.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            actions.chunked(4).forEach { row ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    row.forEach { action -> QuickActionItem(action, onActionClick) }
                }
            }
        }
    }
}

@Composable
private fun RowScope.QuickActionItem(
    action: QuickAction,
    onActionClick: (QuickAction) -> Unit
) {
    Column(
        modifier = Modifier
            .weight(1f)
            .clip(RoundedCornerShape(24.dp))
            .clickable { onActionClick(action) }
            .padding(horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = Color.Transparent,
            tonalElevation = 0.dp,
            shadowElevation = 0.dp,
            modifier = Modifier.size(56.dp)
        ) {
            Image(
                painter = painterResource(id = action.icon),
                contentDescription = action.title,
                modifier = Modifier.size(28.dp),
                contentScale = ContentScale.Fit
            )
        }
        Text(
            text = action.title,
            color = BrandPalette.SlateGrey,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/* ===================== PARTNER HOSPITALS (ROW) ===================== */

@Composable
private fun HospitalsRow(
    hospitals: List<HospitalHighlight>,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(28.dp),
        color = Color.White,
        tonalElevation = 6.dp,
        shadowElevation = 6.dp
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(hospitals) { hospital -> HospitalCircleLogoItem(hospital) }
        }
    }
}

@Composable
private fun HospitalCircleLogoItem(hospital: HospitalHighlight) {
    val context = LocalContext.current
    val resId = resolveDrawableId(context.resources, context.packageName, hospital.logoName)

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Surface(
            shape = CircleShape,
            color = Color.Transparent,
            tonalElevation = 0.dp,
            shadowElevation = 0.dp,
            modifier = Modifier.size(88.dp)
        ) {
            // Ảnh phủ kín vòng tròn + scale để bù viền trong suốt
            Image(
                painter = painterResource(id = resId),
                contentDescription = hospital.name,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .graphicsLayer {
                        scaleX = 1.5f
                        scaleY = 1.5f
                    },
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = hospital.name,
            color = BrandPalette.DeepBlue,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = hospital.address,
            color = BrandPalette.SlateGrey,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

/* ===================== BANNER ===================== */

@Composable
private fun BannerCard() {
    Surface(
        shape = RoundedCornerShape(28.dp),
        color = Color.White,
        tonalElevation = 6.dp,
        shadowElevation = 6.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.img_caring_for_life),
                contentDescription = "Banner chăm sóc sức khỏe",
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(20.dp)),
                contentScale = ContentScale.Crop
            )
            Button(
                onClick = { },
                modifier = Modifier.align(Alignment.CenterHorizontally),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandPalette.OceanBlue,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Dịch vụ của chúng tôi",
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp)
                )
            }
        }
    }
}

/* ===================== HOSPITAL CARDS (CƠ SỞ Y TẾ) ===================== */

@Composable
private fun HospitalHighlightsSection(hospitals: List<HospitalHighlight>) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        items(hospitals) { hospital -> HospitalCard(hospital) }
    }
}

@Composable
private fun HospitalCard(hospital: HospitalHighlight) {
    val context = LocalContext.current
    val logoRes = resolveDrawableId(context.resources, context.packageName, hospital.logoName)

    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .width(210.dp)
            .height(220.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = CircleShape,
                color = Color.Transparent,
                tonalElevation = 0.dp,
                shadowElevation = 0.dp,
                modifier = Modifier.size(96.dp)
            ) {
                Image(
                    painter = painterResource(id = logoRes),
                    contentDescription = hospital.name,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                        .graphicsLayer {
                            scaleX = 1.5f
                            scaleY = 1.5f
                        },
                    contentScale = ContentScale.Crop
                )
            }
            Text(
                text = hospital.name,
                color = BrandPalette.DeepBlue,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
            Text(
                text = hospital.address,
                color = BrandPalette.SlateGrey,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            RatingBar(rating = hospital.rating)
            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandPalette.OceanBlue,
                    contentColor = Color.White
                )
            ) {
                Text(text = "Đặt khám ngay", fontSize = 12.sp)
            }
        }
    }
}

@Composable
private fun RatingBar(rating: Float) {
    // Clamp rating 0..5 để tránh lỗi
    val r = min(5f, max(0f, rating))
    val full = floor(r).toInt()
    val half = if ((r - full) >= 0.5f) 1 else 0
    val empty = 5 - full - half

    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
        repeat(full) {
            Icon(Icons.Filled.Star, null, tint = Color(0xFFFFC107), modifier = Modifier.size(14.dp))
        }
        repeat(half) {
            Icon(Icons.Filled.StarHalf, null, tint = Color(0xFFFFC107), modifier = Modifier.size(14.dp))
        }
        repeat(empty) {
            Icon(Icons.Outlined.Star, null, tint = Color(0x33FFC107), modifier = Modifier.size(14.dp))
        }
    }
}

/* ===================== DOCTORS ===================== */

@Composable
private fun DoctorHighlightsSection(doctors: List<DoctorHighlight>) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        items(doctors) { doctor -> DoctorCard(doctor) }
    }
}

@Composable
private fun DoctorCard(doctor: DoctorHighlight) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .width(210.dp)
            .height(230.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Surface(
                shape = CircleShape,
                color = Color.Transparent,
                tonalElevation = 0.dp,
                shadowElevation = 0.dp
            ) {
                Image(
                    painter = painterResource(id = R.drawable.onboarding_logo),
                    contentDescription = doctor.name,
                    modifier = Modifier.size(72.dp),
                    contentScale = ContentScale.Fit
                )
            }
            Text(
                text = doctor.name,
                color = BrandPalette.DeepBlue,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = doctor.speciality,
                color = BrandPalette.SlateGrey,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
            Text(
                text = doctor.fee,
                color = BrandPalette.DeepBlue,
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
            )
            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(18.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandPalette.OceanBlue,
                    contentColor = Color.White
                )
            ) {
                Text(text = "Tư vấn ngay", fontSize = 12.sp)
            }
        }
    }
}

/* ===================== PATIENT RECORDS ===================== */

@Composable
private fun PatientRecordsTopBar(
    onBackClick: () -> Unit,
    onCreateClick: () -> Unit
) {
    Surface(color = BrandPalette.OceanBlue, shadowElevation = 6.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Quay lại",
                    tint = Color.White
                )
            }
            Text(
                text = "Hồ sơ bệnh nhân",
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp),
                color = Color.White,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center
            )
            Surface(shape = RoundedCornerShape(20.dp), color = Color.White.copy(alpha = 0.2f)) {
                Row(
                    modifier = Modifier
                        .clickable(onClick = onCreateClick)
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "Tạo mới",
                        tint = Color.White
                    )
                    Text(
                        text = "Tạo mới",
                        color = Color.White,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}

@Composable
private fun PatientRecordsScreen(
    records: List<PatientRecord>,
    modifier: Modifier = Modifier
) {
    if (records.isEmpty()) {
        // Empty state
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Chưa có hồ sơ bệnh nhân",
                color = Color.Gray,
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center
            )
        }
    } else {
        LazyColumn(
            modifier = modifier.padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            items(records) { record -> PatientRecordCard(record) }
        }
    }
}

@Composable
private fun PatientRecordCard(
    record: PatientRecord,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = record.name,
                color = BrandPalette.DeepBlue,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            RecordRow(icon = Icons.Filled.Phone, value = record.phone)
            RecordRow(icon = Icons.Filled.Cake, value = record.birthDate)
            RecordRow(icon = Icons.Filled.LocationOn, value = record.address)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { /* TODO */ },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(50),
                    border = BorderStroke(1.dp, BrandPalette.OceanBlue),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = BrandPalette.OceanBlue)
                ) {
                    Text(text = "Chi tiết")
                }
                Button(
                    onClick = { /* TODO */ },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BrandPalette.OceanBlue,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Thông tin sức khỏe")
                }
            }
        }
    }
}

@Composable
private fun RecordRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = BrandPalette.OceanBlue,
            modifier = Modifier.size(18.dp)
        )
        Text(
            text = value,
            color = BrandPalette.SlateGrey,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

/* ===================== UTILS ===================== */

private fun resolveDrawableId(
    resources: Resources,
    packageName: String,
    name: String
): Int {
    val id = resources.getIdentifier(name, "drawable", packageName)
    if (id == 0) {
        Log.e(LOGO_LOG_TAG, "Không tìm thấy drawable: $name — fallback onboarding_logo")
        return R.drawable.onboarding_logo
    }
    return id
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun HomeScreenPreview() {
    Vactrack_ver1Theme { HomeScreenScaffold() }
}
