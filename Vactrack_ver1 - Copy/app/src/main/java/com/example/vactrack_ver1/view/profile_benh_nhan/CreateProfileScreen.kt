package com.example.vactrack_ver1.view.profile_benh_nhan

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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

// MVC: dùng Controller để lưu/đọc dữ liệu
import com.example.vactrack_ver1.controller.PatientController
import com.example.vactrack_ver1.view.profile_benh_nhan.Patient

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
    var address by rememberSaveable { mutableStateOf("") }

    // Prefill khi đang chỉnh sửa (chỉ những field đã lưu trong Patient)
    LaunchedEffect(Unit) {
        PatientController.getEditing()?.let { p ->
            if (fullName.isEmpty() && birthDate.isEmpty() && address.isEmpty()) {
                fullName = p.name
                birthDate = p.birthDate
                phone = p.phone
                // địa chỉ đã được ghép chuỗi khi lưu; nếu muốn tách lại, bạn có thể parse theo dấu phẩy
                address = p.address
            }
        }
    }

    val dobValid = birthDate.matches(Regex("^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/[0-9]{4}$"))
    val requiredValid =
        fullName.isNotBlank() && dobValid && gender.isNotBlank() && nationalId.isNotBlank() &&
                nationality.isNotBlank() && province.isNotBlank() && district.isNotBlank() && ward.isNotBlank() && address.isNotBlank()

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
                        IconButton(onClick = onBackClick) {
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
                    LabeledField(
                        label = "Họ và tên (có dấu)",
                        required = true,
                        value = fullName,
                        onValueChange = { fullName = it },
                        placeholder = "Nhập họ và tên..."
                    )
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        LabeledField(
                            label = "Ngày sinh",
                            required = true,
                            value = birthDate,
                            onValueChange = { birthDate = it },
                            placeholder = "dd/mm/yyyy",
                            keyboardType = KeyboardType.Number,
                            isError = birthDate.isNotEmpty() && !dobValid,
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
                    LabeledField(
                        label = "Mã định danh/CCCD/Passport",
                        required = true,
                        value = nationalId,
                        onValueChange = { nationalId = it },
                        placeholder = "Nhập mã định danh/CCCD/Passport..."
                    )
                    LabeledField(
                        label = "Mã bảo hiểm y tế",
                        required = false,
                        value = insurance,
                        onValueChange = { insurance = it },
                        placeholder = "Nhập mã bảo hiểm y tế..."
                    )
                    LabeledField(
                        label = "Nghề nghiệp",
                        required = false,
                        value = occupation,
                        onValueChange = { occupation = it },
                        placeholder = "Chọn nghề nghiệp..."
                    )
                    LabeledField(
                        label = "Số điện thoại",
                        required = false,
                        value = phone,
                        onValueChange = { phone = it },
                        placeholder = "09xxxxxxxx",
                        keyboardType = KeyboardType.Phone
                    )
                    LabeledField(
                        label = "Email (dùng để nhận phiếu khám)",
                        required = false,
                        value = email,
                        onValueChange = { email = it },
                        placeholder = "Nhập email...",
                        keyboardType = KeyboardType.Email
                    )
                    LabeledField(
                        label = "Quốc gia",
                        required = true,
                        value = nationality,
                        onValueChange = { nationality = it },
                        placeholder = defaultNationality
                    )
                    LabeledField(
                        label = "Dân tộc",
                        required = false,
                        value = ethnicity,
                        onValueChange = { ethnicity = it },
                        placeholder = "Chọn dân tộc..."
                    )
                }

                SectionCard(title = "Địa chỉ theo CCCD") {
                    LabeledField(
                        label = "Tỉnh/Thành phố",
                        required = true,
                        value = province,
                        onValueChange = { province = it },
                        placeholder = "Chọn Tỉnh/Thành phố..."
                    )
                    LabeledField(
                        label = "Quận/Huyện",
                        required = true,
                        value = district,
                        onValueChange = { district = it },
                        placeholder = "Chọn Quận/Huyện..."
                    )
                    LabeledField(
                        label = "Phường/Xã",
                        required = true,
                        value = ward,
                        onValueChange = { ward = it },
                        placeholder = "Chọn Phường/Xã..."
                    )
                    LabeledField(
                        label = "Số nhà/Tên đường/Ấp thôn xóm",
                        required = true,
                        value = address,
                        onValueChange = { address = it },
                        placeholder = "Chỉ nhập số nhà, tên đường, ấp thôn xóm..."
                    )
                }

                // Nút lưu: nếu đang chỉnh -> cập nhật; nếu không -> tạo mới
                Button(
                    onClick = {
                        val addressCombined = buildString {
                            append(address)
                            if (ward.isNotBlank()) append(", $ward")
                            if (district.isNotBlank()) append(", $district")
                            if (province.isNotBlank()) append(", $province")
                        }

                        val payload = Patient(
                            name = fullName,
                            phone = phone,
                            birthDate = birthDate,
                            address = addressCombined
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
                    Text(
                        text = "Tạo hồ sơ mới",
                        style = MaterialTheme.typography.labelLarge.copy(fontSize = 16.sp)
                    )
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
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = label,
                color = Color(0xFF4A4A4A),
                style = MaterialTheme.typography.labelLarge
            )
            if (required) {
                Text(
                    text = " *",
                    color = Color(0xFFD32F2F),
                    style = MaterialTheme.typography.labelLarge
                )
            }
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
            )
        )
        if (isError) {
            Text(
                text = "Định dạng ngày sinh phải là dd/mm/yyyy",
                color = Color(0xFFD32F2F),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun CreateProfilePreview() {
    Vactrack_ver1Theme {
        CreateProfileScreen()
    }
}
