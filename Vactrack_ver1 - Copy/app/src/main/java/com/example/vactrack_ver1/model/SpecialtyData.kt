package com.example.vactrack_ver1.model

import com.example.vactrack_ver1.R

/**
 * Data class representing a medical specialty
 */
data class Specialty(
    val id: String,
    val name: String,
    val iconRes: Int
)

/**
 * Complete list of available specialties with their icons
 */
val specialties = listOf(
    Specialty("gia_dinh", "Bác sĩ Gia Đình", R.drawable.img_bac_si_gia_dinh),
    Specialty("tieu_hoa", "Tiêu Hóa Gan Mật", R.drawable.img_tieu_hoa),
    Specialty("noi_tong_quat", "Nội Tổng Quát", R.drawable.img_noi_tong_quat),
    Specialty("noi_tiet", "Nội Tiết", R.drawable.img_noi_tiet),
    Specialty("da_lieu", "Da Liễu", R.drawable.img_da_lieu),
    Specialty("noi_tim_mach", "Nội Tim Mạch", R.drawable.img_noi_tim_mach),
    Specialty("noi_than_kinh", "Nội Thần Kinh", R.drawable.img_noi_than_kinh),
    Specialty("noi_xuong_khop", "Nội Cơ Xương Khớp", R.drawable.img_noi_xuong_khop),
    Specialty("tai_mui_hong", "Tai Mũi Họng", R.drawable.img_tai_mui_hong),
    Specialty("mat", "Mắt", R.drawable.img_mat),
    Specialty("noi_tieu_hoa", "Nội Tiêu Hóa", R.drawable.img_noi_tieu_hoa),
    Specialty("ho_hap", "Nội Hô Hấp", R.drawable.img_ho_hap),
    Specialty("tam_ly", "Tâm Lý", R.drawable.img_tam_ly),
    Specialty("tam_than_kinh", "Tâm Thần Kinh", R.drawable.img_tam_than_kinh),
    Specialty("rang_ham_mat", "Răng Hàm Mặt", R.drawable.img_rang_ham_mat)
)

/**
 * Mapping of hospital IDs to their supported specialties
 * This maps each hospital to a list of specialty IDs they offer
 */
val specialtiesByHospitalId: Map<String, List<String>> = mapOf(
    "gia_dinh" to listOf(
        "gia_dinh", "noi_tong_quat", "noi_tim_mach", "noi_tieu_hoa",
        "da_lieu", "tai_mui_hong", "mat", "rang_ham_mat"
    ),
    "quan_y_175" to listOf(
        "gia_dinh", "noi_tong_quat", "noi_tim_mach", "noi_than_kinh",
        "noi_xuong_khop", "ho_hap", "tai_mui_hong"
    ),
    "mat_hcm" to listOf(
        "mat", "noi_tong_quat"
    ),
    "ung_buou" to listOf(
        "noi_tong_quat", "noi_tieu_hoa", "ho_hap", "tam_ly"
    ),
    "tam_phuc" to listOf(
        "tam_ly", "tam_than_kinh", "noi_than_kinh", "noi_tong_quat"
    ),
    "hang_xanh" to listOf(
        "gia_dinh", "noi_tong_quat", "noi_tim_mach", "da_lieu",
        "noi_tiet", "noi_xuong_khop", "tai_mui_hong", "mat"
    ),
    "chac2" to listOf(
        "noi_tong_quat", "noi_tim_mach", "noi_tieu_hoa", "ho_hap",
        "noi_than_kinh", "noi_xuong_khop", "noi_tiet"
    )
)

/**
 * Helper function to get specialty name by ID
 * Returns null if specialty ID is not found
 */
fun getSpecialtyNameById(id: String): String? {
    return specialties.firstOrNull { it.id == id }?.name
}
