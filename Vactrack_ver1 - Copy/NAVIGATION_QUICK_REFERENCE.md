# Quick Reference: Booking Screen Navigation

## 🎯 How It Works

### From FacilitySelectionScreen (Hospital List)

When user taps the **"Đặt khám ngay"** button on any hospital card:

```kotlin
// In MainActivity.kt
MainDestination.FacilitySelection -> FacilitySelectionScreen(
    onBookNowClick = { facility ->
        bookingHospitalId = facility.detailId  // ✅ Save hospital ID
        navigateTo(MainDestination.BookingInformation)  // ✅ Navigate
    }
)
```

### From HospitalDetailScreen (Hospital Details)

When user taps the **"Đặt khám ngay"** button at the bottom of detail view:

```kotlin
// In MainActivity.kt
MainDestination.HospitalDetail -> HospitalDetailScreen(
    onBookNowClick = { hospital ->
        bookingHospitalId = hospital.id  // ✅ Save hospital ID
        navigateTo(MainDestination.BookingInformation)  // ✅ Navigate
    }
)
```

### BookingInformationScreen Receives Data

```kotlin
// In MainActivity.kt
MainDestination.BookingInformation -> BookingInformationScreen(
    modifier = Modifier.fillMaxSize(),
    hospitalId = bookingHospitalId,  // ✅ Pass the ID
    onBackClick = { navigateTo(MainDestination.FacilitySelection) },
    onContinueClick = {
        // TODO: Navigate to next step (e.g., patient selection)
    }
)
```

### Inside BookingInformationScreen

```kotlin
// In BookingInformationScreen.kt
@Composable
fun BookingInformationScreen(
    hospitalId: String?,  // ✅ Receives the ID
    onBackClick: () -> Unit,
    onContinueClick: () -> Unit
) {
    // ✅ Find hospital from mock data
    val hospital = hospitalMockDetails.firstOrNull { it.id == hospitalId }
    
    // ✅ Display hospital info
    HospitalInfoCard(
        hospitalName = hospital?.name ?: "Default Hospital",
        hospitalAddress = hospital?.subtitle ?: hospital?.address ?: ""
    )
    
    // ... rest of the UI
}
```

## 📊 Data Flow Diagram

```
┌─────────────────────────┐
│ FacilitySelectionScreen │
│   (Hospital List)       │
└───────────┬─────────────┘
            │
            │ User taps "Đặt khám ngay"
            │
            ▼
┌─────────────────────────┐
│    MainActivity         │
│  bookingHospitalId =    │
│    facility.detailId    │
└───────────┬─────────────┘
            │
            ▼
┌─────────────────────────┐
│ BookingInformationScreen│
│  Receives hospitalId    │
│  Looks up hospital data │
│  Displays form          │
└─────────────────────────┘

        OR

┌─────────────────────────┐
│  HospitalDetailScreen   │
│   (Detailed View)       │
└───────────┬─────────────┘
            │
            │ User taps "Đặt khám ngay"
            │
            ▼
┌─────────────────────────┐
│    MainActivity         │
│  bookingHospitalId =    │
│    hospital.id          │
└───────────┬─────────────┘
            │
            ▼
┌─────────────────────────┐
│ BookingInformationScreen│
│  Receives hospitalId    │
│  Looks up hospital data │
│  Displays form          │
└─────────────────────────┘
```

## 🔑 Key Variables

### In MainActivity.kt
```kotlin
var bookingHospitalId by rememberSaveable { mutableStateOf<String?>(null) }
```
This variable stores the selected hospital ID and persists across configuration changes.

### Hospital IDs Available
From `hospitalMockDetails`:
- `"gia_dinh"` - Bệnh viện Nhân Dân Gia Định
- `"quan_y_175"` - Bệnh viện Quân Y 175
- `"ung_buou_hcm"` - Bệnh viện Ung bướu TP. HCM
- `"mat_hcm"` - Bệnh viện Mắt TP. HCM
- `"chac2"` - Phòng khám đa khoa CHAC2
- `"hang_xanh"` - Phòng khám đa khoa Hàng Xanh
- `"tam_phuc"` - Phòng khám sản phụ khoa Tâm Phúc

## ✅ What's Already Working

1. ✅ Navigation routes defined in `MainDestination` enum
2. ✅ State variable `bookingHospitalId` for tracking selected hospital
3. ✅ `onBookNowClick` callbacks in both screens
4. ✅ Data passing via `hospitalId` parameter
5. ✅ Hospital lookup in `BookingInformationScreen`
6. ✅ UI displays correct hospital information
7. ✅ Back navigation returns to facility selection
8. ✅ All drawable resources (icons) exist

## 🚀 Next Steps (Optional Enhancements)

### 1. Implement Next Step After "TIẾP TỤC"
```kotlin
onContinueClick = {
    // Save booking info
    // Navigate to patient selection or confirmation
    navigateTo(MainDestination.ProfileBenhNhan)
}
```

### 2. Add Form Field Pickers
- Specialty dropdown/bottom sheet
- Service selection based on specialty
- Clinic selection
- Date picker (Material3 DatePicker)
- Time slot selector

### 3. Pass Booking Data Forward
Create a booking data class:
```kotlin
data class BookingInfo(
    val hospitalId: String,
    val specialty: String,
    val service: String,
    val clinic: String,
    val date: String,
    val time: String
)
```

Store in MainActivity and pass to next screens.

## 📱 Testing the Implementation

1. Run the app
2. Navigate to Home screen
3. Tap "Đặt khám" button
4. Browse hospitals in FacilitySelectionScreen
5. Tap "Đặt khám ngay" on any hospital
6. ✅ You should see the BookingInformationScreen with:
   - Correct hospital name and address
   - Progress indicator showing step 1
   - 5 form fields
   - "TIẾP TỤC" button

OR

1. Tap on a hospital card to view details
2. View HospitalDetailScreen
3. Scroll to bottom
4. Tap "Đặt khám ngay"
5. ✅ Same BookingInformationScreen appears with correct hospital data

## 🎉 Success!

Your booking information screen is fully integrated and functional! The navigation flows smoothly from hospital selection to booking form, and all hospital data is properly passed and displayed.
