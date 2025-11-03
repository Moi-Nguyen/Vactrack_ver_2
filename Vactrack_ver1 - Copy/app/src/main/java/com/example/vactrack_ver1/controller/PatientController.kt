package com.example.vactrack_ver1.controller

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.vactrack_ver1.view.profile_benh_nhan.Patient

object PatientController {
    val patients: SnapshotStateList<Patient> = mutableStateListOf()

    private var editingIndex: Int? = null

    fun addPatient(p: Patient) {
        patients.add(0, p) // thêm vào đầu danh sách để hiển thị trước
    }

    fun startEditing(index: Int) {
        editingIndex = index
    }

    fun getEditing(): Patient? = editingIndex?.let { i -> patients.getOrNull(i) }

    fun applyEdit(updated: Patient) {
        val i = editingIndex
        if (i != null && i in patients.indices) {
            patients[i] = updated // ghi đè hồ sơ đang chỉnh sửa
        }
        editingIndex = null
    }

    fun removePatient(index: Int) {
        if (index !in patients.indices) return
        patients.removeAt(index)
        editingIndex = editingIndex?.let { current ->
            when {
                current == index -> null
                current > index -> current - 1
                else -> current
            }
        }
    }

    fun cancelEditing() {
        editingIndex = null
    }

    fun clearAll() {
        patients.clear()
        editingIndex = null
    }
}
