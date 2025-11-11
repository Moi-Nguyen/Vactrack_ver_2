# Code Snippets for Future Enhancements

## 1. Adding a Date Picker

Replace the date FormField with this implementation:

```kotlin
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberDatePickerState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DatePickerField(
    label: String,
    placeholder: String,
    selectedDate: String,
    onDateSelected: (String) -> Unit
) {
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium,
            color = TextPrimary,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { showDatePicker = true },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = CardBackground),
            elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, BorderColor, RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Text(
                    text = selectedDate.ifEmpty { placeholder },
                    fontSize = 14.sp,
                    color = if (selectedDate.isEmpty()) TextPlaceholder else TextPrimary
                )
            }
        }
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                Button(
                    onClick = {
                        datePickerState.selectedDateMillis?.let { millis ->
                            val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                            onDateSelected(formatter.format(Date(millis)))
                        }
                        showDatePicker = false
                    }
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                Button(onClick = { showDatePicker = false }) {
                    Text("Hủy")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }
}
```

## 2. Adding a Time Picker with Bottom Sheet

```kotlin
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberModalBottomSheetState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerField(
    label: String,
    placeholder: String,
    selectedTime: String,
    onTimeSelected: (String) -> Unit
) {
    var showTimePicker by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()
    
    // Available time slots
    val timeSlots = listOf(
        "08:00", "08:30", "09:00", "09:30", "10:00", "10:30",
        "11:00", "11:30", "13:00", "13:30", "14:00", "14:30",
        "15:00", "15:30", "16:00", "16:30", "17:00"
    )

    FormField(
        label = label,
        placeholder = placeholder,
        value = selectedTime,
        onClick = { showTimePicker = true }
    )

    if (showTimePicker) {
        ModalBottomSheet(
            onDismissRequest = { showTimePicker = false },
            sheetState = sheetState
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Chọn giờ khám",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(timeSlots) { time ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onTimeSelected(time)
                                    showTimePicker = false
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = if (time == selectedTime) PrimaryColor.copy(alpha = 0.1f) 
                                                else Color.White
                            )
                        ) {
                            Text(
                                text = time,
                                modifier = Modifier.padding(16.dp),
                                fontSize = 16.sp,
                                fontWeight = if (time == selectedTime) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }
        }
    }
}
```

## 3. Adding Dropdown/Bottom Sheet for Specialty Selection

```kotlin
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SpecialtyPickerField(
    label: String,
    placeholder: String,
    selectedSpecialty: String,
    hospitalId: String?,
    onSpecialtySelected: (String) -> Unit
) {
    var showPicker by remember { mutableStateOf(false) }
    
    // Get specialties from hospital data
    val hospital = hospitalMockDetails.firstOrNull { it.id == hospitalId }
    val specialties = hospital?.specialities ?: emptyList()

    FormField(
        label = label,
        placeholder = placeholder,
        value = selectedSpecialty,
        onClick = { showPicker = true }
    )

    if (showPicker) {
        ModalBottomSheet(
            onDismissRequest = { showPicker = false },
            sheetState = rememberModalBottomSheetState()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = "Chọn chuyên khoa",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
                
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(bottom = 32.dp)
                ) {
                    items(specialties) { specialty ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSpecialtySelected(specialty)
                                    showPicker = false
                                },
                            colors = CardDefaults.cardColors(
                                containerColor = if (specialty == selectedSpecialty) 
                                    PrimaryColor.copy(alpha = 0.1f) else Color.White
                            )
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = specialty,
                                    fontSize = 15.sp,
                                    fontWeight = if (specialty == selectedSpecialty) 
                                        FontWeight.Bold else FontWeight.Normal,
                                    modifier = Modifier.weight(1f)
                                )
                                if (specialty == selectedSpecialty) {
                                    Icon(
                                        painter = painterResource(R.drawable.img_tich_trang),
                                        contentDescription = "Selected",
                                        tint = PrimaryColor,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
```

## 4. Form Validation

Add this to your BookingInformationScreen:

```kotlin
// Inside BookingInformationScreen composable
val isFormValid = remember(
    selectedSpecialty, selectedService, selectedClinic, selectedDate, selectedTime
) {
    selectedSpecialty.isNotEmpty() &&
    selectedService.isNotEmpty() &&
    selectedClinic.isNotEmpty() &&
    selectedDate.isNotEmpty() &&
    selectedTime.isNotEmpty()
}

// Update the button
BottomContinueButton(
    onClick = {
        if (isFormValid) {
            onContinueClick()
        }
    },
    enabled = isFormValid,  // Add enabled parameter to button
    modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 20.dp, vertical = 16.dp)
)
```

Then modify BottomContinueButton:

```kotlin
@Composable
private fun BottomContinueButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier.height(50.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = PrimaryColor,
            disabledContainerColor = PrimaryColor.copy(alpha = 0.5f)
        ),
        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = 4.dp
        )
    ) {
        Text(
            text = "TIẾP TỤC",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            letterSpacing = 0.5.sp
        )
    }
}
```

## 5. Loading State

```kotlin
@Composable
fun BookingInformationScreen(
    modifier: Modifier = Modifier,
    hospitalId: String?,
    onBackClick: () -> Unit = {},
    onContinueClick: () -> Unit = {}
) {
    var isLoading by remember { mutableStateOf(false) }
    val hospital = hospitalMockDetails.firstOrNull { it.id == hospitalId }
    
    Box(modifier = modifier.fillMaxSize()) {
        // Your existing UI
        
        // Loading overlay
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.5f)),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PrimaryColor)
            }
        }
    }
}
```

## 6. Passing Booking Data to Next Screen

Create a data class in MainActivity:

```kotlin
// In MainActivity.kt
data class BookingData(
    val hospitalId: String,
    val hospitalName: String,
    val specialty: String,
    val service: String,
    val clinic: String,
    val date: String,
    val time: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var bookingData by rememberSaveable { mutableStateOf<BookingData?>(null) }
            
            // ...
            
            MainDestination.BookingInformation -> BookingInformationScreen(
                hospitalId = bookingHospitalId,
                onBackClick = { navigateTo(MainDestination.FacilitySelection) },
                onContinueClick = { data ->
                    bookingData = data
                    navigateTo(MainDestination.BookingConfirmation)
                }
            )
        }
    }
}
```

Update BookingInformationScreen signature:

```kotlin
@Composable
fun BookingInformationScreen(
    modifier: Modifier = Modifier,
    hospitalId: String?,
    onBackClick: () -> Unit = {},
    onContinueClick: (BookingData) -> Unit = {}  // Changed signature
) {
    // ... existing code
    
    BottomContinueButton(
        onClick = {
            val data = BookingData(
                hospitalId = hospitalId ?: "",
                hospitalName = hospital?.name ?: "",
                specialty = selectedSpecialty,
                service = selectedService,
                clinic = selectedClinic,
                date = selectedDate,
                time = selectedTime
            )
            onContinueClick(data)
        }
    )
}
```

## 7. Error Handling

```kotlin
var errorMessage by remember { mutableStateOf<String?>(null) }

// Show error dialog
errorMessage?.let { message ->
    AlertDialog(
        onDismissRequest = { errorMessage = null },
        title = { Text("Lỗi") },
        text = { Text(message) },
        confirmButton = {
            Button(onClick = { errorMessage = null }) {
                Text("OK")
            }
        }
    )
}
```

## 8. Animation for Progress Indicator

```kotlin
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween

@Composable
private fun StepIndicator(
    iconRes: Int,
    label: String,
    isActive: Boolean,
    isCompleted: Boolean,
    modifier: Modifier = Modifier
) {
    val scale by animateFloatAsState(
        targetValue = if (isActive) 1.1f else 1f,
        animationSpec = tween(300),
        label = "scale"
    )
    
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                }
                .clip(CircleShape)
                .background(
                    when {
                        isActive -> PrimaryColor
                        isCompleted -> PrimaryColor.copy(alpha = 0.7f)
                        else -> Color(0xFFF3F4F6)
                    }
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = iconRes),
                contentDescription = label,
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
```

## How to Use These Snippets

1. Copy the relevant snippet
2. Paste it into `BookingInformationScreen.kt`
3. Replace the placeholder FormField with the enhanced version
4. Add required imports at the top of the file
5. Test the functionality

All snippets follow Material Design 3 guidelines and match your app's design language!
