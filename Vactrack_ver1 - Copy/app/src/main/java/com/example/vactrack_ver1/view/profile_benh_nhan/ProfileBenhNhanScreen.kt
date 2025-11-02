package com.example.vactrack_ver1.view.profile_benh_nhan

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vactrack_ver1.R
import com.example.vactrack_ver1.design.BrandPalette
import com.example.vactrack_ver1.ui.theme.Vactrack_ver1Theme
import com.example.vactrack_ver1.controller.PatientController

private data class PatientRecord(
    val name: String,
    val phone: String,
    val birthDate: String,
    val address: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileBenhNhanScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onCreateClick: () -> Unit = {},
    onRegisterNew: () -> Unit = {}
) {
    var showCreateSheet by rememberSaveable { mutableStateOf(false) }
    var showScanNotice by rememberSaveable { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    val title = "Hồ sơ bệnh nhân"
    val backDesc = "Quay lại"
    val createLabel = "Tạo mới"
    val registerLabel = "ĐĂNG KÝ MỚI"
    val scanLabel = "QUÉT MÃ BHYT/CCCD"
    val dialogTitle = "Thông báo"
    val dialogMessage = "Tính năng quét mã BHYT/CCCD hiện chưa khả dụng."
    val detailLabel = "Chi tiết"
    val editLabel = "Chỉnh sửa"

    // lấy dữ liệu động từ Controller
    val patientRecords = PatientController.patients.map {
        PatientRecord(
            name = it.name,
            phone = it.phone,
            birthDate = it.birthDate,
            address = it.address
        )
    }

    Surface(modifier = modifier.fillMaxSize(), color = Color(0xFFF3F9FF)) {
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
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = backDesc,
                                tint = Color.White
                            )
                        }
                        Text(
                            text = title,
                            modifier = Modifier
                                .weight(1f)
                                .padding(top = 6.dp),
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            textAlign = TextAlign.Center
                        )
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clickable {
                                    onCreateClick()
                                    showCreateSheet = true
                                }
                                .padding(vertical = 6.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_tao_moi),
                                contentDescription = createLabel,
                                modifier = Modifier.size(22.dp),
                                contentScale = ContentScale.Fit
                            )
                            Text(
                                text = createLabel,
                                color = Color.White,
                                style = MaterialTheme.typography.labelSmall
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                itemsIndexed(patientRecords) { index, record ->
                    PatientRecordCard(
                        record = record,
                        detailLabel = detailLabel,
                        editLabel = editLabel,
                        onEditClick = {
                            PatientController.startEditing(index) // << quan trọng
                            onRegisterNew() // điều hướng sang CreateProfileScreen
                        }
                    )
                }
            }
        }

        if (showCreateSheet) {
            ModalBottomSheet(
                onDismissRequest = { showCreateSheet = false },
                sheetState = sheetState,
                containerColor = Color.White,
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 20.dp),
                    verticalArrangement = Arrangement.spacedBy(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(width = 36.dp, height = 4.dp)
                            .clip(RoundedCornerShape(50))
                            .background(BrandPalette.SlateGrey.copy(alpha = 0.25f))
                    )
                    Text(
                        text = title,
                        color = BrandPalette.DeepBlue,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                    )
                    Button(
                        onClick = {
                            showCreateSheet = false
                            onRegisterNew()
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(24.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BrandPalette.OceanBlue,
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = registerLabel, style = MaterialTheme.typography.labelLarge.copy(fontSize = 16.sp))
                    }
                    OutlinedButton(
                        onClick = {
                            showCreateSheet = false
                            showScanNotice = true
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(24.dp),
                        border = BorderStroke(1.5.dp, BrandPalette.OceanBlue)
                    ) {
                        Text(text = scanLabel, color = BrandPalette.OceanBlue, style = MaterialTheme.typography.labelLarge.copy(fontSize = 16.sp))
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }

        if (showScanNotice) {
            AlertDialog(
                onDismissRequest = { showScanNotice = false },
                confirmButton = {
                    Button(onClick = { showScanNotice = false }, shape = RoundedCornerShape(18.dp)) {
                        Text("Đóng")
                    }
                },
                title = { Text(dialogTitle, color = BrandPalette.DeepBlue) },
                text = { Text(dialogMessage, color = BrandPalette.SlateGrey) },
                shape = RoundedCornerShape(24.dp),
                containerColor = Color.White,
                tonalElevation = 6.dp
            )
        }
    }
}

@Composable
private fun PatientRecordCard(
    record: PatientRecord,
    detailLabel: String,
    editLabel: String,
    onEditClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier.fillMaxWidth()
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
                    Text(text = detailLabel)
                }
                Button(
                    onClick = onEditClick,
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(50),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BrandPalette.OceanBlue,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = editLabel)
                }
            }
        }
    }
}

@Composable
private fun RecordRow(icon: ImageVector, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = BrandPalette.OceanBlue)
        Text(text = value, color = BrandPalette.SlateGrey, style = MaterialTheme.typography.bodyMedium)
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun ProfileBenhNhanPreview() {
    Vactrack_ver1Theme { ProfileBenhNhanScreen() }
}
