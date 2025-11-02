package com.example.vactrack_ver1.controller

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import com.example.vactrack_ver1.view.profile_benh_nhan.Patient

object PatientController {
    val patients: SnapshotStateList<Patient> = mutableStateListOf()

    private var editingIndex: Int? = null

    fun addPatient(p: Patient) {
        patients.add(0, p) // thêm lên đầu
    }

    fun startEditing(index: Int) {
        editingIndex = index
    }

    fun getEditing(): Patient? = editingIndex?.let { i -> patients.getOrNull(i) }

    fun applyEdit(updated: Patient) {
        val i = editingIndex
        if (i != null && i in patients.indices) {
            patients[i] = updated // GHI ĐÈ item cũ
        }
        editingIndex = null
    }

    fun cancelEditing() {
        editingIndex = null
    }

    fun clearAll() {
        patients.clear()
        editingIndex = null
    }
}
