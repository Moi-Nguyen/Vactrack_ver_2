package com.example.vactrack_ver1.view.facility

import android.content.res.Resources
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Schedule
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Color
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
import com.example.vactrack_ver1.ui.theme.Vactrack_ver1Theme
import java.util.Locale

/* ==== Theme tokens ==== */
private val PrimaryColor = Color(0xFF66C3DA)
private val TextPrimary = Color(0xFF0F172A)
private val TextSecondary = Color(0xFF64748B)
private val SectionBackground = Color(0xFFF4FAFC)
private const val BADGE_LOGO_SCALE = 1.35f

/* ==== Data model ==== */
data class HospitalService(
    val id: String,
    val title: String,
    val iconName: String
)

data class HospitalDetail(
    val id: String,
    val name: String,
    val subtitle: String,
    val imageName: String,
    /** DÙNG NHƯ TÊN DRAWABLE (không đuôi .png). Nếu chưa có ảnh → fallback text. */
    val badge: String,
    val ratingText: String,
    val address: String,
    val workingTime: String,
    val hotline: String,
    val services: List<HospitalService>,
    val introduction: String,
    val specialities: List<String>,
    val directionGuide: String
)

/* ==== Mock data (đặt tên ảnh badge_* để bạn thêm file .png sau) ==== */
internal val hospitalMockDetails: List<HospitalDetail> = listOf(
    HospitalDetail(
        id = "gia_dinh",
        name = "Bệnh viện Nhân Dân Gia Định",
        subtitle = "1 Nơ Trang Long, Phường 7, Bình Thạnh, TP. Hồ Chí Minh",
        imageName = "img_bv_gia_dinh",
        badge = "badge_gia_dinh",
        ratingText = "4.8 | 2.3K đánh giá",
        address = "Địa chỉ: Số 1 Nơ Trang Long, Phường 7, Quận Bình Thạnh, TP. HCM",
        workingTime = "Thời gian khám: Thứ 2 - Chủ Nhật",
        hotline = "Tổng đài đặt khám: 1900 2115",
        services = defaultServices(),
        introduction =
            "Bệnh viện Nhân Dân Gia Định là bệnh viện đa khoa hạng I với lịch sử lâu đời tại TP. HCM. " +
                    "Bệnh viện luôn chú trọng chất lượng khám chữa bệnh, ứng dụng kỹ thuật cao và quy trình phục vụ người bệnh.",
        specialities = listOf(
            "Nội tổng quát","Hô hấp","Nội tiết","Ngoại thần kinh","Lão khoa","Sản khoa","Nhi khoa",
            "Tai mũi họng","Ngoại lồng ngực","Ngoại chấn thương","Tim mạch can thiệp – DSA","Nội tim mạch",
            "Da liễu","Cơ xương khớp","Nội thần kinh","Tiêu hóa","Huyết học","Phụ khoa","Răng hàm mặt",
            "Mắt","Ngoại niệu","Dinh dưỡng","Hồi sức tim mạch","Hồi sức tích cực – Chống độc"
        ),
        directionGuide =
            "Bệnh viện nằm trên trục Nơ Trang Long, Bình Thạnh; thuận tiện đi xe máy/ô tô, gần bus 8, 54, 104."
    ),
    HospitalDetail(
        id = "quan_y_175",
        name = "Bệnh viện Quân Y 175",
        subtitle = "786 Nguyễn Kiệm, Gò Vấp, TP. Hồ Chí Minh",
        imageName = "img_bv_qy175",
        badge = "badge_quan_y_175",
        ratingText = "4.7 | 3.0K đánh giá",
        address = "Địa chỉ: 786 Nguyễn Kiệm, Phường 3, Quận Gò Vấp, TP. HCM",
        workingTime = "Thời gian khám: Thứ 2 - Thứ 7",
        hotline = "Tư vấn/đặt lịch: 1900 1175",
        services = defaultServices(),
        introduction =
            "Bệnh viện tuyến cuối của quân đội khu vực phía Nam, tiếp nhận khám chữa bệnh cho quân nhân và người dân với hệ thống thiết bị hiện đại.",
        specialities = listOf(
            "Chấn thương chỉnh hình", "Ngoại thần kinh", "Hồi sức cấp cứu", "Tim mạch",
            "Chẩn đoán hình ảnh", "Nội tổng quát", "Vật lý trị liệu – PHCN", "Nhi","Sản"
        ),
        directionGuide = "Mặt tiền đường Nguyễn Kiệm, bãi giữ xe rộng; thuận tiện taxi/xe công nghệ."
    ),
    HospitalDetail(
        id = "ung_buou_hcm",
        name = "Bệnh viện Ung bướu TP. HCM",
        subtitle = "47 Nguyễn Huy Lượng, Phường 14, Bình Thạnh, TP. Hồ Chí Minh",
        imageName = "img_bv_ung_buou",
        badge = "badge_ung_buou",
        ratingText = "4.6 | 2.1K đánh giá",
        address = "Địa chỉ: 47 Nguyễn Huy Lượng, Phường 14, Quận Bình Thạnh, TP. HCM",
        workingTime = "Thời gian khám: Thứ 2 - Thứ 7",
        hotline = "Tổng đài tư vấn: 1900 633 315",
        services = defaultServices(),
        introduction =
            "Bệnh viện chuyên khoa ung bướu đầu ngành, triển khai điều trị đa mô thức theo phác đồ chuẩn, chăm sóc giảm nhẹ và tầm soát.",
        specialities = listOf(
            "Hóa trị", "Xạ trị", "Phẫu thuật ung bướu", "Nội soi can thiệp", "Chăm sóc giảm nhẹ",
            "Ung bướu vú", "Ung bướu gan mật", "Ung bướu tiêu hóa", "Ung bướu hô hấp"
        ),
        directionGuide = "Khu vực Bình Thạnh, tiếp cận từ Đinh Tiên Hoàng/Phan Đăng Lưu; có bãi giữ xe."
    ),
    HospitalDetail(
        id = "mat_hcm",
        name = "Bệnh viện Mắt TP. HCM",
        subtitle = "280 Điện Biên Phủ, Phường Võ Thị Sáu, Quận 3, TP. Hồ Chí Minh",
        imageName = "img_bv_mat",
        badge = "badge_mat_hcm",
        ratingText = "4.7 | 2.4K đánh giá",
        address = "Địa chỉ: 280 Điện Biên Phủ, Phường Võ Thị Sáu, Quận 3, TP. HCM",
        workingTime = "Thời gian khám: Thứ 2 - Thứ 7",
        hotline = "Hỗ trợ/đặt lịch: 1900 7274",
        services = defaultServices(),
        introduction =
            "Bệnh viện chuyên khoa mắt lớn, thế mạnh phẫu thuật khúc xạ, điều trị glôcôm và bệnh lý võng mạc.",
        specialities = listOf(
            "Khúc xạ – Lasik/Smile", "Đục thủy tinh thể", "Glôcôm", "Võng mạc – Đái tháo đường",
            "Mộng – Quặm", "Nhi khoa mắt", "Tạo hình", "Chấn thương mắt"
        ),
        directionGuide = "Trục Điện Biên Phủ; thuận bus/xe công nghệ. Bãi đậu xe hạn chế giờ cao điểm."
    ),
    HospitalDetail(
        id = "chac2",
        name = "PHÒNG KHÁM ĐA KHOA CHAC2",
        subtitle = "42 Đặng Văn Bi, TP. Thủ Đức, TP. Hồ Chí Minh",
        imageName = "img_pk_chac2",
        badge = "badge_chac2",
        ratingText = "4.5 | 850 đánh giá",
        address = "Địa chỉ: 42 Đặng Văn Bi, TP. Thủ Đức, TP. HCM",
        workingTime = "Thời gian làm việc: Thứ 2 - Chủ Nhật",
        hotline = "Hotline: 028 3896 6868",
        services = defaultServices(),
        introduction =
            "Phòng khám đa khoa tiếp nhận khám BHYT và dịch vụ, xét nghiệm, chẩn đoán cơ bản, tầm soát sức khỏe.",
        specialities = listOf("Nội tổng quát","Nhi","Sản","Tai mũi họng","Da liễu","Răng hàm mặt","Xét nghiệm","Siêu âm"),
        directionGuide = "Mặt tiền Đặng Văn Bi, dễ tìm; có bãi giữ xe máy."
    ),
    HospitalDetail(
        id = "hang_xanh",
        name = "PHÒNG KHÁM ĐA KHOA HÀNG XANH",
        subtitle = "395 Điện Biên Phủ, Bình Thạnh, TP. Hồ Chí Minh",
        imageName = "img_pk_hang_xanh",
        badge = "badge_hang_xanh",
        ratingText = "4.4 | 620 đánh giá",
        address = "Địa chỉ: 395 Điện Biên Phủ, Phường 25, Quận Bình Thạnh, TP. HCM",
        workingTime = "Thời gian làm việc: Thứ 2 - Chủ Nhật",
        hotline = "Hotline: 028 3512 3456",
        services = defaultServices(),
        introduction =
            "Phòng khám đa khoa khu vực Hàng Xanh, quy trình nhanh, phù hợp các nhu cầu khám thông thường.",
        specialities = listOf("Nội","Nhi","Sản","Da liễu","Cơ xương khớp","TMH","RHM","Siêu âm – XN"),
        directionGuide = "Gần vòng xoay Hàng Xanh, dễ di chuyển từ nhiều hướng."
    ),
    HospitalDetail(
        id = "tam_phuc",
        name = "Phòng khám sản phụ khoa Tâm Phúc",
        subtitle = "164 Nguyễn Xí, Phường 26, Bình Thạnh, TP. Hồ Chí Minh",
        imageName = "img_pk_tam_phuc",
        badge = "badge_tam_phuc",
        ratingText = "4.8 | 1.1K đánh giá",
        address = "Địa chỉ: 164 Nguyễn Xí, Phường 26, Quận Bình Thạnh, TP. HCM",
        workingTime = "Thời gian làm việc: Thứ 2 - Chủ Nhật",
        hotline = "Hotline: 028 3888 8899",
        services = defaultServices(),
        introduction =
            "Phòng khám chuyên sản phụ khoa, theo dõi thai, siêu âm, tầm soát và điều trị các bệnh lý phụ khoa.",
        specialities = listOf("Khám thai","Siêu âm","Hiếm muộn","Phụ khoa tổng quát","KHHGĐ","Sản khoa"),
        directionGuide = "Trục Nguyễn Xí; gần BX Miền Đông cũ; có chỗ đậu xe máy."
    )
)

private fun defaultServices(): List<HospitalService> = listOf(
    HospitalService("bhyt", "Khám BHYT, Khám viện phí", "img_kham"),
    HospitalService("specialist", "Khám chuyên gia", "img_kham"),
    HospitalService("service", "Khám dịch vụ (Tại khoa)", "img_kham"),
    HospitalService("video", "Tư vấn trực tiếp", "img_goi_video"),
    HospitalService("doctor", "Đặt khám theo bác sĩ", "ic_service_doctor")
)

/* ==== Screen ==== */
@Composable
fun HospitalDetailScreen(
    modifier: Modifier = Modifier,
    hospitals: List<HospitalDetail> = hospitalMockDetails,
    initialHospitalId: String? = null,
    onBackClick: () -> Unit = {},
    onBookNowClick: (HospitalDetail) -> Unit = {}
) {
    var selectedId by rememberSaveable {
        mutableStateOf(
            initialHospitalId?.takeIf { id -> hospitals.any { it.id == id } }
                ?: hospitals.firstOrNull()?.id.orEmpty()
        )
    }
    LaunchedEffect(initialHospitalId, hospitals) {
        initialHospitalId?.takeIf { id -> hospitals.any { it.id == id } }?.let { selectedId = it }
    }
    val selectedHospital = hospitals.firstOrNull { it.id == selectedId } ?: return

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = SectionBackground,
        bottomBar = {
            BookNowBar(
                onClick = { onBookNowClick(selectedHospital) }
            )
        }
    ) { inner ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(inner),
            contentPadding = PaddingValues(bottom = 12.dp)
        ) {
            item {
                HeaderSection(
                    hospitalName = selectedHospital.name,
                    imageName = selectedHospital.imageName,
                    badge = selectedHospital.badge,
                    ratingText = selectedHospital.ratingText,
                    onBackClick = onBackClick
                )
            }
            item { HospitalSummaryCard(selectedHospital = selectedHospital) }
            item { ServicesSection(services = selectedHospital.services) }
            item { DetailTabsContent(hospital = selectedHospital) }
            item { Spacer(Modifier.height(88.dp)) }
        }
    }
}

/* ==== Header ==== */
@Composable
private fun HeaderSection(
    hospitalName: String,
    imageName: String,
    badge: String,
    ratingText: String,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val resources = context.resources
    val heroImageRes = remember(imageName) { resolveDrawableId(resources, context.packageName, imageName) }
    val badgeLogoRes = remember(badge) { resolveOptionalDrawableId(resources, context.packageName, badge) }

    Surface(
        color = PrimaryColor,
        tonalElevation = 4.dp,
        shadowElevation = 8.dp,
        shape = RoundedCornerShape(bottomStart = 28.dp, bottomEnd = 28.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 20.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                IconButton(onClick = onBackClick, modifier = Modifier.align(Alignment.CenterStart)) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Quay lại",
                        tint = Color.White
                    )
                }
                Text(
                    text = hospitalName,
                    color = Color.White,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        lineHeight = 30.sp
                    ),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .align(Alignment.Center)
                        .padding(horizontal = 56.dp)
                )
            }

            Surface(
                modifier = Modifier
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(26.dp)),
                shape = RoundedCornerShape(26.dp),
                tonalElevation = 2.dp,
                shadowElevation = 6.dp
            ) {
                Image(
                    painter = painterResource(id = heroImageRes),
                    contentDescription = hospitalName,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 22.dp),
                color = PrimaryColor.copy(alpha = 0.22f),
                shape = RoundedCornerShape(32.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 18.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(18.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color.Transparent,
                        border = BorderStroke(1.2.dp, Color.White.copy(alpha = 0.7f)),
                        tonalElevation = 0.dp,
                        shadowElevation = 0.dp,
                        modifier = Modifier.size(96.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            if (badgeLogoRes != null) {
                                Image(
                                    painter = painterResource(id = badgeLogoRes),
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .graphicsLayer(
                                            scaleX = BADGE_LOGO_SCALE,
                                            scaleY = BADGE_LOGO_SCALE
                                        ),
                                    contentScale = ContentScale.Crop,
                                    alignment = Alignment.Center
                                )
                            } else {
                                Text(
                                    text = badge,
                                    color = PrimaryColor,
                                    textAlign = TextAlign.Center,
                                    style = MaterialTheme.typography.titleMedium.copy(
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 19.sp,
                                        lineHeight = 22.sp
                                    )
                                )
                            }
                        }
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = hospitalName,
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 20.sp,
                                lineHeight = 24.sp
                            ),
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            repeat(5) {
                                Icon(
                                    imageVector = Icons.Filled.Star,
                                    contentDescription = null,
                                    tint = Color(0xFFFFC107),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = ratingText,
                                color = Color.White,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontSize = 16.sp,
                                    lineHeight = 20.sp
                                ),
                                modifier = Modifier.weight(1f),
                                textAlign = TextAlign.End
                            )
                        }
                    }
                }
            }
        }
    }
}
/* ==== Summary card ==== */
@Composable
private fun HospitalSummaryCard(selectedHospital: HospitalDetail) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        shape = RoundedCornerShape(24.dp),
        color = Color.White,
        tonalElevation = 4.dp,
        shadowElevation = 8.dp
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 22.dp, vertical = 20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = selectedHospital.name,
                color = PrimaryColor,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    lineHeight = 24.sp
                )
            )
            Text(
                text = selectedHospital.subtitle,
                color = TextPrimary,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontSize = 16.sp,
                    lineHeight = 22.sp
                )
            )
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.LocationOn,
                    contentDescription = null,
                    tint = PrimaryColor,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = selectedHospital.address,
                    color = TextSecondary,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 15.sp,
                        lineHeight = 20.sp
                    )
                )
            }
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Schedule,
                    contentDescription = null,
                    tint = PrimaryColor,
                    modifier = Modifier.size(20.dp)
                )
                Column {
                    Text(
                        text = selectedHospital.workingTime,
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 15.sp,
                            lineHeight = 20.sp
                        )
                    )
                    Text(
                        text = selectedHospital.hotline,
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontSize = 15.sp,
                            lineHeight = 20.sp
                        )
                    )
                }
            }
        }
    }
}

/* ==== Services ==== */
@Composable
private fun ServicesSection(services: List<HospitalService>) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        Text(
            text = "Các dịch vụ",
            color = TextPrimary,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        )
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 2.dp)
        ) {
            items(services, key = { it.id }) { service ->
                ServiceCard(service = service)
            }
        }
    }
}

@Composable
private fun ServiceCard(service: HospitalService) {
    val context = LocalContext.current
    val iconRes = remember(service.iconName) {
        resolveDrawableId(context.resources, context.packageName, service.iconName)
    }
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        border = BorderStroke(1.2.dp, PrimaryColor.copy(alpha = 0.4f)),
        tonalElevation = 2.dp,
        shadowElevation = 4.dp,
        modifier = Modifier
            .height(148.dp)
            .width(136.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                shape = CircleShape,
                color = PrimaryColor.copy(alpha = 0.12f),
                modifier = Modifier.size(60.dp)
            ) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = service.title,
                    modifier = Modifier.padding(14.dp),
                    contentScale = ContentScale.Fit
                )
            }
            Text(
                text = service.title,
                color = TextPrimary,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontSize = 16.sp,
                    lineHeight = 20.sp
                ),
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

/* ==== Tabs content ==== */
@Composable
private fun DetailTabsContent(hospital: HospitalDetail) {
    var selectedTab by rememberSaveable { mutableStateOf(0) }
    val tabs = listOf("Giới thiệu", "Chuyên khoa", "Hướng dẫn")

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        Text(
            text = "Mô tả",
            color = TextPrimary,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )
        )
        ScrollableTabRow(
            selectedTabIndex = selectedTab,
            edgePadding = 8.dp,
            containerColor = Color.White,
            contentColor = PrimaryColor,
            divider = {}
        ) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = index == selectedTab,
                    onClick = { selectedTab = index },
                    modifier = Modifier.height(48.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(22.dp),
                        color = if (index == selectedTab) PrimaryColor else Color.Transparent,
                        border = BorderStroke(1.dp, PrimaryColor),
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Box(
                            modifier = Modifier.padding(horizontal = 22.dp, vertical = 10.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = title,
                                color = if (index == selectedTab) Color.White else PrimaryColor,
                                style = MaterialTheme.typography.bodyLarge.copy(
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 16.sp
                                ),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }
        }

        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            tonalElevation = 2.dp,
            shadowElevation = 4.dp
        ) {
            when (selectedTab) {
                0 -> IntroBlock(hospital)
                1 -> SpecialitiesBlock(hospital.specialities)
                else -> GuideBlock(hospital.directionGuide)
            }
        }
    }
}

@Composable
private fun IntroBlock(hospital: HospitalDetail) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Giới thiệu",
            color = TextPrimary,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 19.sp
            )
        )
        Text(
            text = hospital.introduction,
            color = TextSecondary,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 16.sp,
                lineHeight = 22.sp
            )
        )

        Spacer(Modifier.height(8.dp))
        Text(
            text = "Thời gian làm việc của bệnh viện",
            color = TextPrimary,
            style = MaterialTheme.typography.titleSmall.copy(
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp
            )
        )
        Text(
            text = "• ${hospital.workingTime}\n• ${hospital.hotline}",
            color = TextSecondary,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 15.sp,
                lineHeight = 21.sp
            )
        )
    }
}

@Composable
private fun SpecialitiesBlock(items: List<String>) {
    val mid = (items.size + 1) / 2
    val left = items.take(mid)
    val right = items.drop(mid)

    Column(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Chuyên khoa",
            color = TextPrimary,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 19.sp
            )
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                left.forEach {
                    Text(
                        "• $it",
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 15.sp, lineHeight = 20.sp)
                    )
                }
            }
            Column(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                right.forEach {
                    Text(
                        "• $it",
                        color = TextSecondary,
                        style = MaterialTheme.typography.bodyMedium.copy(fontSize = 15.sp, lineHeight = 20.sp)
                    )
                }
            }
        }
        Spacer(Modifier.height(4.dp))
    }
}

@Composable
private fun GuideBlock(text: String) {
    Column(
        modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Hướng dẫn",
            color = TextPrimary,
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 19.sp
            )
        )
        Text(
            text = text,
            color = TextSecondary,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 16.sp,
                lineHeight = 22.sp
            )
        )
    }
}

/* ==== Bottom fixed button ==== */
@Composable
private fun BookNowBar(onClick: () -> Unit) {
    Surface(
        tonalElevation = 6.dp,
        shadowElevation = 12.dp,
        color = Color.White
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            Button(
                onClick = onClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = PrimaryColor,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Đặt khám ngay",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 18.sp
                    )
                )
            }
        }
    }
}

/* ==== Utils ==== */
private fun resolveDrawableId(resources: Resources, packageName: String, name: String): Int =
    resources.getIdentifier(name, "drawable", packageName).takeIf { it != 0 }
        ?: R.drawable.img_dat_kham_co_so

private fun resolveOptionalDrawableId(resources: Resources, packageName: String, name: String): Int? =
    name.takeIf { it.isNotBlank() }?.let { original ->
        listOf(
            original.trim(),
            original.trim().lowercase(Locale.ROOT).replace("\\s+".toRegex(), "_")
        ).distinct().firstNotNullOfOrNull { candidate ->
            resources.getIdentifier(candidate, "drawable", packageName).takeIf { it != 0 }
        }
    }

/* ==== Preview ==== */
@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun HospitalDetailPreview() {
    Vactrack_ver1Theme {
        HospitalDetailScreen(
            hospitals = hospitalMockDetails,
            initialHospitalId = "gia_dinh"
        )
    }
}
