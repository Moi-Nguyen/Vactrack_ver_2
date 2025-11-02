package com.example.vactrack_ver1.view.profile_benh_nhan

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class PatientViewModel : ViewModel() {
    private val _patients = MutableStateFlow<List<Patient>>(emptyList())
    val patients: StateFlow<List<Patient>> = _patients

    fun addPatient(p: Patient) {
        // thêm mới lên đầu danh sách
        _patients.value = listOf(p) + _patients.value
    }
}
