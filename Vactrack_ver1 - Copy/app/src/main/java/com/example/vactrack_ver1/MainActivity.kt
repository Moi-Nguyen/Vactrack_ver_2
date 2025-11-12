package com.example.vactrack_ver1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import com.example.vactrack_ver1.ui.theme.Vactrack_ver1Theme
import com.example.vactrack_ver1.controller.OnboardingController
import com.example.vactrack_ver1.view.login.LoginScreen
import com.example.vactrack_ver1.view.onboarding.OnboardingScreen
import com.example.vactrack_ver1.view.login.ForgotPasswordScreen
import com.example.vactrack_ver1.view.login.VerifyCodeScreen
import com.example.vactrack_ver1.view.login.PasswordResetScreen
import com.example.vactrack_ver1.view.login.NewPasswordScreen
import com.example.vactrack_ver1.view.login.PasswordUpdateSuccessScreen
import com.example.vactrack_ver1.view.home.HomeScreenScaffold
import com.example.vactrack_ver1.view.profile_benh_nhan.PatientDetailScreen
import com.example.vactrack_ver1.view.profile_benh_nhan.ProfileBenhNhanScreen
import com.example.vactrack_ver1.controller.PatientController
import com.example.vactrack_ver1.view.phieu_kham.TicketListScreen
import com.example.vactrack_ver1.view.notification.NotificationListScreen
import com.example.vactrack_ver1.view.account.AccountScreen
import com.example.vactrack_ver1.view.profile_benh_nhan.CreateProfileScreen
import com.example.vactrack_ver1.view.facility.FacilitySelectionScreen
import com.example.vactrack_ver1.view.facility.FacilityItem
import com.example.vactrack_ver1.view.facility.FacilityCategory
import com.example.vactrack_ver1.view.facility.HospitalDetailScreen
import com.example.vactrack_ver1.view.facility.hospitalMockDetails
import com.example.vactrack_ver1.view.booking.BookingInformationScreen
import com.example.vactrack_ver1.view.booking.SelectPatientScreen
import com.example.vactrack_ver1.view.booking.ConfirmBookingScreen
import com.example.vactrack_ver1.view.booking.PaymentSuccessScreen
import com.example.vactrack_ver1.view.specialty.SpecialtySelectionScreen
import com.example.vactrack_ver1.controller.Ticket
import com.example.vactrack_ver1.controller.TicketController
import com.example.vactrack_ver1.controller.TicketStatus
import com.example.vactrack_ver1.model.Specialty
import com.example.vactrack_ver1.model.specialtiesByHospitalId
import com.example.vactrack_ver1.model.getSpecialtyNameById
import com.example.vactrack_ver1.view.phieu_kham.TicketFilter

class MainActivity : ComponentActivity() {
    private val onboardingController = OnboardingController()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Vactrack_ver1Theme {
                var emailForReset by rememberSaveable { mutableStateOf("") }
                var currentDestination by rememberSaveable {
                    mutableStateOf(MainDestination.Onboarding.name)
                }
                var selectedPatientIndex by rememberSaveable { mutableStateOf(-1) }
                var selectedHospitalId by rememberSaveable { mutableStateOf<String?>(null) }
                var bookingHospitalId by rememberSaveable { mutableStateOf<String?>(null) }
                var selectedSpecialtyId by rememberSaveable { mutableStateOf<String?>(null) }
                var selectedTicketTab by rememberSaveable { mutableStateOf(TicketFilter.Paid) }
                
                // Booking confirmation data
                var bookingSpecialty by rememberSaveable { mutableStateOf("") }
                var bookingService by rememberSaveable { mutableStateOf("") }
                var bookingClinic by rememberSaveable { mutableStateOf("") }
                var bookingDate by rememberSaveable { mutableStateOf("") }
                var bookingTime by rememberSaveable { mutableStateOf("") }

                fun navigateTo(destination: MainDestination) {
                    currentDestination = destination.name
                }

                when (MainDestination.valueOf(currentDestination)) {
                    MainDestination.Onboarding -> OnboardingScreen(
                        controller = onboardingController,
                        modifier = Modifier.fillMaxSize(),
                        onPrimaryAction = { navigateTo(MainDestination.Login) }
                    )

                    MainDestination.Login -> LoginScreen(
                        modifier = Modifier.fillMaxSize(),
                        onForgotPassword = { navigateTo(MainDestination.ForgotPassword) },
                        onLogin = { email, _ ->
                            emailForReset = email
                            navigateTo(MainDestination.Home)
                        }
                    )

                    MainDestination.ForgotPassword -> ForgotPasswordScreen(
                        modifier = Modifier.fillMaxSize(),
                        onBackClick = { navigateTo(MainDestination.Login) },
                        onResetPassword = { email ->
                            emailForReset = email
                            navigateTo(MainDestination.VerifyCode)
                        }
                    )

                    MainDestination.VerifyCode -> VerifyCodeScreen(
                        modifier = Modifier.fillMaxSize(),
                        email = emailForReset,
                        onBackClick = { navigateTo(MainDestination.ForgotPassword) },
                        onResend = { /* TODO */ },
                        onVerify = {
                            navigateTo(MainDestination.PasswordReset)
                        }
                    )

                    MainDestination.PasswordReset -> PasswordResetScreen(
                        modifier = Modifier.fillMaxSize(),
                        onBackClick = { navigateTo(MainDestination.VerifyCode) },
                        onConfirm = { navigateTo(MainDestination.NewPassword) }
                    )

                    MainDestination.NewPassword -> NewPasswordScreen(
                        modifier = Modifier.fillMaxSize(),
                        onBackClick = { navigateTo(MainDestination.PasswordReset) },
                        onUpdate = { _, _ -> navigateTo(MainDestination.PasswordSuccess) }
                    )

                    MainDestination.PasswordSuccess -> PasswordUpdateSuccessScreen(
                        modifier = Modifier.fillMaxSize(),
                        onBackClick = { navigateTo(MainDestination.Login) },
                        onContinue = {
                            navigateTo(MainDestination.Home)
                        }
                    )

                    MainDestination.Home -> HomeScreenScaffold(
                        modifier = Modifier.fillMaxSize(),
                        onFacilityBookingClick = { navigateTo(MainDestination.FacilitySelection) },
                        onSpecialtyBookingClick = { navigateTo(MainDestination.SpecialtySelection) },
                        onTicketUnpaidClick = {
                            selectedTicketTab = TicketFilter.Unpaid
                            navigateTo(MainDestination.TicketList)
                        },
                        onTicketPaidClick = {
                            selectedTicketTab = TicketFilter.Paid
                            navigateTo(MainDestination.TicketList)
                        },
                        onHospitalBookNowClick = { hospital ->
                            // Set the hospital ID for booking
                            bookingHospitalId = hospital.id
                            // Navigate directly to booking information screen
                            navigateTo(MainDestination.BookingInformation)
                        },
                        onProfileClick = { navigateTo(MainDestination.ProfileBenhNhan) },
                        onTicketClick = { 
                            selectedTicketTab = TicketFilter.Paid
                            navigateTo(MainDestination.TicketList) 
                        },
                        onNotificationClick = { navigateTo(MainDestination.NotificationList) },
                        onAccountClick = { navigateTo(MainDestination.Account) }
                    )

                    MainDestination.FacilitySelection -> FacilitySelectionScreen(
                        modifier = Modifier.fillMaxSize(),
                        onBackClick = { navigateTo(MainDestination.Home) },
                        onDetailClick = { facility ->
                            facility.detailId?.let { detailId ->
                                selectedHospitalId = detailId
                                navigateTo(MainDestination.HospitalDetail)
                            }
                        },
                        onBookNowClick = { facility ->
                            bookingHospitalId = facility.detailId
                            navigateTo(MainDestination.BookingInformation)
                        }
                    )

                    MainDestination.HospitalDetail -> HospitalDetailScreen(
                        modifier = Modifier.fillMaxSize(),
                        hospitals = hospitalMockDetails,
                        initialHospitalId = selectedHospitalId,
                        onBackClick = { navigateTo(MainDestination.FacilitySelection) },
                        onBookNowClick = { hospital ->
                            bookingHospitalId = hospital.id
                            navigateTo(MainDestination.BookingInformation)
                        }
                    )

                    MainDestination.SpecialtySelection -> SpecialtySelectionScreen(
                        modifier = Modifier.fillMaxSize(),
                        onBackClick = { navigateTo(MainDestination.Home) },
                        onSpecialtySelected = { specialty ->
                            // Save selected specialty and filter hospitals
                            selectedSpecialtyId = specialty.id
                            navigateTo(MainDestination.SpecialtyHospitals)
                        }
                    )

                    MainDestination.SpecialtyHospitals -> {
                        // Filter hospitals that offer the selected specialty
                        val hospitalsForSpecialty = if (selectedSpecialtyId != null) {
                            hospitalMockDetails.filter { hospital ->
                                val supportedSpecialties = specialtiesByHospitalId[hospital.id] ?: emptyList()
                                selectedSpecialtyId in supportedSpecialties
                            }
                        } else {
                            hospitalMockDetails
                        }

                        // Convert HospitalDetail to FacilityItem with unique IDs
                        val facilitiesForSpecialty = hospitalsForSpecialty.mapIndexed { index, hospital ->
                            FacilityItem(
                                id = "facility_${hospital.id}_$index",
                                name = hospital.name,
                                address = hospital.subtitle,
                                type = FacilityCategory.Hospital,
                                detailId = hospital.id,
                                isSelected = false,
                                mapUrl = null
                            )
                        }

                        FacilitySelectionScreen(
                            modifier = Modifier.fillMaxSize(),
                            facilities = facilitiesForSpecialty,
                            onBackClick = { navigateTo(MainDestination.SpecialtySelection) },
                            onDetailClick = { facility ->
                                facility.detailId?.let { detailId ->
                                    selectedHospitalId = detailId
                                    navigateTo(MainDestination.HospitalDetail)
                                }
                            },
                            onBookNowClick = { facility ->
                                bookingHospitalId = facility.detailId
                                // Continue to booking with specialty pre-selected
                                navigateTo(MainDestination.BookingInformation)
                            }
                        )
                    }

                    MainDestination.BookingInformation -> {
                        // Convert specialty ID to name for pre-selection
                        val preselectedSpecialtyName = selectedSpecialtyId?.let { getSpecialtyNameById(it) }
                        
                        BookingInformationScreen(
                            modifier = Modifier.fillMaxSize(),
                            hospitalId = bookingHospitalId,
                            preselectedSpecialtyName = preselectedSpecialtyName,
                            onBackClick = { 
                                // Navigate back to previous screen based on flow
                                if (selectedSpecialtyId != null) {
                                    navigateTo(MainDestination.SpecialtyHospitals)
                                } else {
                                    navigateTo(MainDestination.FacilitySelection)
                                }
                            },
                            onContinueClick = { specialty, service, clinic, date, time ->
                                // Save booking data
                                bookingSpecialty = specialty
                                bookingService = service
                                bookingClinic = clinic
                                bookingDate = date
                                bookingTime = time
                                navigateTo(MainDestination.SelectPatient)
                            }
                        )
                    }

                    MainDestination.SelectPatient -> SelectPatientScreen(
                        modifier = Modifier.fillMaxSize(),
                        onBackClick = { navigateTo(MainDestination.BookingInformation) },
                        onContinueClick = { patientIndex ->
                            selectedPatientIndex = patientIndex
                            navigateTo(MainDestination.ConfirmBooking)
                        }
                    )

                    MainDestination.ConfirmBooking -> {
                        val hospital = hospitalMockDetails.find { it.id == bookingHospitalId }
                        val patient = PatientController.patients.getOrNull(selectedPatientIndex)
                        val hospitalName = hospital?.name ?: "Trung tâm y tế"
                        val hospitalAddress = hospital?.address ?: ""
                        val patientName = patient?.name ?: ""
                        val fee = 300000L // TODO: Calculate actual fee
                        
                        ConfirmBookingScreen(
                            modifier = Modifier.fillMaxSize(),
                            hospitalName = hospitalName,
                            hospitalAddress = hospitalAddress,
                            patientName = patientName,
                            doctorName = null, // TODO: Add doctor selection if needed
                            specialtyName = bookingSpecialty,
                            serviceName = bookingService,
                            clinicName = bookingClinic,
                            visitDate = bookingDate,
                            visitTime = bookingTime,
                            fee = fee,
                            onBackClick = { navigateTo(MainDestination.SelectPatient) },
                            onContinueClick = {
                                // Optional: can be used for other flows
                                navigateTo(MainDestination.Home)
                            },
                            onPayClick = {
                                // Save ticket when payment succeeds
                                TicketController.addPaidTicket(
                                    Ticket(
                                        id = System.currentTimeMillis().toString(),
                                        hospitalName = hospitalName,
                                        hospitalAddress = hospitalAddress,
                                        patientName = patientName,
                                        specialtyName = bookingSpecialty,
                                        serviceName = bookingService,
                                        clinicName = bookingClinic,
                                        visitDate = bookingDate,
                                        visitTime = bookingTime,
                                        fee = fee,
                                        createdAt = System.currentTimeMillis(),
                                        status = TicketStatus.PAID
                                    )
                                )
                                // Navigate to payment success screen
                                navigateTo(MainDestination.PaymentSuccess)
                            }
                        )
                    }

                    MainDestination.PaymentSuccess -> PaymentSuccessScreen(
                        modifier = Modifier.fillMaxSize(),
                        onBackClick = {
                            // Return to home after successful payment
                            navigateTo(MainDestination.Home)
                        }
                    )

                    MainDestination.ProfileBenhNhan -> ProfileBenhNhanScreen(
                        modifier = Modifier.fillMaxSize(),
                        onBackClick = { navigateTo(MainDestination.Home) },
                        onCreateClick = { /* open sheet only */ },
                        onRegisterNew = {
                            selectedPatientIndex = -1
                            navigateTo(MainDestination.CreateProfile)
                        },
                        onDetailClick = { index ->
                            selectedPatientIndex = index
                            navigateTo(MainDestination.PatientDetail)
                        }
                    )

                    MainDestination.CreateProfile -> CreateProfileScreen(
                        modifier = Modifier.fillMaxSize(),
                        onBackClick = { navigateTo(MainDestination.ProfileBenhNhan) },
                        onSubmit = { navigateTo(MainDestination.ProfileBenhNhan) }
                    )

                    MainDestination.PatientDetail -> {
                        val patient = PatientController.patients.getOrNull(selectedPatientIndex)
                        PatientDetailScreen(
                            modifier = Modifier.fillMaxSize(),
                            patient = patient,
                            index = selectedPatientIndex,
                            onBackClick = { navigateTo(MainDestination.ProfileBenhNhan) },
                            onEditClick = { index ->
                                if (index >= 0) {
                                    PatientController.startEditing(index)
                                    navigateTo(MainDestination.CreateProfile)
                                }
                            },
                            onConfirmClick = { navigateTo(MainDestination.ProfileBenhNhan) },
                            onDeleteClick = { index ->
                                PatientController.removePatient(index)
                                selectedPatientIndex = -1
                                navigateTo(MainDestination.ProfileBenhNhan)
                            }
                        )
                    }

                    MainDestination.TicketList -> TicketListScreen(
                        modifier = Modifier.fillMaxSize(),
                        initialTab = selectedTicketTab,
                        onBackClick = { navigateTo(MainDestination.Home) },
                        onHomeClick = { navigateTo(MainDestination.Home) },
                        onProfileClick = { navigateTo(MainDestination.ProfileBenhNhan) },
                        onTicketClick = { navigateTo(MainDestination.TicketList) },
                        onNotificationClick = { navigateTo(MainDestination.NotificationList) },
                        onAccountClick = { navigateTo(MainDestination.Account) }
                    )

                    MainDestination.NotificationList -> NotificationListScreen(
                        modifier = Modifier.fillMaxSize(),
                        onBackClick = { navigateTo(MainDestination.Home) },
                        onHomeClick = { navigateTo(MainDestination.Home) },
                        onProfileClick = { navigateTo(MainDestination.ProfileBenhNhan) },
                        onTicketClick = { navigateTo(MainDestination.TicketList) },
                        onNotificationClick = { navigateTo(MainDestination.NotificationList) },
                        onAccountClick = { navigateTo(MainDestination.Account) }
                    )

                    MainDestination.Account -> AccountScreen(
                        modifier = Modifier.fillMaxSize(),
                        onBackClick = { navigateTo(MainDestination.Home) },
                        onHomeClick = { navigateTo(MainDestination.Home) },
                        onProfileClick = { navigateTo(MainDestination.ProfileBenhNhan) },
                        onTicketClick = { navigateTo(MainDestination.TicketList) },
                        onNotificationClick = { navigateTo(MainDestination.NotificationList) },
                        onAccountClick = { navigateTo(MainDestination.Account) },
                        onLogout = {
                            selectedPatientIndex = -1
                            PatientController.clearAll()
                            navigateTo(MainDestination.Login)
                        }
                    )
                }
            }
        }
    }
}

private enum class MainDestination {
    Onboarding,
    Login,
    ForgotPassword,
    VerifyCode,
    PasswordReset,
    NewPassword,
    PasswordSuccess,
    Home,
    FacilitySelection,
    HospitalDetail,
    SpecialtySelection,
    SpecialtyHospitals,
    BookingInformation,
    SelectPatient,
    ConfirmBooking,
    PaymentSuccess,
    ProfileBenhNhan,
    CreateProfile,
    PatientDetail,
    TicketList,
    NotificationList,
    Account
}
