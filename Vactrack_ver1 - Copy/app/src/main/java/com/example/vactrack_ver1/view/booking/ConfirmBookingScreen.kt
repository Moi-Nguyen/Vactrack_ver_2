package com.example.vactrack_ver1.view.booking

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vactrack_ver1.R
import com.example.vactrack_ver1.ui.theme.Vactrack_ver1Theme
import java.text.NumberFormat
import java.util.Locale

/* ==== Theme tokens ==== */
private val PrimaryBlue = Color(0xFF5BB7CF)
private val BackgroundColor = Color(0xFFF5F9FA)
private val TextPrimary = Color(0xFF1F2937)
private val TextSecondary = Color(0xFF6B7280)
private val CardBackground = Color.White
private val BorderColor = Color(0xFFC8E6F5)

/* ==== Helper function to format currency ==== */
private fun formatCurrency(amount: Long): String {
    val locale = Locale.forLanguageTag("vi-VN")
    val formatter = NumberFormat.getNumberInstance(locale)
    return "${formatter.format(amount)}đ"
}

@Composable
fun ConfirmBookingScreen(
    hospitalName: String,
    hospitalAddress: String,
    patientName: String,
    doctorName: String? = null,
    specialtyName: String,
    serviceName: String,
    clinicName: String,
    visitDate: String,
    visitTime: String,
    fee: Long,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onContinueClick: () -> Unit = {},
    onPayClick: () -> Unit = {}
) {
    var referralCode by remember { mutableStateOf("") }
    val serviceFee = 5000L // Phí GDTT
    val totalFee = fee + serviceFee

    Surface(
        modifier = modifier.fillMaxSize(),
        color = BackgroundColor
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                StepHeaderWithAppBar(
                    title = "Xác nhận thông tin",
                    currentStep = 2, // Third step (check icon)
                    onBackClick = onBackClick
                )
            },
            bottomBar = {
                BottomPaymentButton(
                    onClick = onPayClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(BackgroundColor)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                )
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp)
                    .padding(top = 20.dp, bottom = 16.dp)
            ) {
                // Main confirmation card (booking summary)
                ConfirmationCard(
                    hospitalName = hospitalName,
                    hospitalAddress = hospitalAddress,
                    patientName = patientName,
                    doctorName = doctorName,
                    specialtyName = specialtyName,
                    serviceName = serviceName,
                    clinicName = clinicName,
                    visitDate = visitDate,
                    visitTime = visitTime,
                    fee = fee
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Payment card with QR code
                PaymentCard(
                    referralCode = referralCode,
                    onReferralCodeChange = { referralCode = it }
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Fee summary block
                FeeSummaryBlock(
                    fee = fee,
                    totalFee = totalFee
                )

                Spacer(modifier = Modifier.height(20.dp))
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
                fontSize = 22.sp,
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
                            color = if (index == currentStep) Color.Transparent else Color.White.copy(
                                alpha = 0.5f
                            ),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = iconRes),
                        contentDescription = "Step ${index + 1}",
                        modifier = Modifier.size(22.dp),
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

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun ConfirmationCard(
    hospitalName: String,
    hospitalAddress: String,
    patientName: String,
    doctorName: String?,
    specialtyName: String,
    serviceName: String,
    clinicName: String,
    visitDate: String,
    visitTime: String,
    fee: Long
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBackground
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Section 1: Hospital info
            Text(
                text = hospitalName,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryBlue,
                lineHeight = 24.sp
            )
            
            Spacer(modifier = Modifier.height(6.dp))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Địa chỉ:",
                    fontSize = 15.sp,
                    color = TextSecondary,
                    fontWeight = FontWeight.Medium
                )
                Text(
                    text = hospitalAddress,
                    fontSize = 15.sp,
                    color = TextSecondary,
                    lineHeight = 22.sp,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Section 2: Patient info
            Text(
                text = "Thông tin bệnh nhân",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryBlue
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = patientName,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Section 3: Booking info
            Text(
                text = "Thông tin đặt khám",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryBlue
            )
            
            Spacer(modifier = Modifier.height(8.dp))

            // Doctor name (if provided)
            if (doctorName != null) {
                Text(
                    text = doctorName,
                    fontSize = 15.sp,
                    color = TextPrimary,
                    lineHeight = 22.sp
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Specialty
            Text(
                text = specialtyName,
                fontSize = 15.sp,
                color = TextPrimary,
                lineHeight = 22.sp
            )
            
            Spacer(modifier = Modifier.height(8.dp))

            // Service
            Text(
                text = serviceName,
                fontSize = 15.sp,
                color = TextPrimary,
                lineHeight = 22.sp
            )
            
            Spacer(modifier = Modifier.height(8.dp))

            // Date and time
            Text(
                text = "$visitDate ($visitTime)",
                fontSize = 15.sp,
                color = TextPrimary,
                lineHeight = 22.sp
            )
            
            Spacer(modifier = Modifier.height(10.dp))

            // Fee
            Text(
                text = formatCurrency(fee),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryBlue
            )
        }
    }
}

@Composable
private fun PaymentCard(
    referralCode: String,
    onReferralCodeChange: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(
            containerColor = CardBackground
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Payment method section
            Text(
                text = "Phương thức thanh toán",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            // Payment method field (static for now)
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .background(
                        color = CardBackground,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = BorderColor,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = "Chuyển khoản",
                    fontSize = 15.sp,
                    color = TextPrimary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Referral code section
            Text(
                text = "Mã giới thiệu",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            OutlinedTextField(
                value = referralCode,
                onValueChange = onReferralCodeChange,
                modifier = Modifier.fillMaxWidth(),
                placeholder = {
                    Text(
                        text = "Nhập mã giới thiệu (nếu có)",
                        fontSize = 15.sp,
                        color = TextSecondary.copy(alpha = 0.6f)
                    )
                },
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedBorderColor = BorderColor,
                    focusedBorderColor = PrimaryBlue,
                    unfocusedContainerColor = CardBackground,
                    focusedContainerColor = CardBackground
                ),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            /* ═══════════════════════════════════════════════════════════════════
             * QR CODE SECTION - INSTRUCTIONS FOR NEW ASSET
             * ═══════════════════════════════════════════════════════════════════
             * 
             * PROBLEM: img_qr_code is a screenshot with a small QR in the middle
             * SOLUTION: Create a new clean QR asset called "qr_payment.png"
             * 
             * STEPS TO CREATE THE NEW ASSET:
             * 
             * 1. Generate a clean QR code image:
             *    - Use a QR generator (e.g., qr-code-generator.com, qrcode.pro)
             *    - Input your payment URL/data
             *    - Download as PNG, size: 512x512px or 1024x1024px
             * 
             * 2. Edit the image (optional but recommended):
             *    - Open in image editor (Paint, Photoshop, GIMP, etc.)
             *    - Ensure it's a SQUARE image (1:1 aspect ratio)
             *    - Add small white padding: ~20-30px on all sides
             *    - Final result: clean QR code centered with thin white margin
             * 
             * 3. Add to your Android project:
             *    - Save as "qr_payment.png" (lowercase, no spaces)
             *    - Copy to: app/src/main/res/drawable/
             *    - File name MUST be lowercase (Android requirement)
             * 
             * 4. Rebuild project:
             *    - In Android Studio: Build > Clean Project
             *    - Then: Build > Rebuild Project
             *    - R.drawable.qr_payment will be available
             * 
             * ═══════════════════════════════════════════════════════════════════ */

            // ✅ IDEAL VERSION - Using new clean QR asset (qr_payment.png)
            // Uncomment this when you've added the new asset to res/drawable/
            /*
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .height(280.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = CardBackground)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.qr_payment),
                        contentDescription = "Mã QR thanh toán",
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f),
                        contentScale = ContentScale.Fit
                    )
                }
            }
            */

            // ⚠️ FALLBACK VERSION - Zooming into old img_qr_code asset
            // This tries to crop out the white borders but is less ideal
            // Replace this with the version above once you have qr_payment.png
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp)
                    .height(280.dp),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                colors = CardDefaults.cardColors(containerColor = CardBackground)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    // Using Crop to zoom into center and remove white borders
                    // This is a workaround - a clean QR asset would be much better
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(8.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(R.drawable.img_qr_code),
                            contentDescription = "Mã QR thanh toán",
                            modifier = Modifier
                                .fillMaxSize()
                                .size(220.dp), // Force larger rendering
                            contentScale = ContentScale.Crop // Zoom in to remove white margins
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun FeeSummaryBlock(
    fee: Long,
    totalFee: Long
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Row 1: Tiền khám
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Tiền khám",
                fontSize = 15.sp,
                color = TextSecondary
            )
            Text(
                text = formatCurrency(fee),
                fontSize = 15.sp,
                color = TextSecondary
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Row 2: Tổng + Phí GDTT
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "Tổng + Phí GDTT",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = TextPrimary
            )
            Text(
                text = formatCurrency(totalFee),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = PrimaryBlue
            )
        }
    }
}

@Composable
private fun BottomPaymentButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp),
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = PrimaryBlue,
            contentColor = Color.White
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 2.dp,
            pressedElevation = 4.dp
        )
    ) {
        Text(
            text = "THANH TOÁN",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun ConfirmBookingScreenPreview() {
    Vactrack_ver1Theme {
        ConfirmBookingScreen(
            hospitalName = "Trung tâm nội tim mạch",
            hospitalAddress = "Số 1 Nơ Trang Long, Phường Gia Định, TP. Hồ Chí Minh",
            patientName = "Lê Đức Anh",
            doctorName = "BS.CKI. Đỗ Đăng Khoa",
            specialtyName = "Khám tim mạch",
            serviceName = "Khám với bác sĩ tim mạch",
            clinicName = "Phòng khám tim mạch",
            visitDate = "01/01/2026",
            visitTime = "6:00 - 7:00",
            fee = 300000L
        )
    }
}
