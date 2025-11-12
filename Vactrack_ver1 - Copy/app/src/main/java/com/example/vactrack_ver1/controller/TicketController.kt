package com.example.vactrack_ver1.controller

import androidx.compose.runtime.mutableStateListOf

data class Ticket(
    val id: String,
    val hospitalName: String,
    val hospitalAddress: String,
    val patientName: String,
    val specialtyName: String,
    val serviceName: String,
    val clinicName: String,
    val visitDate: String,
    val visitTime: String,
    val fee: Long,
    val createdAt: Long,
    val status: TicketStatus = TicketStatus.PAID
)

enum class TicketStatus {
    PENDING,
    PAID,
    CANCELLED
}

object TicketController {
    private val _tickets = mutableStateListOf<Ticket>()
    val tickets: List<Ticket> get() = _tickets

    fun addPaidTicket(ticket: Ticket) {
        _tickets.add(0, ticket) // Add newest first
    }

    fun clearAll() {
        _tickets.clear()
    }
}
