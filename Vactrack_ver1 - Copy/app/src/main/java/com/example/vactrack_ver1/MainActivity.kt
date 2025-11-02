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
import com.example.vactrack_ver1.view.profile_benh_nhan.ProfileBenhNhanScreen
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
                        onProfileClick = { navigateTo(MainDestination.ProfileBenhNhan) }
                    )

                    MainDestination.ProfileBenhNhan -> ProfileBenhNhanScreen(
                        modifier = Modifier.fillMaxSize(),
                        onBackClick = { navigateTo(MainDestination.Home) },
                        onCreateClick = { /* open sheet only */ },
                        onRegisterNew = { navigateTo(MainDestination.CreateProfile) }
                    )

                    MainDestination.CreateProfile -> CreateProfileScreen(
                        modifier = Modifier.fillMaxSize(),
                        onBackClick = { navigateTo(MainDestination.ProfileBenhNhan) },
                        onSubmit = { navigateTo(MainDestination.ProfileBenhNhan) }
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
    CreateProfile
}


