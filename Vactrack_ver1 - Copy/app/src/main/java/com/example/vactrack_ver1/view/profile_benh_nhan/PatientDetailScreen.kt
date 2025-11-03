package com.example.vactrack_ver1.view.profile_benh_nhan

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
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
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.vactrack_ver1.R
import com.example.vactrack_ver1.design.BrandPalette
import com.example.vactrack_ver1.ui.theme.Vactrack_ver1Theme
import java.util.Random

@Composable
fun PatientDetailScreen(
    patient: Patient?,
    index: Int,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onEditClick: (Int) -> Unit = {},
    onConfirmClick: () -> Unit = {},
    onDeleteClick: (Int) -> Unit = {}
) {
    var showDeleteConfirm by remember { mutableStateOf(false) }

    val screenTitle = "Xác nhận thông tin"
    val infoTitle = "Thông tin bệnh nhân"
    val qrTitle = "QR mã số bệnh nhân"
    val confirmLabel = "Xác nhận thông tin"
    val editLabel = "Chỉnh sửa"
    val deleteDesc = "Xóa hồ sơ"
    val deleteConfirmTitle = "XÁC NHẬN"
    val deleteConfirmMessage = "Bạn có muốn xóa hồ sơ này không?"
    val deleteConfirmLabel = "Xóa"
    val cancelLabel = "Hủy"
    val emptyFallback = "-"

    val patientCode = remember(patient, index) {
        patient?.nationalId?.takeIf { it.isNotBlank() }
            ?: "BN-${(index + 1).coerceAtLeast(1).toString().padStart(6, '0')}"
    }

    val fullAddress = remember(patient) {
        if (patient == null) {
            emptyFallback
        } else {
            listOf(
                patient.addressLine,
                patient.ward,
                patient.district,
                patient.province,
                patient.nationality
            ).filter { it.isNotBlank() }
                .takeIf { it.isNotEmpty() }
                ?.joinToString(", ")
                ?: emptyFallback
        }
    }

    val infoPairs = remember(patient, fullAddress) {
        listOf(
            "Họ và tên" to patient?.name.orEmpty(),
            "Mã số bệnh nhân" to patientCode,
            "Ngày sinh" to (patient?.birthDate.orEmpty()),
            "Giới tính" to (patient?.gender.orEmpty()),
            "Mã định danh/CCCD/Passport" to (patient?.nationalId.orEmpty()),
            "Mã bảo hiểm y tế" to (patient?.insurance.orEmpty()),
            "Nghề nghiệp" to (patient?.occupation.orEmpty()),
            "Số điện thoại" to (patient?.phone.orEmpty()),
            "Email" to (patient?.email?.ifBlank { "Chưa cập nhật" } ?: "Chưa cập nhật"),
            "Địa chỉ (Ghi trên CCCD)" to fullAddress
        ).map { (label, value) ->
            label to value.ifBlank { emptyFallback }
        }
    }

    val qrBitmap = remember(patientCode) { generatePseudoQrBitmap(patientCode, 480) }

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
                        IconButton(onClick = onBackClick) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Quay lại",
                                tint = Color.White
                            )
                        }
                        Text(
                            text = screenTitle,
                            modifier = Modifier
                                .weight(1f)
                                .padding(top = 6.dp),
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            textAlign = TextAlign.Center
                        )
                        IconButton(onClick = { if (patient != null) showDeleteConfirm = true }) {
                            Image(
                                painter = painterResource(R.drawable.img_thung_rac),
                                contentDescription = deleteDesc,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                    }
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                if (patient == null) {
                    EmptyStateCard()
                } else {
                    Column(
                        modifier = Modifier
                            .weight(1f, fill = false)
                            .verticalScroll(rememberScrollState()),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        PatientInfoCard(
                            title = infoTitle,
                            infoPairs = infoPairs
                        )
                        QrCodeCard(
                            title = qrTitle,
                            patientCode = patientCode,
                            bitmap = qrBitmap
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    Button(
                        onClick = onConfirmClick,
                        modifier = Modifier
                            .weight(1f)
                            .height(54.dp),
                        shape = RoundedCornerShape(28.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = BrandPalette.OceanBlue,
                            contentColor = Color.White
                        )
                    ) {
                        Text(
                            text = confirmLabel,
                            style = MaterialTheme.typography.labelLarge.copy(fontSize = 16.sp)
                        )
                    }
                    OutlinedButton(
                        onClick = { if (patient != null && index >= 0) onEditClick(index) },
                        modifier = Modifier
                            .weight(1f)
                            .height(54.dp),
                        shape = RoundedCornerShape(28.dp),
                        border = BorderStroke(1.5.dp, BrandPalette.OceanBlue),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = BrandPalette.OceanBlue)
                    ) {
                        Text(
                            text = editLabel,
                            style = MaterialTheme.typography.labelLarge.copy(fontSize = 16.sp)
                        )
                    }
                }
            }
        }
    }

    if (showDeleteConfirm && patient != null) {
        Dialog(onDismissRequest = { showDeleteConfirm = false }) {
            Surface(
                shape = RoundedCornerShape(28.dp),
                tonalElevation = 0.dp,
                shadowElevation = 12.dp,
                color = Color.White
            ) {
                Column(
                    modifier = Modifier
                        .widthIn(min = 280.dp)
                        .padding(horizontal = 24.dp, vertical = 28.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    Text(
                        text = deleteConfirmTitle,
                        color = BrandPalette.OceanBlue,
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 22.sp
                        ),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = deleteConfirmMessage,
                        color = BrandPalette.DeepBlue,
                        style = MaterialTheme.typography.bodyLarge.copy(fontSize = 16.sp),
                        textAlign = TextAlign.Center
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        OutlinedButton(
                            onClick = { showDeleteConfirm = false },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            shape = RoundedCornerShape(24.dp),
                            border = BorderStroke(1.5.dp, BrandPalette.OceanBlue),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = BrandPalette.OceanBlue)
                        ) {
                            Text(cancelLabel, fontSize = 16.sp)
                        }
                        Button(
                            onClick = {
                                showDeleteConfirm = false
                                onDeleteClick(index)
                            },
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp),
                            shape = RoundedCornerShape(24.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = BrandPalette.OceanBlue,
                                contentColor = Color.White
                            )
                        ) {
                            Text(deleteConfirmLabel, fontSize = 16.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun PatientInfoCard(
    title: String,
    infoPairs: List<Pair<String, String>>
) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null,
                    tint = BrandPalette.OceanBlue
                )
                Text(
                    text = title.uppercase(),
                    color = BrandPalette.OceanBlue,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 18.sp
                    )
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        border = BorderStroke(1.5.dp, BrandPalette.OceanBlue),
                        shape = RoundedCornerShape(18.dp)
                    )
                    .padding(horizontal = 16.dp, vertical = 18.dp)
            ) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    infoPairs.forEach { (label, value) ->
                        InfoRow(label = label, value = value)
                    }
                }
            }
        }
    }
}

@Composable
private fun QrCodeCard(
    title: String,
    patientCode: String,
    bitmap: ImageBitmap
) {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = title.uppercase(),
                color = BrandPalette.OceanBlue,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp
                )
            )
            Box(
                modifier = Modifier
                    .size(200.dp)
                    .background(Color.White, RoundedCornerShape(16.dp))
                    .border(
                        border = BorderStroke(1.2.dp, BrandPalette.OceanBlue.copy(alpha = 0.3f)),
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    bitmap = bitmap,
                    contentDescription = "Mã số bệnh nhân QR",
                    modifier = Modifier.fillMaxSize()
                )
            }
            Text(
                text = patientCode,
                color = BrandPalette.OceanBlue,
                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.SemiBold)
            )
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(
            text = label,
            modifier = Modifier.weight(1f),
            color = BrandPalette.SlateGrey,
            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
        )
        Text(
            text = value,
            modifier = Modifier.weight(1f),
            color = BrandPalette.DeepBlue,
            style = MaterialTheme.typography.bodyMedium.copy(fontSize = 15.sp)
        )
    }
}

@Composable
private fun EmptyStateCard() {
    Card(
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Không tìm thấy thông tin bệnh nhân.",
                color = BrandPalette.DeepBlue,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Vui lòng quay lại và chọn hồ sơ khác.",
                color = BrandPalette.SlateGrey,
                style = MaterialTheme.typography.bodySmall,
                textAlign = TextAlign.Center
            )
        }
    }
}

private fun generatePseudoQrBitmap(data: String, targetSize: Int): ImageBitmap {
    val modules = 33
    val quietZone = 4
    val cellSize = (targetSize / (modules + quietZone * 2)).coerceAtLeast(4)
    val size = (modules + quietZone * 2) * cellSize
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    val paint = Paint().apply { style = Paint.Style.FILL }
    canvas.drawColor(android.graphics.Color.WHITE)

    val random = Random(data.hashCode().toLong())
    val finderOrigins = listOf(
        0 to 0,
        modules - 7 to 0,
        0 to modules - 7
    )

    fun isFinder(x: Int, y: Int): Boolean {
        finderOrigins.forEach { (ox, oy) ->
            val localX = x - ox
            val localY = y - oy
            if (localX in 0 until 7 && localY in 0 until 7) {
                val border = localX == 0 || localX == 6 || localY == 0 || localY == 6
                val center = localX in 2..4 && localY in 2..4
                if (border || center) return true
            }
        }
        return false
    }

    for (y in 0 until modules) {
        for (x in 0 until modules) {
            val shouldFill = if (isFinder(x, y)) {
                true
            } else {
                random.nextBoolean()
            }
            if (shouldFill) {
                val px = (x + quietZone) * cellSize
                val py = (y + quietZone) * cellSize
                paint.color = android.graphics.Color.BLACK
                canvas.drawRect(
                    px.toFloat(),
                    py.toFloat(),
                    (px + cellSize).toFloat(),
                    (py + cellSize).toFloat(),
                    paint
                )
            }
        }
    }
    return bitmap.asImageBitmap()
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun PatientDetailPreview() {
    val sample = Patient(
        name = "Lê Đức Anh",
        phone = "0123456789",
        birthDate = "17/10/2005",
        gender = "Nam",
        nationalId = "abcdxyz-123123",
        insurance = "abxyzqgfjhi",
        occupation = "CEO tập đoàn Vingroup",
        email = "",
        nationality = "Việt Nam",
        ethnicity = "Kinh",
        province = "TP. Hồ Chí Minh",
        district = "Quận 12",
        ward = "Thạnh Lộc",
        addressLine = "02 Võ Oanh"
    )
    Vactrack_ver1Theme {
        PatientDetailScreen(
            patient = sample,
            index = 0
        )
    }
}
