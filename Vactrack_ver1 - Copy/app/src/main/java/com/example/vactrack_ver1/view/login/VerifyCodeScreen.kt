package com.example.vactrack_ver1.view.login

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vactrack_ver1.R
import com.example.vactrack_ver1.design.BrandPalette
import com.example.vactrack_ver1.ui.theme.Vactrack_ver1Theme
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import java.util.Locale

@Composable
fun VerifyCodeScreen(
    modifier: Modifier = Modifier,
    email: String,
    onBackClick: () -> Unit = {},
    onVerify: (code: String) -> Unit = {},
    onResend: () -> Unit = {}
) {
    val codeLength = 5
    var code by rememberSaveable { mutableStateOf(List(codeLength) { "" }) }
    val focusRequesters = remember { List(codeLength) { FocusRequester() } }

    val focusManager = LocalFocusManager.current

    LaunchedEffect(Unit) {
        focusRequesters.firstOrNull()?.requestFocus()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 24.dp, vertical = 16.dp)
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier
                .align(Alignment.TopStart)
                .padding(top = 10.dp)
                .shadow(elevation = 4.dp, shape = CircleShape, clip = false)
                .size(40.dp)
                .clip(CircleShape)
                .background(BrandPalette.MistWhite)
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Back",
                tint = BrandPalette.OceanBlue
            )
        }

        Column(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.onboarding_wordmark),
                contentDescription = "VacTrack branding",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp),
                contentScale = ContentScale.Fit
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Text(
                    text = "Check your email",
                    color = BrandPalette.DeepBlue,
                    style = MaterialTheme.typography.headlineSmall.copy(
                        fontWeight = FontWeight.SemiBold
                    )
                )

                val description = buildAnnotatedString {
                    append("We sent a reset link to ")
                    withStyle(
                        SpanStyle(
                            color = BrandPalette.DeepBlue,
                            fontWeight = FontWeight.SemiBold
                        )
                    ) {
                        append(email.lowercase(Locale.ENGLISH))
                    }
                    append(". Enter the 5 digit code mentioned in the email.")
                }
                Text(
                    text = description,
                    color = BrandPalette.SlateGrey,
                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 15.sp)
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                code.forEachIndexed { index, value ->
                    Spacer(modifier = Modifier.width(if (index == 0) 0.dp else 12.dp))
                    CodeCell(
                        value = value,
                        focusRequester = focusRequesters[index],
                        onValueChange = { digit ->
                            code = code.toMutableList().also { it[index] = digit }
                            if (digit.isNotEmpty() && index < codeLength - 1) {
                                focusRequesters[index + 1].requestFocus()
                            } else if (digit.isNotEmpty()) {
                                focusManager.clearFocus()
                            }
                        },
                        onBackspace = {
                            if (index > 0) {
                                focusRequesters[index - 1].requestFocus()
                                code = code.toMutableList().also { it[index - 1] = "" }
                            }
                        }
                    )
                }
            }

            Button(
                onClick = { onVerify(code.joinToString(separator = "")) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = BrandPalette.OceanBlue,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Verify Code",
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Haven't got the email yet?",
                    color = BrandPalette.SlateGrey,
                    fontSize = 14.sp
                )
                TextButton(onClick = onResend) {
                    Text(
                        text = "Resend email",
                        color = BrandPalette.OceanBlue,
                        fontWeight = FontWeight.SemiBold,
                        textDecoration = TextDecoration.Underline
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
private fun CodeCell(
    value: String,
    focusRequester: FocusRequester,
    onValueChange: (String) -> Unit,
    onBackspace: () -> Unit
) {
    var isFocused by remember { mutableStateOf(false) }
    val shape = RoundedCornerShape(16.dp)
    val filled = value.isNotEmpty()

    BasicTextField(
        value = value,
        onValueChange = { newValue ->
            val sanitized = newValue.takeLast(1)
            if (sanitized.length <= 1 && sanitized.all { it.isDigit() }) {
                onValueChange(sanitized)
            }
        },
        modifier = Modifier
            .size(58.dp)
            .focusRequester(focusRequester)
            .onFocusChanged { isFocused = it.isFocused }
            .onKeyEvent { event ->
                if (event.key == Key.Backspace && event.type == KeyEventType.KeyUp) {
                    if (value.isEmpty()) {
                        onBackspace()
                    } else {
                        onValueChange("")
                    }
                    true
                } else {
                    false
                }
            },
        textStyle = MaterialTheme.typography.headlineMedium.copy(
            color = BrandPalette.DeepBlue,
            textAlign = TextAlign.Center
        ),
        singleLine = true,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            capitalization = KeyboardCapitalization.None
        ),
        interactionSource = remember { MutableInteractionSource() },
        decorationBox = { innerTextField ->
            Box(
                modifier = Modifier
                    .shadow(if (isFocused) 10.dp else 2.dp, shape = shape, clip = true)
                    .background(Color.White, shape)
                    .border(
                        width = if (isFocused) 2.dp else 1.dp,
                        color = if (isFocused) BrandPalette.OceanBlue else BrandPalette.MistWhite,
                        shape = shape
                    )
                    .padding(vertical = 10.dp),
                contentAlignment = Alignment.Center
            ) {
                innerTextField()
            }
        }
    )
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun VerifyCodePreview() {
    Vactrack_ver1Theme {
        VerifyCodeScreen(
            email = "forexampple@gmail.com"
        )
    }
}
