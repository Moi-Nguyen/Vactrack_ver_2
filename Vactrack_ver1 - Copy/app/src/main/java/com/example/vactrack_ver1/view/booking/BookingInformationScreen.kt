package com.example.vactrack_ver1.view.booking

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vactrack_ver1.R
import com.example.vactrack_ver1.ui.theme.Vactrack_ver1Theme
import com.example.vactrack_ver1.view.facility.hospitalMockDetails
import androidx.compose.runtime.remember

/* ==== Theme tokens ==== */
private val PrimaryBlue = Color(0xFF5BB7CF)
private val BackgroundColor = Color(0xFFF5F9FA)
private val TextPrimary = Color(0xFF1F2937)
private val TextSecondary = Color(0xFF6B7280)
private val TextPlaceholder = Color(0xFFB0B8C1)
private val CardBackground = Color.White
private val BorderColor = Color(0xFFD4E9EE)
private val StepInactive = Color(0xFFE5E7EB)

/* ==== Data Models ==== */
// Specialties by hospital ID
private val specialtiesByHospitalId = mapOf(
    "gia_dinh" to listOf(
        "Nội tổng quát", "Hô hấp", "Nội tiết", "Ngoại thần kinh", "Lão khoa",
        "Sản khoa", "Nhi khoa", "Tai mũi họng", "Ngoại lồng ngực", "Ngoại chấn thương",
        "Tim mạch can thiệp", "Nội tim mạch", "Da liễu", "Cơ xương khớp", "Nội thần kinh"
    ),
    "quan_y_175" to listOf(
        "Chấn thương chỉnh hình", "Ngoại thần kinh", "Hồi sức cấp cứu", "Tim mạch",
        "Chẩn đoán hình ảnh", "Nội tổng quát", "Vật lý trị liệu – PHCN", "Nhi", "Sản"
    ),
    "ung_buou_hcm" to listOf(
        "Hóa trị", "Xạ trị", "Phẫu thuật ung bướu", "Nội soi can thiệp", "Chăm sóc giảm nhẹ",
        "Ung bướu vú", "Ung bướu gan mật", "Ung bướu tiêu hóa", "Ung bướu hô hấp"
    ),
    "mat_hcm" to listOf(
        "Khúc xạ – Lasik/Smile", "Đục thủy tinh thể", "Glôcôm", "Võng mạc – Đái tháo đường",
        "Mộng – Quặm", "Nhi khoa mắt", "Tạo hình", "Chấn thương mắt"
    ),
    "chac2" to listOf(
        "Nội tổng quát", "Nhi", "Sản", "Tai mũi họng", "Da liễu", "Răng hàm mặt", "Xét nghiệm", "Siêu âm"
    ),
    "hang_xanh" to listOf(
        "Nội", "Nhi", "Sản", "Da liễu", "Cơ xương khớp", "TMH", "RHM", "Siêu âm – XN"
    ),
    "tam_phuc" to listOf(
        "Khám thai", "Siêu âm", "Hiếm muộn", "Phụ khoa tổng quát", "KHHGĐ", "Sản khoa"
    )
)

// Default specialties if hospital not found
private val defaultSpecialties = listOf("Nội tổng quát", "Ngoại tổng quát", "Nhi khoa")

// Service options (fixed)
private val serviceOptions = listOf(
    "Khám BHYT",
    "Khám dịch vụ (khám tư)"
)

// Clinics by specialty (linked data)
private val clinicsBySpecialty = mapOf(
    "Nội tổng quát" to listOf(
        "Phòng 101 - Nội tổng quát",
        "Phòng 102 - Nội tổng quát",
        "Phòng 103 - Nội tổng quát"
    ),
    "Hô hấp" to listOf(
        "Phòng 104 - Hô hấp",
        "Phòng 105 - Hô hấp"
    ),
    "Nội tiết" to listOf(
        "Phòng 106 - Nội tiết"
    ),
    "Ngoại thần kinh" to listOf(
        "Phòng 201 - Ngoại thần kinh",
        "Phòng 202 - Ngoại thần kinh"
    ),
    "Ngoại tổng quát" to listOf(
        "Phòng 203 - Ngoại tổng quát",
        "Phòng 204 - Ngoại tổng quát"
    ),
    "Sản khoa" to listOf(
        "Phòng 301 - Sản khoa",
        "Phòng 302 - Sản khoa",
        "Phòng 303 - Sản khoa"
    ),
    "Nhi khoa" to listOf(
        "Phòng 304 - Nhi",
        "Phòng 305 - Nhi",
        "Phòng 306 - Nhi"
    ),
    "Tai mũi họng" to listOf(
        "Phòng 401 - TMH",
        "Phòng 402 - TMH"
    ),
    "Tim mạch" to listOf(
        "Phòng 501 - Tim mạch",
        "Phòng 502 - Tim mạch",
        "Phòng 503 - Tim mạch"
    ),
    "Tim mạch can thiệp" to listOf(
        "Phòng 504 - Tim mạch can thiệp",
        "Phòng 505 - DSA"
    ),
    "Nội tim mạch" to listOf(
        "Phòng 506 - Nội tim mạch"
    ),
    "Da liễu" to listOf(
        "Phòng 601 - Da liễu",
        "Phòng 602 - Da liễu"
    ),
    "Cơ xương khớp" to listOf(
        "Phòng 701 - Cơ xương khớp",
        "Phòng 702 - YHCT"
    ),
    "Nội thần kinh" to listOf(
        "Phòng 801 - Nội thần kinh"
    ),
    "Chấn thương chỉnh hình" to listOf(
        "Phòng 901 - Chấn thương chỉnh hình",
        "Phòng 902 - Chỉnh hình"
    ),
    "Hồi sức cấp cứu" to listOf(
        "Phòng ICU-01",
        "Phòng ICU-02"
    ),
    "Hóa trị" to listOf(
        "Phòng Hóa 1",
        "Phòng Hóa 2",
        "Phòng Hóa 3"
    ),
    "Xạ trị" to listOf(
        "Phòng Xạ trị 1",
        "Phòng Xạ trị 2"
    ),
    "Phẫu thuật ung bướu" to listOf(
        "Phòng PT Ung bướu 1",
        "Phòng PT Ung bướu 2"
    ),
    "Khúc xạ – Lasik/Smile" to listOf(
        "Phòng Khúc xạ 1",
        "Phòng Lasik"
    ),
    "Đục thủy tinh thể" to listOf(
        "Phòng Phẫu thuật mắt 1",
        "Phòng Phẫu thuật mắt 2"
    ),
    "Glôcôm" to listOf(
        "Phòng Glôcôm"
    ),
    "Khám thai" to listOf(
        "Phòng Khám thai 1",
        "Phòng Khám thai 2"
    ),
    "Siêu âm" to listOf(
        "Phòng Siêu âm 1",
        "Phòng Siêu âm 2",
        "Phòng Siêu âm 3"
    ),
    "Hiếm muộn" to listOf(
        "Phòng Hiếm muộn"
    ),
    "Phụ khoa tổng quát" to listOf(
        "Phòng Phụ khoa 1",
        "Phòng Phụ khoa 2"
    )
)

// Default clinics if specialty not found
private val defaultClinicRooms = listOf(
    "Phòng 101",
    "Phòng 102",
    "Phòng 201"
)

// Sample dates (upcoming days)
private val availableDates = listOf(
    "Hôm nay - 10/11/2025",
    "Ngày mai - 11/11/2025",
    "Thứ 3 - 12/11/2025",
    "Thứ 4 - 13/11/2025",
    "Thứ 5 - 14/11/2025",
    "Thứ 6 - 15/11/2025",
    "Thứ 7 - 16/11/2025"
)

// Sample time slots
private val timeSlots = listOf(
    "07:00 - 08:00",
    "08:00 - 09:00",
    "09:00 - 10:00",
    "10:00 - 11:00",
    "11:00 - 12:00",
    "13:00 - 14:00",
    "14:00 - 15:00",
    "15:00 - 16:00",
    "16:00 - 17:00",
    "17:00 - 18:00"
)

/* ==== Screen ==== */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookingInformationScreen(
    modifier: Modifier = Modifier,
    hospitalId: String?,
    preselectedSpecialtyName: String? = null,
    onBackClick: () -> Unit = {},
    onContinueClick: (specialty: String, service: String, clinic: String, date: String, time: String) -> Unit = { _, _, _, _, _ -> }
) {
    // Find the hospital from the mock data
    val hospital = hospitalMockDetails.firstOrNull { it.id == hospitalId }
    
    // Get specialties for this hospital
    val hospitalSpecialties = specialtiesByHospitalId[hospitalId] ?: defaultSpecialties
    
    // Form state
    var selectedSpecialty by rememberSaveable { mutableStateOf("") }
    var selectedService by rememberSaveable { mutableStateOf("") }
    var selectedClinic by rememberSaveable { mutableStateOf("") }
    var selectedDate by rememberSaveable { mutableStateOf("") }
    var selectedTime by rememberSaveable { mutableStateOf("") }
    
    // Pre-select specialty if provided from specialty selection flow
    LaunchedEffect(preselectedSpecialtyName, hospitalSpecialties) {
        if (selectedSpecialty.isEmpty() && preselectedSpecialtyName != null) {
            // Check if this hospital supports the preselected specialty
            if (hospitalSpecialties.contains(preselectedSpecialtyName)) {
                selectedSpecialty = preselectedSpecialtyName
            }
        }
    }
    
    // Get available clinics for selected specialty
    val availableClinics = remember(selectedSpecialty) {
        if (selectedSpecialty.isEmpty()) {
            emptyList()
        } else {
            clinicsBySpecialty[selectedSpecialty] ?: defaultClinicRooms
        }
    }
    
    // Bottom sheet state
    var showBottomSheet by rememberSaveable { mutableStateOf(false) }
    var showWarningMessage by remember { mutableStateOf(false) }
    var bottomSheetOptions by remember { mutableStateOf<List<String>>(emptyList()) }
    var bottomSheetTitle by remember { mutableStateOf("") }
    var onOptionSelected by remember { mutableStateOf<(String) -> Unit>({}) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            StepHeaderWithAppBar(
                title = "Chọn Thông tin khám",
                currentStep = 0,
                onBackClick = onBackClick
            )
        },
        bottomBar = {
            BottomContinueButton(
                onClick = {
                    onContinueClick(
                        selectedSpecialty,
                        selectedService,
                        selectedClinic,
                        selectedDate,
                        selectedTime
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .background(BackgroundColor)
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            )
        },
        containerColor = BackgroundColor
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Hospital Info Card
            item {
                HospitalInfoCard(
                    hospitalName = hospital?.name ?: "Bệnh viện nhân dân Gia Định",
                    hospitalAddress = hospital?.subtitle ?: hospital?.address
                        ?: "Số 1 Nơ Trang Long, Phường Gia Định, TP. Hồ Chí Minh (Địa chỉ cũ: Số 1 Nơ Trang Long, Phường 7, Quận Bình Thạnh, TP.HCM)"
                )
            }

            // Form Fields in a single card
            item {
                BookingFormCard(
                    selectedSpecialty = selectedSpecialty,
                    selectedService = selectedService,
                    selectedClinic = selectedClinic,
                    selectedDate = selectedDate,
                    selectedTime = selectedTime,
                    onSpecialtyClick = {
                        bottomSheetTitle = "Chọn chuyên khoa"
                        bottomSheetOptions = hospitalSpecialties
                        onOptionSelected = { selected ->
                            selectedSpecialty = selected
                            selectedClinic = "" // Reset clinic when specialty changes
                            showBottomSheet = false
                        }
                        showBottomSheet = true
                    },
                    onServiceClick = {
                        bottomSheetTitle = "Chọn dịch vụ"
                        bottomSheetOptions = serviceOptions
                        onOptionSelected = { selected ->
                            selectedService = selected
                            showBottomSheet = false
                        }
                        showBottomSheet = true
                    },
                    onClinicClick = {
                        if (selectedSpecialty.isEmpty()) {
                            // Show warning if no specialty selected
                            showWarningMessage = true
                        } else {
                            bottomSheetTitle = "Chọn phòng khám"
                            bottomSheetOptions = availableClinics
                            onOptionSelected = { selected ->
                                selectedClinic = selected
                                showBottomSheet = false
                            }
                            showBottomSheet = true
                        }
                    },
                    onDateClick = {
                        bottomSheetTitle = "Chọn ngày khám"
                        bottomSheetOptions = availableDates
                        onOptionSelected = { selected ->
                            selectedDate = selected
                            showBottomSheet = false
                        }
                        showBottomSheet = true
                    },
                    onTimeClick = {
                        bottomSheetTitle = "Chọn giờ khám"
                        bottomSheetOptions = timeSlots
                        onOptionSelected = { selected ->
                            selectedTime = selected
                            showBottomSheet = false
                        }
                        showBottomSheet = true
                    }
                )
            }
        }
        
        // Bottom Sheet for selection
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { showBottomSheet = false },
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                containerColor = CardBackground
            ) {
                OptionBottomSheetContent(
                    title = bottomSheetTitle,
                    options = bottomSheetOptions,
                    onOptionClick = onOptionSelected
                )
            }
        }
        
        // Warning bottom sheet for clinic without specialty
        if (showWarningMessage) {
            ModalBottomSheet(
                onDismissRequest = { showWarningMessage = false },
                sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
                containerColor = CardBackground
            ) {
                WarningBottomSheetContent(
                    message = "Vui lòng chọn chuyên khoa trước",
                    onDismiss = { showWarningMessage = false }
                )
            }
        }
    }
}

/* ==== Step Header with App Bar ==== */
@Composable
private fun StepHeaderWithAppBar(
    title: String,
    currentStep: Int,
    onBackClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(PrimaryBlue)
            // Added statusBarsPadding() and padding(top = 20.dp) to move the top bar down slightly
            .padding(top = 20.dp)
    ) {
        // App Bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 4.dp)
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Text(
                text = title,
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
        }

        // Step Indicators
        StepIndicatorRow(currentStep = currentStep)
    }
}

@Composable
private fun StepIndicatorRow(currentStep: Int) {
    val steps = listOf(
        R.drawable.img_thong_tin,
        R.drawable.img_person,
        R.drawable.img_tich_trang,
        R.drawable.img_wallet
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 40.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Icon row with connectors
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            steps.forEachIndexed { index, iconRes ->
                // Step icon
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(
                            if (index == currentStep) Color.White else Color.Transparent
                        )
                        .border(
                            width = if (index == currentStep) 0.dp else 1.5.dp,
                            color = if (index == currentStep) Color.Transparent else Color.White.copy(alpha = 0.5f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = iconRes),
                        contentDescription = "Step ${index + 1}",
                        modifier = Modifier.size(20.dp),
                        colorFilter = ColorFilter.tint(
                            if (index == currentStep) PrimaryBlue else Color.White
                        )
                    )
                }

                // Connector line between icons
                if (index < steps.size - 1) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(1.dp)
                            .background(Color.White.copy(alpha = 0.35f))
                    )
                }
            }
        }
        
        // Small spacing below icons
        Spacer(modifier = Modifier.height(12.dp))
    }
}

/* ==== Hospital Info Card ==== */
@Composable
private fun HospitalInfoCard(
    hospitalName: String,
    hospitalAddress: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBackground
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = hospitalName,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary,
                lineHeight = 24.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = hospitalAddress,
                fontSize = 13.sp,
                fontWeight = FontWeight.Normal,
                color = TextSecondary,
                lineHeight = 18.sp
            )
        }
    }
}

/* ==== Booking Form Card ==== */
@Composable
private fun BookingFormCard(
    selectedSpecialty: String,
    selectedService: String,
    selectedClinic: String,
    selectedDate: String,
    selectedTime: String,
    onSpecialtyClick: () -> Unit,
    onServiceClick: () -> Unit,
    onClinicClick: () -> Unit,
    onDateClick: () -> Unit,
    onTimeClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(18.dp)
    ) {
        FormField(
            label = "Chuyên khoa",
            placeholder = "Chọn chuyên khoa",
            value = selectedSpecialty,
            onClick = onSpecialtyClick
        )

        FormField(
            label = "Dịch vụ",
            placeholder = "Chọn dịch vụ",
            value = selectedService,
            onClick = onServiceClick
        )

        FormField(
            label = "Phòng khám",
            placeholder = "Chọn phòng khám",
            value = selectedClinic,
            onClick = onClinicClick
        )

        FormField(
            label = "Ngày khám",
            placeholder = "Chọn ngày khám",
            value = selectedDate,
            onClick = onDateClick
        )

        FormField(
            label = "Giờ khám",
            placeholder = "Chọn giờ khám",
            value = selectedTime,
            onClick = onTimeClick
        )
    }
}

/* ==== Form Field ==== */
@Composable
private fun FormField(
    label: String,
    placeholder: String,
    value: String,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = TextPrimary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(CardBackground)
                .border(
                    width = 1.dp,
                    color = BorderColor,
                    shape = RoundedCornerShape(8.dp)
                )
                .clickable(onClick = onClick)
                .padding(horizontal = 14.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                text = value.ifEmpty { placeholder },
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                color = if (value.isEmpty()) TextPlaceholder else TextPrimary
            )
        }
    }
}

/* ==== Bottom Button ==== */
@Composable
private fun BottomContinueButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier.height(48.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = PrimaryBlue
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 2.dp
        )
    ) {
        Text(
            text = "TIẾP TỤC",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            letterSpacing = 1.sp
        )
    }
}

/* ==== Bottom Sheet Content ==== */
@Composable
private fun OptionBottomSheetContent(
    title: String,
    options: List<String>,
    onOptionClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = TextPrimary,
            modifier = Modifier.padding(vertical = 12.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(options.size) { index ->
                OptionItem(
                    text = options[index],
                    onClick = { onOptionClick(options[index]) }
                )
            }
        }
    }
}

@Composable
private fun WarningBottomSheetContent(
    message: String,
    onDismiss: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = message,
            fontSize = 16.sp,
            fontWeight = FontWeight.Medium,
            color = TextPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Button(
            onClick = onDismiss,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = PrimaryBlue
            )
        ) {
            Text(
                text = "Đã hiểu",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun OptionItem(
    text: String,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        color = Color.Transparent
    ) {
        Text(
            text = text,
            fontSize = 15.sp,
            fontWeight = FontWeight.Normal,
            color = TextPrimary,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 14.dp, horizontal = 8.dp)
        )
    }
}

/* ==== Preview ==== */
@Preview(showBackground = true)
@Composable
private fun BookingInformationScreenPreview() {
    Vactrack_ver1Theme {
        BookingInformationScreen(
            hospitalId = "gia_dinh",
            onBackClick = {},
            onContinueClick = { _, _, _, _, _ -> }
        )
    }
}
