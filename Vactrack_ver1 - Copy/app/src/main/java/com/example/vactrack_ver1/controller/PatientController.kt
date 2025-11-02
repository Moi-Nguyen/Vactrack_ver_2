package com.example.vactrack_ver1.controller

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.vactrack_ver1.view.profile_benh_nhan.Patient

object PatientController {
    val patients: SnapshotStateList<Patient> = mutableStateListOf()

    // Chỉ số của item đang chỉnh sửa (nếu có)
    var editingIndex: Int? = null
        private set

    fun addPatient(p: Patient) {
        patients.add(0, p)
    }

    fun startEditing(index: Int) {
        editingIndex = index
    }

    fun getEditing(): Patient? = editingIndex?.let { i -> patients.getOrNull(i) }

    fun applyEdit(updated: Patient) {
        editingIndex?.let { i ->
            if (i in patients.indices) {
                patients[i] = updated
            }
        }
        editingIndex = null
    }

    fun clearAll() {
        patients.clear()
        editingIndex = null
    }
}
