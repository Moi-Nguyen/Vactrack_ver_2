# Hospital Card Fix Summary

## Overview
Fixed the hospital cards in the "Cơ sở y tế – Đặt khám nhiều nhất" section to prevent text overlapping and implement proper click navigation.

## Changes Made

### 1. **HospitalCard Composable** (`HomeScreen.kt`)

#### Layout Improvements:
- ✅ **Changed card height** from fixed `240.dp` to `wrapContentHeight()` for responsive sizing
- ✅ **Reduced spacing** from `10.dp` to `8.dp` between elements for better fit
- ✅ **Added horizontal padding** to text elements (`4.dp`) to prevent edge clipping
- ✅ **Increased address maxLines** from `1` to `2` to accommodate longer addresses
- ✅ **Added line height** to text styles for better readability
- ✅ **Fixed button height** to `40.dp` for consistent sizing
- ✅ **Added maxLines and overflow** to button text to prevent clipping

#### Click Functionality:
- ✅ **Added `onBookNowClick` callback** parameter to handle navigation
- ✅ **Connected button `onClick`** to the callback
- ✅ **Proper parameter passing** through the component hierarchy

### 2. **HospitalHighlightsSection** (`HomeScreen.kt`)
- ✅ Added `onBookNowClick: (HospitalHighlight) -> Unit` parameter
- ✅ Pass hospital object to callback when button is clicked
- ✅ Forward callback to each `HospitalCard`

### 3. **HomeContent** (`HomeScreen.kt`)
- ✅ Added `onHospitalBookNowClick` parameter
- ✅ Pass callback to `HospitalHighlightsSection`

### 4. **HomeScreenScaffold** (`HomeScreen.kt`)
- ✅ Added `onHospitalBookNowClick: (HospitalHighlight) -> Unit` parameter
- ✅ Forward callback to `HomeContent`

### 5. **MainActivity** 
- ✅ Implemented `onHospitalBookNowClick` handler in `MainDestination.Home`
- ✅ Sets `bookingHospitalId = hospital.id` when button is clicked
- ✅ Navigates to `BookingInformation` screen with the selected hospital

## Navigation Flow

```
User clicks "Đặt khám ngay" button on hospital card
    ↓
HospitalCard.onBookNowClick() is called
    ↓
Callback propagates: HospitalHighlightsSection → HomeContent → HomeScreenScaffold
    ↓
MainActivity receives hospital object
    ↓
Sets bookingHospitalId = hospital.id
    ↓
Navigates to BookingInformationScreen
    ↓
BookingInformationScreen receives hospitalId and displays the selected hospital
```

## Technical Details

### Card Layout Structure:
```kotlin
Card (wrapContentHeight, 200dp width)
└── Column (padding 16dp, spacing 8dp)
    ├── Image (80dp logo with 1.5x scale)
    ├── Spacer (4dp)
    ├── Text (Hospital name, max 2 lines)
    ├── Text (Address, max 2 lines)
    ├── RatingBar
    ├── Spacer (8dp)
    └── Button ("Đặt khám ngay", 40dp height)
```

### Key Features:
1. **Text Overflow Handling**: All text uses `maxLines` and `TextOverflow.Ellipsis`
2. **Responsive Sizing**: Card height adapts to content
3. **Proper Spacing**: Adequate padding prevents overlapping
4. **Click Navigation**: Each hospital card navigates to its specific booking screen
5. **Hospital Context**: Full hospital object passed through callbacks

## Testing Checklist

- [ ] Verify "Đặt khám ngay" button is fully visible on all cards
- [ ] Test with different screen sizes and font scales
- [ ] Confirm long hospital names don't overflow (ellipsis applied)
- [ ] Confirm long addresses are truncated properly (max 2 lines)
- [ ] Click "Đặt khám ngay" on each hospital card
- [ ] Verify BookingInformationScreen opens with correct hospital
- [ ] Test on different devices (phones, tablets)
- [ ] Check with accessibility features enabled (large text)

## Example Hospital Data

The following hospitals are displayed in the section:
- Bệnh viện Nhân Dân Gia Định (id: "gia_dinh")
- Bệnh viện Quân Y 175 (id: "quan_y_175")
- Bệnh viện Ung bướu TP. HCM (id: "ung_buou")
- Bệnh viện Mắt TP. HCM (id: "mat_hcm")
- PHÒNG KHÁM ĐA KHOA CHAC2 (id: "chac2")
- PHÒNG KHÁM ĐA KHOA HÀNG XANH (id: "hang_xanh")
- Phòng khám sản phụ khoa Tâm Phúc (id: "tam_phuc")

Each hospital now properly navigates to the booking screen when "Đặt khám ngay" is clicked.
