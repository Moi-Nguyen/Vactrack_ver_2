package com.example.vactrack_ver1.view.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vactrack_ver1.R
import com.example.vactrack_ver1.design.BrandPalette
import com.example.vactrack_ver1.ui.theme.Vactrack_ver1Theme
import com.example.vactrack_ver1.view.components.MainBottomNavItem
import com.example.vactrack_ver1.view.components.MainBottomNavigationBar

data class QuickAction(val id: String, val title: String, val icon: Int)
data class HospitalHighlight(val name: String, val address: String)
data class DoctorHighlight(val name: String, val speciality: String, val fee: String)
data class PatientRecord(
    val name: String,
    val phone: String,
    val birthDate: String,
    val address: String
)

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

private val hospitalsSample = listOf(
    HospitalHighlight("Bệnh viện A", "Quận 1, TP.HCM"),
    HospitalHighlight("Bệnh viện B", "Quận 2, TP.HCM"),
    HospitalHighlight("Bệnh viện C", "Quận 3, TP.HCM"),
    HospitalHighlight("Bệnh viện D", "Quận 4, TP.HCM"),
    HospitalHighlight("Bệnh viện E", "Quận 5, TP.HCM"),
    HospitalHighlight("Bệnh viện F", "Quận 6, TP.HCM")
)

private val doctorsSample = listOf(
    DoctorHighlight("Nguyễn Đức Lương", "Bác sĩ chuyên khoa", "200.000₫"),
    DoctorHighlight("Nguyễn Đức Mạnh", "Bác sĩ hô hấp", "220.000₫"),
    DoctorHighlight("Nguyễn Đức Long", "Bác sĩ nhi khoa", "210.000₫")
)

private val patientRecordsSample = listOf(
    PatientRecord(
        name = "Lê Đức Anh",
        phone = "0123456789",
        birthDate = "17/10/2005",
        address = "02 Võ Oanh, Thạnh Lộc, Quận 12, TP.HCM, Việt Nam"
    )
)

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
                records = patientRecordsSample,
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
                    if (action.id == "facility_booking") {
                        onFacilityBookingClick()
                    }
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
        item {
            SectionTitle(
                title = "ĐỐI TÁC BỆNH VIỆN TIN TƯỞNG VÀ HỢP TÁC",
                subtitle = null,
                modifier = Modifier.fillMaxWidth()
            )
        }
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
            color = BrandPalette.MistWhite,
            tonalElevation = 4.dp,
            shadowElevation = 4.dp,
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
            color = BrandPalette.MistWhite,
            tonalElevation = 4.dp,
            shadowElevation = 4.dp
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
            color = BrandPalette.MistWhite,
            tonalElevation = 4.dp,
            shadowElevation = 4.dp,
            modifier = Modifier.size(56.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Image(
                    painter = painterResource(id = action.icon),
                    contentDescription = action.title,
                    modifier = Modifier.size(28.dp),
                    contentScale = ContentScale.Fit
                )
            }
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
            items(hospitals) { hospital ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Surface(
                        shape = CircleShape,
                        color = BrandPalette.MistWhite,
                        tonalElevation = 4.dp,
                        shadowElevation = 4.dp,
                        modifier = Modifier.size(80.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.onboarding_logo),
                            contentDescription = hospital.name,
                            modifier = Modifier.padding(16.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = hospital.name,
                        color = BrandPalette.DeepBlue,
                        style = MaterialTheme.typography.bodyMedium,
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = hospital.address,
                        color = BrandPalette.SlateGrey,
                        style = MaterialTheme.typography.bodySmall,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

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

@Composable
private fun HospitalHighlightsSection(hospitals: List<HospitalHighlight>) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
        items(hospitals) { hospital -> HospitalCard(hospital) }
    }
}

@Composable
private fun HospitalCard(hospital: HospitalHighlight) {
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
            Image(
                painter = painterResource(id = R.drawable.onboarding_logo),
                contentDescription = hospital.name,
                modifier = Modifier.size(72.dp),
                contentScale = ContentScale.Fit
            )
            Text(
                text = hospital.name,
                color = BrandPalette.DeepBlue,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = hospital.address,
                color = BrandPalette.SlateGrey,
                style = MaterialTheme.typography.bodySmall
            )
            RatingBar()
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
private fun RatingBar() {
    Row(horizontalArrangement = Arrangement.spacedBy(2.dp)) {
        repeat(5) {
            Icon(
                imageVector = Icons.Filled.Star,
                contentDescription = null,
                tint = Color(0xFFFFC107),
                modifier = Modifier.size(14.dp)
            )
        }
    }
}

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
                color = BrandPalette.MistWhite,
                tonalElevation = 4.dp,
                shadowElevation = 4.dp
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
    LazyColumn(
        modifier = modifier.padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(records) { record -> PatientRecordCard(record) }
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
    icon: ImageVector,
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

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun HomeScreenPreview() {
    Vactrack_ver1Theme { HomeScreenScaffold() }
}
