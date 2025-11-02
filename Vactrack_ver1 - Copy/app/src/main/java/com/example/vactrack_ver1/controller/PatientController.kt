package com.example.vactrack_ver1.controller

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.vactrack_ver1.view.profile_benh_nhan.Patient

object PatientController {
    val patients: SnapshotStateList<Patient> = mutableStateListOf()

    // chỉ số item đang chỉnh (null nếu không chỉnh)
    private var editingIndex: Int? = null

    fun addPatient(p: Patient) {
        // thêm mới lên đầu danh sách
        patients.add(0, p)
    }

    /** Gọi khi nhấn "Chỉnh sửa" trên list */
    fun startEditing(index: Int) {
        editingIndex = index
    }

    /** Lấy dữ liệu đang chỉnh để prefill form */
    fun getEditing(): Patient? = editingIndex?.let { i -> patients.getOrNull(i) }

    /** Áp dụng cập nhật: GHI ĐÈ phần tử cũ thay vì thêm mới */
    fun applyEdit(updated: Patient) {
        val i = editingIndex
        if (i != null && i in patients.indices) {
            patients[i] = updated
        }
        editingIndex = null
    }

    /** Hủy chế độ chỉnh sửa (khi back/đóng form) */
    fun cancelEditing() {
        editingIndex = null
    }

    fun clearAll() {
        patients.clear()
        editingIndex = null
    }
}
