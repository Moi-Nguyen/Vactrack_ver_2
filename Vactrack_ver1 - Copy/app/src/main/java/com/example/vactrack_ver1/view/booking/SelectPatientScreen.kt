package com.example.vactrack_ver1.view.booking

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Cake
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vactrack_ver1.R
import com.example.vactrack_ver1.controller.PatientController
import com.example.vactrack_ver1.ui.theme.Vactrack_ver1Theme

/* ==== Theme tokens ==== */
private val PrimaryBlue = Color(0xFF5BB7CF)
private val BackgroundColor = Color(0xFFF5F9FA)
private val TextPrimary = Color(0xFF1F2937)
private val TextSecondary = Color(0xFF6B7280)
private val CardBackground = Color.White

/* ==== Data Model ==== */
private data class PatientRecord(
    val name: String,
    val phone: String,
    val birthDate: String,
    val address: String
)

@Composable
fun SelectPatientScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onContinueClick: (Int) -> Unit = {}
) {
    var selectedPatientIndex by rememberSaveable { mutableIntStateOf(-1) }

    // Map từ Patient -> PatientRecord (ghép địa chỉ đầy đủ để hiển thị)
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

    Surface(
        modifier = modifier.fillMaxSize(),
        color = BackgroundColor
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                StepHeaderWithAppBar(
                    title = "Chọn Thông tin khám",
                    currentStep = 1, // Second step (person icon)
                    onBackClick = onBackClick
                )
            }
        ) { innerPadding ->
            if (patientRecords.isEmpty()) {
                // Empty state
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Chưa có hồ sơ bệnh nhân",
                        color = TextSecondary,
                        fontSize = 16.sp,
                        textAlign = TextAlign.Center
                    )
                }
            } else {
                // Show patient list
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                        .padding(horizontal = 20.dp, vertical = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    itemsIndexed(patientRecords) { index, record ->
                        PatientCard(
                            name = record.name,
                            phone = record.phone,
                            birthDate = record.birthDate,
                            address = record.address,
                            isSelected = selectedPatientIndex == index,
                            onClick = { 
                                selectedPatientIndex = index
                                onContinueClick(index)
                            }
                        )
                    }
                }
            }
        }
    }
}

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

@Composable
private fun PatientCard(
    name: String,
    phone: String,
    birthDate: String,
    address: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBackground
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isSelected) 6.dp else 3.dp
        ),
        border = if (isSelected) {
            androidx.compose.foundation.BorderStroke(2.dp, PrimaryBlue)
        } else null
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            // Name row with person icon
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Person,
                    contentDescription = null,
                    tint = PrimaryBlue,
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = name,
                    color = PrimaryBlue,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Phone row
            PatientInfoRow(
                icon = Icons.Filled.Phone,
                value = phone
            )

            // Birth date row
            PatientInfoRow(
                icon = Icons.Filled.Cake,
                value = birthDate
            )

            // Address row
            PatientInfoRow(
                icon = Icons.Filled.LocationOn,
                value = address
            )
        }
    }
}

@Composable
private fun PatientInfoRow(
    icon: ImageVector,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = PrimaryBlue,
            modifier = Modifier.size(20.dp)
        )
        Text(
            text = value,
            color = TextSecondary,
            fontSize = 14.sp
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun SelectPatientScreenPreview() {
    Vactrack_ver1Theme {
        SelectPatientScreen()
    }
}
