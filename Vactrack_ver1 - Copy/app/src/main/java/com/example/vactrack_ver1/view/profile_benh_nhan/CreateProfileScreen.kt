package com.example.vactrack_ver1.view.profile_benh_nhan

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vactrack_ver1.design.BrandPalette
import com.example.vactrack_ver1.ui.theme.Vactrack_ver1Theme
import com.example.vactrack_ver1.controller.PatientController
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Formats raw digit input into dd/MM/yyyy format.
 * User types only digits (e.g. 03011990) and slashes are auto-inserted.
 *
 * @param raw The raw input string from the TextField
 * @return Formatted string with slashes at positions 2 and 5 (e.g. "03/01/1990")
 */
private fun formatDobInput(raw: String): String {
    // Keep only digits, limit to 8 digits max
    val digitsOnly = raw.filter { it.isDigit() }.take(8)
    
    return buildString {
        digitsOnly.forEachIndexed { index, char ->
            // Insert '/' after day (position 2) and month (position 4)
            if (index == 2 || index == 4) {
                append('/')
            }
            append(char)
        }
    }
}

/**
 * Validates if the formatted date string is a valid date.
 *
 * @param formatted The formatted date string (should be dd/MM/yyyy)
 * @return true if the string is exactly 10 characters and represents a valid date, false otherwise
 */
private fun isValidDob(formatted: String): Boolean {
    // Only validate when we have a complete date string
    if (formatted.length != 10) return false
    
    return try {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        dateFormat.isLenient = false // Strict parsing - rejects invalid dates like 32/01/2000
        val parsedDate = dateFormat.parse(formatted)
        
        // Optional: Check that the date is not in the future
        val today = Date()
        parsedDate != null && !parsedDate.after(today)
    } catch (e: Exception) {
        false
    }
}
@Composable
fun CreateProfileScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onSubmit: () -> Unit = {}
) {
    val screenBackground = Color(0xFFF2F8FF)
    val defaultNationality = "Việt Nam"

    var fullName by rememberSaveable { mutableStateOf("") }
    var birthDate by rememberSaveable { mutableStateOf("") }
    var gender by rememberSaveable { mutableStateOf("") }
    var nationalId by rememberSaveable { mutableStateOf("") }
    var insurance by rememberSaveable { mutableStateOf("") }
    var occupation by rememberSaveable { mutableStateOf("") }
    var phone by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var nationality by rememberSaveable { mutableStateOf(defaultNationality) }
    var ethnicity by rememberSaveable { mutableStateOf("") }
    var province by rememberSaveable { mutableStateOf("") }
    var district by rememberSaveable { mutableStateOf("") }
    var ward by rememberSaveable { mutableStateOf("") }
    var address by rememberSaveable { mutableStateOf("") } // addressLine

    // Prefill TẤT CẢ field khi đang chỉnh sửa
    LaunchedEffect(Unit) {
        PatientController.getEditing()?.let { p ->
            // chỉ set khi lần đầu vào (tránh override khi user đang gõ)
            if (fullName.isEmpty() &&
                birthDate.isEmpty() &&
                gender.isEmpty() &&
                nationalId.isEmpty() &&
                insurance.isEmpty() &&
                occupation.isEmpty() &&
                phone.isEmpty() &&
                email.isEmpty() &&
                nationality.isNotEmpty() && // đã có default -> không ép
                ethnicity.isEmpty() &&
                province.isEmpty() &&
                district.isEmpty() &&
                ward.isEmpty() &&
                address.isEmpty()
            ) {
                fullName = p.name
                birthDate = p.birthDate
                gender = p.gender
                nationalId = p.nationalId
                insurance = p.insurance
                occupation = p.occupation
                phone = p.phone
                email = p.email
                nationality = p.nationality
                ethnicity = p.ethnicity
                province = p.province
                district = p.district
                ward = p.ward
                address = p.addressLine
            }
        }
    }

    // Updated DOB validation: only show error when input is complete but invalid
    val dobValid = isValidDob(birthDate)
    val dobShouldShowError = birthDate.length == 10 && !dobValid
    
    val requiredValid =
        fullName.isNotBlank() && dobValid && gender.isNotBlank() && nationalId.isNotBlank() &&
                nationality.isNotBlank() && province.isNotBlank() && district.isNotBlank() &&
                ward.isNotBlank() && address.isNotBlank()

    Surface(modifier = modifier.fillMaxSize(), color = screenBackground) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                Surface(color = BrandPalette.OceanBlue) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp)
                            .padding(horizontal = 20.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = {
                            PatientController.cancelEditing()  // nếu back khi đang sửa
                            onBackClick()
                        }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Quay lại",
                                tint = Color.White
                            )
                        }
                        Text(
                            text = "Tạo mới hồ sơ",
                            modifier = Modifier
                                .weight(1f)
                                .padding(top = 6.dp),
                            color = Color.White,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.width(42.dp))
                    }
                }
            }
        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(screenBackground)
                    .verticalScroll(rememberScrollState())
                    .padding(innerPadding)
                    .padding(horizontal = 16.dp, vertical = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                SectionCard(title = "Thông tin chung") {
                    LabeledField("Họ và tên (có dấu)", true, fullName, { fullName = it }, "Nhập họ và tên...")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // DOB field with auto-formatting
                        LabeledField(
                            label = "Ngày sinh",
                            required = true,
                            value = birthDate,
                            onValueChange = { newValue ->
                                // Auto-format the input: digits only, insert slashes automatically
                                birthDate = formatDobInput(newValue)
                            },
                            placeholder = "dd/mm/yyyy",
                            keyboardType = KeyboardType.Number,
                            isError = dobShouldShowError,
                            supportingText = "Định dạng ngày sinh phải là dd/mm/yyyy",
                            modifier = Modifier.weight(1f)
                        )
                        LabeledField(
                            label = "Giới tính",
                            required = true,
                            value = gender,
                            onValueChange = { gender = it },
                            placeholder = "Chọn giới tính...",
                            modifier = Modifier.weight(1f)
                        )
                    }
                    LabeledField("Mã định danh/CCCD/Passport", true, nationalId, { nationalId = it }, "Nhập mã định danh/CCCD/Passport...")
                    LabeledField("Mã bảo hiểm y tế", false, insurance, { insurance = it }, "Nhập mã bảo hiểm y tế...")
                    LabeledField("Nghề nghiệp", false, occupation, { occupation = it }, "Chọn nghề nghiệp...")
                    LabeledField("Số điện thoại", false, phone, { phone = it }, "09xxxxxxxx", keyboardType = KeyboardType.Phone)
                    LabeledField("Email (dùng để nhận phiếu khám)", false, email, { email = it }, "Nhập email...", keyboardType = KeyboardType.Email)
                    LabeledField("Quốc gia", true, nationality, { nationality = it }, defaultNationality)
                    LabeledField("Dân tộc", false, ethnicity, { ethnicity = it }, "Chọn dân tộc...")
                }

                SectionCard(title = "Địa chỉ theo CCCD") {
                    LabeledField("Tỉnh/Thành phố", true, province, { province = it }, "Chọn Tỉnh/Thành phố...")
                    LabeledField("Quận/Huyện", true, district, { district = it }, "Chọn Quận/Huyện...")
                    LabeledField("Phường/Xã", true, ward, { ward = it }, "Chọn Phường/Xã...")
                    LabeledField("Số nhà/Tên đường/Ấp thôn xóm", true, address, { address = it }, "Chỉ nhập số nhà, tên đường, ấp thôn xóm...")
                }

                Button(
                    onClick = {
                        val payload = Patient(
                            name = fullName,
                            phone = phone,
                            birthDate = birthDate,
                            gender = gender,
                            nationalId = nationalId,
                            insurance = insurance,
                            occupation = occupation,
                            email = email,
                            nationality = nationality,
                            ethnicity = ethnicity,
                            province = province,
                            district = district,
                            ward = ward,
                            addressLine = address
                        )

                        if (PatientController.getEditing() != null) {
                            PatientController.applyEdit(payload) // cập nhật
                        } else {
                            PatientController.addPatient(payload) // thêm mới
                        }

                        onSubmit()
                    },
                    enabled = requiredValid,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = BrandPalette.OceanBlue,
                        contentColor = Color.White,
                        disabledContainerColor = BrandPalette.OceanBlue.copy(alpha = 0.4f),
                        disabledContentColor = Color.White
                    )
                ) {
                    Text("Tạo hồ sơ mới", style = MaterialTheme.typography.labelLarge.copy(fontSize = 16.sp))
                }
            }
        }
    }
}

@Composable
private fun SectionCard(
    title: String,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .border(BorderStroke(1.dp, Color(0xFFE1EEF9)), RoundedCornerShape(24.dp)),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier
                .background(Color.White)
                .padding(horizontal = 20.dp, vertical = 18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = title,
                color = BrandPalette.DeepBlue,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp
                )
            )
            content()
        }
    }
}

@Composable
private fun LabeledField(
    label: String,
    required: Boolean,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    isError: Boolean = false,
    supportingText: String? = null,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = label, color = Color(0xFF4A4A4A), style = MaterialTheme.typography.labelLarge)
            if (required) Text(text = " *", color = Color(0xFFD32F2F), style = MaterialTheme.typography.labelLarge)
        }
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            singleLine = true,
            placeholder = { Text(placeholder) },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            isError = isError,
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            visualTransformation = VisualTransformation.None,
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = BrandPalette.OceanBlue,
                unfocusedBorderColor = Color(0xFFD6E6F4),
                errorBorderColor = Color(0xFFD32F2F),
                cursorColor = BrandPalette.OceanBlue,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White
            ),
            supportingText = if (supportingText != null) {
                {
                    Text(
                        text = supportingText,
                        color = if (isError) Color(0xFFD32F2F) else Color(0xFF666666),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            } else null
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun CreateProfilePreview() {
    Vactrack_ver1Theme { CreateProfileScreen() }
}
