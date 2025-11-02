package com.example.vactrack_ver1.view.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vactrack_ver1.R
import com.example.vactrack_ver1.design.BrandPalette
import com.example.vactrack_ver1.ui.theme.Vactrack_ver1Theme

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onForgotPassword: () -> Unit = {},
    onSignUp: () -> Unit = {},
    onLogin: (email: String, password: String) -> Unit = { _, _ -> },
    onFacebook: () -> Unit = {},
    onGoogle: () -> Unit = {}
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var loginError by rememberSaveable { mutableStateOf(false) }
    val demoEmail = "demo@vactrack.com"
    val demoPassword = "Vactrack123"

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.onboarding_wordmark),
                contentDescription = "VacTrack wordmark",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp)
                    .height(120.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Sign in to your account",
                color = BrandPalette.DeepBlue,
                style = MaterialTheme.typography.headlineSmall.copy(
                    fontWeight = FontWeight.SemiBold
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            SocialRow(
                onFacebook = onFacebook,
                onGoogle = onGoogle
            )

            Spacer(modifier = Modifier.height(16.dp))

            DividerRow()

            Spacer(modifier = Modifier.height(16.dp))

            AuthTextField(
                value = email,
                onValueChange = {
                    email = it
                    loginError = false
                },
                placeholder = "demo@vactrack.com"
            )

            Spacer(modifier = Modifier.height(12.dp))

            AuthTextField(
                value = password,
                onValueChange = {
                    password = it
                    loginError = false
                },
                placeholder = "********",
                isPassword = true,
                passwordVisible = passwordVisible,
                onTogglePassword = { passwordVisible = !passwordVisible }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onForgotPassword) {
                    Text(
                        text = "Forget Password?",
                        color = BrandPalette.DeepBlue,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .background(Color.Transparent),
                shape = RoundedCornerShape(24.dp),
                color = BrandPalette.OceanBlue,
                onClick = {
                    val trimmedEmail = email.trim()
                    if (trimmedEmail.equals(demoEmail, ignoreCase = true) && password == demoPassword) {
                        loginError = false
                        onLogin(trimmedEmail, password)
                    } else {
                        loginError = true
                    }
                }
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "Log In",
                        color = Color.White,
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    )
                }
            }

            if (loginError) {
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = "Email hoặc mật khẩu không đúng. Sử dụng demo@vactrack.com / Vactrack123.",
                    color = Color(0xFFD32F2F),
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have account? ",
                    color = BrandPalette.SlateGrey,
                    fontSize = 14.sp
                )
                TextButton(onClick = onSignUp) {
                    Text(
                        text = "Sign Up",
                        color = BrandPalette.OceanBlue,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Demo account: $demoEmail / $demoPassword",
                color = BrandPalette.SlateGrey.copy(alpha = 0.7f),
                fontSize = 12.sp
            )
        }
    }
}

@Composable
private fun SocialRow(
    onFacebook: () -> Unit,
    onGoogle: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SocialButton(
            iconRes = R.drawable.img_facebook,
            label = "Facebook",
            backgroundColor = Color(0xFFF2F7FF),
            iconBackgroundColor = Color(0xFF1877F2),
            iconTint = Color.White,
            onClick = onFacebook
        )
        SocialButton(
            iconRes = R.drawable.img_google,
            label = "Google",
            backgroundColor = Color(0xFFFDF6F0),
            iconBackgroundColor = Color.White,
            iconTint = null,
            onClick = onGoogle
        )
    }
}

@Composable
private fun RowScope.SocialButton(
    iconRes: Int,
    label: String,
    backgroundColor: Color,
    iconBackgroundColor: Color,
    iconTint: Color?,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .weight(1f)
            .height(56.dp),
        shape = RoundedCornerShape(18.dp),
        color = backgroundColor,
        shadowElevation = 4.dp,
        onClick = onClick
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(CircleShape)
                    .background(iconBackgroundColor),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = iconRes),
                    contentDescription = "$label icon",
                    modifier = Modifier.size(20.dp),
                    contentScale = ContentScale.Fit,
                    colorFilter = iconTint?.let { androidx.compose.ui.graphics.ColorFilter.tint(it) }
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = label,
                color = BrandPalette.SlateGrey,
                fontWeight = FontWeight.SemiBold,
                fontSize = 15.sp
            )
        }
    }
}

@Composable
private fun DividerRow() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Divider(
            modifier = Modifier.weight(1f),
            color = BrandPalette.SlateGrey.copy(alpha = 0.25f)
        )
        Text(
            text = "  Or  ",
            color = BrandPalette.SlateGrey,
            fontSize = 13.sp
        )
        Divider(
            modifier = Modifier.weight(1f),
            color = BrandPalette.SlateGrey.copy(alpha = 0.25f)
        )
    }
}

@Composable
private fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    isPassword: Boolean = false,
    passwordVisible: Boolean = false,
    onTogglePassword: () -> Unit = {}
) {
    val colors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = BrandPalette.OceanBlue,
        unfocusedBorderColor = Color.Transparent,
        focusedContainerColor = BrandPalette.MistWhite,
        unfocusedContainerColor = BrandPalette.MistWhite,
        cursorColor = BrandPalette.DeepBlue,
        focusedTrailingIconColor = BrandPalette.SlateGrey,
        unfocusedTrailingIconColor = BrandPalette.SlateGrey,
        focusedPlaceholderColor = BrandPalette.SlateGrey.copy(alpha = 0.6f),
        unfocusedPlaceholderColor = BrandPalette.SlateGrey.copy(alpha = 0.6f),
        focusedTextColor = BrandPalette.SlateGrey,
        unfocusedTextColor = BrandPalette.SlateGrey
    )

    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = {
            Text(text = placeholder)
        },
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        singleLine = true,
        shape = RoundedCornerShape(18.dp),
        visualTransformation = if (isPassword && !passwordVisible) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        trailingIcon = {
            if (isPassword) {
                IconButton(onClick = onTogglePassword) {
                    Icon(
                        imageVector = if (passwordVisible) {
                            Icons.Outlined.Visibility
                        } else {
                            Icons.Outlined.VisibilityOff
                        },
                        contentDescription = if (passwordVisible) "Hide password" else "Show password"
                    )
                }
            }
        },
        colors = colors
    )
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun LoginScreenPreview() {
    Vactrack_ver1Theme {
        LoginScreen(modifier = Modifier.fillMaxSize())
    }
}
