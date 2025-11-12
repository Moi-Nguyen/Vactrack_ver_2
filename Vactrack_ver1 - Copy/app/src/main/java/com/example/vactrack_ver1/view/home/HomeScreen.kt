package com.example.vactrack_ver1.view.home

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.automirrored.filled.StarHalf
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
import com.example.vactrack_ver1.view.utils.*
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import androidx.compose.foundation.lazy.itemsIndexed


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
private data class PatientRecord(val name: String, val phone: String, val birthDate: String, val address: String)

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
    onSpecialtyBookingClick: () -> Unit = {},
    onTicketUnpaidClick: () -> Unit = {},
    onTicketPaidClick: () -> Unit = {},
    onHospitalBookNowClick: (HospitalHighlight) -> Unit = {}, // New callback for hospital booking
    modifier: Modifier = Modifier,
    onProfileClick: () -> Unit = {},
    onTicketClick: () -> Unit = {},
    onNotificationClick: () -> Unit = {},
    onAccountClick: () -> Unit = {}
) {
    var showPatientRecords by rememberSaveable { mutableStateOf(false) }
    val context = LocalContext.current

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
        containerColor = Color(0xFFF3F9FF)
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
                    when (action.id) {
                        // Navigate to facility booking
                        "facility_booking" -> onFacilityBookingClick()
                        
                        // Navigate to specialty booking
                        "specialist_booking" -> onSpecialtyBookingClick()
                        
                        // Show "coming soon" for video call
                        "video_call" -> {
                            Toast.makeText(
                                context,
                                "Tính năng sẽ được phát triển trong các phiên bản tiếp theo.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        
                        // Show "coming soon" for health package
                        "health_package" -> {
                            Toast.makeText(
                                context,
                                "Tính năng sẽ được phát triển trong các phiên bản tiếp theo.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        
                        // Navigate to ticket list (Unpaid tab)
                        "bill_payment" -> onTicketUnpaidClick()
                        
                        // Show "coming soon" for doctor booking
                        "doctor_booking" -> {
                            Toast.makeText(
                                context,
                                "Tính năng sẽ được phát triển trong các phiên bản tiếp theo.",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        
                        // Navigate to ticket list (Paid tab)
                        "result_lookup" -> onTicketPaidClick()
                        
                        // Open phone dialer with hotline number
                        "hotline_booking" -> {
                            val intent = Intent(Intent.ACTION_DIAL).apply {
                                data = Uri.parse("tel:19002115")
                            }
                            context.startActivity(intent)
                        }
                    }
                },
                onHospitalBookNowClick = onHospitalBookNowClick,
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
    onHospitalBookNowClick: (HospitalHighlight) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val horizontalPadding = responsiveHorizontalPadding()
    val spacingSmall = responsiveSpacingSmall()
    val spacingMedium = responsiveSpacingMedium()
    val spacingLarge = responsiveSpacingLarge()
    
    LazyColumn(
        modifier = modifier,
        contentPadding = PaddingValues(horizontal = horizontalPadding, vertical = spacingMedium)
    ) {
        item { HeaderSection() }
        item { Spacer(modifier = Modifier.height(spacingMedium)) }
        item { SearchBarTop() }
        item { Spacer(modifier = Modifier.height(spacingMedium)) }
        item { QuickActionsCard(quickActions, onQuickActionClick) }
        item { Spacer(modifier = Modifier.height(spacingLarge)) }
        item { SectionTitle("ĐỐI TÁC BỆNH VIỆN TIN TƯỞNG VÀ HỢP TÁC", null, Modifier.fillMaxWidth()) }
        item { Spacer(modifier = Modifier.height(spacingSmall)) }
        item { PartnerHospitalsCard(hospitals = hospitals, modifier = Modifier.fillMaxWidth()) }
        item { Spacer(modifier = Modifier.height(spacingLarge)) }
        item { SectionTitle("CƠ SỞ Y TẾ", "Đặt khám nhiều nhất") }
        item { Spacer(modifier = Modifier.height(spacingMedium)) }
        item { HospitalHighlightsSection(hospitals, onHospitalBookNowClick) }
        item { Spacer(modifier = Modifier.height(spacingLarge)) }
        item { SectionTitle("BÁC SĨ TƯ VẤN", "Khám bệnh qua video") }
        item { Spacer(modifier = Modifier.height(spacingMedium)) }
        item { DoctorHighlightsSection(doctors) }
        item { Spacer(modifier = Modifier.height(spacingLarge)) }
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
private fun HeaderSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Left logo
        Image(
            painter = painterResource(id = R.drawable.onboarding_logo),
            contentDescription = "Logo ứng dụng",
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.width(12.dp))

        // Center greeting
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

        // Right avatar
        Image(
            painter = painterResource(id = R.drawable.onboarding_logo),
            contentDescription = "Ảnh đại diện",
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Fit
        )
    }
}

@Composable
private fun SearchBarTop() {
    Card(
        shape = RoundedCornerShape(30.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                imageVector = Icons.Filled.Search,
                contentDescription = "Tìm kiếm",
                tint = BrandPalette.OceanBlue,
                modifier = Modifier.size(24.dp)
            )
            Text(
                text = "Tìm CSYT/ bác sĩ/ chuyên khoa/ dịch vụ",
                color = BrandPalette.SlateGrey.copy(alpha = 0.65f),
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
    val gridColumns = responsiveGridColumns(compactColumns = 4)
    val cardPadding = responsiveSpacingMedium()
    
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        // FIXED: LazyVerticalGrid inside LazyColumn item needs bounded height
        // Using .height() instead of .heightIn() to avoid infinite constraints
        LazyVerticalGrid(
            columns = GridCells.Fixed(gridColumns),
            modifier = Modifier
                .fillMaxWidth()
                .height(260.dp),  // Fixed height to avoid infinite constraints
            contentPadding = PaddingValues(cardPadding),
            horizontalArrangement = Arrangement.spacedBy(responsiveSpacingSmall()),
            verticalArrangement = Arrangement.spacedBy(responsiveSpacingMedium())
        ) {
            items(actions) { action ->
                QuickActionItem(
                    action = action,
                    onActionClick = onActionClick
                )
            }
        }
    }
}

@Composable
private fun QuickActionItem(
    action: QuickAction,
    onActionClick: (QuickAction) -> Unit
) {
    val iconSize = responsiveIconSize(baseSize = 40.dp)
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onActionClick(action) }
            .padding(vertical = responsiveSpacingSmall()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(responsiveSpacingSmall())
    ) {
        Image(
            painter = painterResource(id = action.icon),
            contentDescription = action.title,
            modifier = Modifier.size(iconSize),
            contentScale = ContentScale.Fit
        )
        
        Text(
            text = action.title,
            color = BrandPalette.SlateGrey,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

/* ===================== PARTNER HOSPITALS ===================== */

@Composable
private fun PartnerHospitalsCard(
    hospitals: List<HospitalHighlight>,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(28.dp),
        color = Color.White,
        tonalElevation = 6.dp,
        shadowElevation = 6.dp,
        modifier = modifier
    ) {
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = responsiveSpacingMedium(), vertical = responsiveSpacingMedium()),
            horizontalArrangement = Arrangement.spacedBy(responsiveSpacingLarge()),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items(hospitals, key = { it.id }) { hospital ->
                PartnerHospitalItem(hospital)
            }
        }
    }
}

@Composable
private fun PartnerHospitalItem(hospital: HospitalHighlight) {
    val context = LocalContext.current
    val logoRes = resolveDrawableId(context.resources, context.packageName, hospital.logoName)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.widthIn(min = 110.dp, max = 140.dp)
    ) {
        Surface(
            shape = CircleShape,
            color = Color.Transparent,
            tonalElevation = 0.dp,
            shadowElevation = 0.dp,
            modifier = Modifier.size(72.dp)
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
        Spacer(Modifier.height(8.dp))
        Text(
            text = hospital.name,
            color = BrandPalette.DeepBlue,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            maxLines = 2,
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

/* ===================== HOSPITAL CARDS (CƠ SỞ Y TẾ) ===================== */

@Composable
private fun HospitalHighlightsSection(
    hospitals: List<HospitalHighlight>,
    onBookNowClick: (HospitalHighlight) -> Unit = {}
) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        itemsIndexed(
            items = hospitals,
            key = { index, _ -> "hospital_card_$index" }
        ) { _, hospital ->
            HospitalCard(
                hospital = hospital,
                onBookNowClick = { onBookNowClick(hospital) }
            )
        }
    }
}

@Composable
private fun HospitalCard(
    hospital: HospitalHighlight,
    onBookNowClick: () -> Unit = {}
) {
    val context = LocalContext.current
    val logoRes = resolveDrawableId(context.resources, context.packageName, hospital.logoName)
    val cardWidth = when (getWindowSize()) {
        WindowSize.COMPACT -> 180.dp
        WindowSize.MEDIUM -> 220.dp
        WindowSize.EXPANDED -> 260.dp
    }
    val logoSize = when (getWindowSize()) {
        WindowSize.COMPACT -> 72.dp
        WindowSize.MEDIUM -> 88.dp
        WindowSize.EXPANDED -> 96.dp
    }

    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .width(cardWidth)
            .wrapContentHeight()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(responsiveSpacingMedium()),
            verticalArrangement = Arrangement.spacedBy(responsiveSpacingSmall()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Circular logo
            Image(
                painter = painterResource(id = logoRes),
                contentDescription = hospital.name,
                modifier = Modifier
                    .size(logoSize)
                    .clip(CircleShape)
                    .graphicsLayer {
                        scaleX = 1.5f
                        scaleY = 1.5f
                    },
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.height(responsiveSpacingSmall()))
            
            // Hospital name
            Text(
                text = hospital.name,
                color = BrandPalette.DeepBlue,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold
                ),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
            )
            
            // Address
            Text(
                text = hospital.address,
                color = BrandPalette.SlateGrey,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp)
            )
            
            // Rating
            RatingBar(rating = hospital.rating)
            
            Spacer(modifier = Modifier.height(responsiveSpacingSmall()))
            
            // Button
            Button(
                onClick = onBookNowClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 40.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandPalette.OceanBlue,
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(vertical = 8.dp, horizontal = 12.dp)
            ) {
                Text(
                    text = "Đặt khám ngay",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
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
            Icon(Icons.AutoMirrored.Filled.StarHalf, null, tint = Color(0xFFFFC107), modifier = Modifier.size(14.dp))
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
        itemsIndexed(
            items = doctors,
            key = { index, _ -> "doctor_card_$index" }
        ) { _, doctor ->
            DoctorCard(doctor)
        }
    }
}

@Composable
private fun DoctorCard(doctor: DoctorHighlight) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier
            .width(200.dp)
            .height(240.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Doctor avatar
            Image(
                painter = painterResource(id = R.drawable.onboarding_logo),
                contentDescription = doctor.name,
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Fit
            )
            
            // Doctor name
            Text(
                text = doctor.name,
                color = BrandPalette.DeepBlue,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                ),
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                lineHeight = 16.sp
            )
            
            // Specialty
            Text(
                text = doctor.speciality,
                color = BrandPalette.SlateGrey,
                style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            
            // Fee
            Text(
                text = doctor.fee,
                color = BrandPalette.DeepBlue,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )
            )
            
            Spacer(modifier = Modifier.weight(1f))
            
            // Button
            Button(
                onClick = { },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandPalette.OceanBlue,
                    contentColor = Color.White
                ),
                contentPadding = PaddingValues(vertical = 10.dp)
            ) {
                Text(
                    text = "Tư vấn ngay",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold
                )
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
            itemsIndexed(
                items = records,
                key = { index, _ -> "patient_record_$index" }
            ) { _, record ->
                PatientRecordCard(record)
            }
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
