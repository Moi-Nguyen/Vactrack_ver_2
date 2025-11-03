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
                        onProfileClick = { navigateTo(MainDestination.ProfileBenhNhan) },
                        onTicketClick = { navigateTo(MainDestination.TicketList) },
                        onNotificationClick = { navigateTo(MainDestination.NotificationList) },
                        onAccountClick = { navigateTo(MainDestination.Account) }
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
    ProfileBenhNhan,
    CreateProfile,
    PatientDetail,
    TicketList,
    NotificationList,
    Account
}


