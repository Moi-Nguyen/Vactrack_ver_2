package com.example.vactrack_ver1.view.profile_benh_nhan

data class Patient(
    val name: String,
    val phone: String,
    val birthDate: String,

    // Các thông tin thêm để prefill đầy đủ
    val gender: String = "",
    val nationalId: String = "",
    val insurance: String = "",
    val occupation: String = "",
    val email: String = "",
    val nationality: String = "Việt Nam",
    val ethnicity: String = "",

    // Địa chỉ: lưu riêng từng phần để khi sửa có thể prefill chính xác
    val province: String = "",
    val district: String = "",
    val ward: String = "",
    val addressLine: String = ""   // Số nhà/Tên đường/Ấp thôn xóm
)
