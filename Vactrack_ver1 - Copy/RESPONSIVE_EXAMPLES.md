# 🎯 Responsive Refactoring Examples

## Example 1: BookingInformationScreen (Form Screen)

### Before (Non-Responsive):
```kotlin
@Composable
fun BookingInformationScreen(
    hospitalId: String?,
    modifier: Modifier = Modifier
) {
    Scaffold(modifier = modifier.fillMaxSize()) { padding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp), // ❌ Fixed padding
            verticalArrangement = Arrangement.spacedBy(16.dp) // ❌ Fixed spacing
        ) {
            item {
                Text(
                    text = "Thông tin đặt khám",
                    fontSize = 24.sp, // ❌ Hard-coded size
                    fontWeight = FontWeight.Bold
                )
            }
            
            item {
                Spacer(modifier = Modifier.height(24.dp)) // ❌ Fixed spacing
            }
            
            item {
                SelectionCard(
                    title = "Chuyên khoa",
                    selectedValue = selectedSpecialty,
                    onClick = { showSpecialtyPicker = true }
                )
            }
            
            // More items...
        }
    }
}
```

### After (Fully Responsive):
```kotlin
@Composable
fun BookingInformationScreen(
    hospitalId: String?,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onContinueClick: (String, String, String, String, String) -> Unit = { _, _, _, _, _ -> }
) {
    // Add responsive utilities import
    val horizontalPadding = responsiveHorizontalPadding()
    val spacingSmall = responsiveSpacingSmall()
    val spacingMedium = responsiveSpacingMedium()
    val spacingLarge = responsiveSpacingLarge()
    val maxContentWidth = getMaxContentWidth()
    
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "Thông tin đặt khám",
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = maxContentWidth) // ✅ Constrain on tablets
                    .align(Alignment.TopCenter), // ✅ Center on large screens
                contentPadding = PaddingValues(
                    horizontal = horizontalPadding,
                    vertical = spacingMedium
                ),
                verticalArrangement = Arrangement.spacedBy(spacingMedium)
            ) {
                // Step indicator
                item {
                    StepIndicator(
                        currentStep = 1,
                        totalSteps = 3,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                
                item {
                    Spacer(modifier = Modifier.height(spacingSmall))
                }
                
                // Hospital Info Card
                item {
                    HospitalInfoCard(
                        hospitalId = hospitalId,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                
                item {
                    Spacer(modifier = Modifier.height(spacingSmall))
                }
                
                // Section Title
                item {
                    Text(
                        text = "Thông tin khám bệnh",
                        style = MaterialTheme.typography.titleMedium, // ✅ Use theme
                        fontWeight = FontWeight.Bold,
                        maxLines = 1 // ✅ Protect from overflow
                    )
                }
                
                // Specialty Selection
                item {
                    SelectionCard(
                        title = "Chuyên khoa",
                        selectedValue = selectedSpecialty,
                        placeholder = "Chọn chuyên khoa",
                        onClick = { showSpecialtyPicker = true },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                
                // Service Selection
                item {
                    SelectionCard(
                        title = "Dịch vụ khám",
                        selectedValue = selectedService,
                        placeholder = "Chọn dịch vụ",
                        onClick = { showServicePicker = true },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = selectedSpecialty.isNotBlank()
                    )
                }
                
                // Clinic Selection
                item {
                    SelectionCard(
                        title = "Phòng khám",
                        selectedValue = selectedClinic,
                        placeholder = "Chọn phòng khám",
                        onClick = { showClinicPicker = true },
                        modifier = Modifier.fillMaxWidth(),
                        enabled = selectedService.isNotBlank()
                    )
                }
                
                // Date Selection
                item {
                    DateSelectionCard(
                        selectedDate = selectedDate,
                        onDateSelected = { selectedDate = it },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                
                // Time Selection
                item {
                    TimeSlotGrid(
                        selectedTime = selectedTime,
                        onTimeSelected = { selectedTime = it },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                
                // Bottom spacing before button
                item {
                    Spacer(modifier = Modifier.height(spacingLarge))
                }
                
                // Continue Button
                item {
                    Button(
                        onClick = {
                            if (isFormValid()) {
                                onContinueClick(
                                    selectedSpecialty,
                                    selectedService,
                                    selectedClinic,
                                    selectedDate,
                                    selectedTime
                                )
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = 56.dp), // ✅ Minimum tappable height
                        enabled = isFormValid(),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Text(
                            text = "Tiếp tục",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
                
                // Extra bottom padding for navigation bar
                item {
                    Spacer(modifier = Modifier.height(spacingMedium))
                }
            }
        }
    }
}

// Reusable Selection Card Component
@Composable
private fun SelectionCard(
    title: String,
    selectedValue: String,
    placeholder: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (enabled) Color.White else Color.LightGray.copy(alpha = 0.3f)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(responsiveSpacingMedium())
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelMedium,
                color = BrandPalette.SlateGrey,
                maxLines = 1
            )
            
            Spacer(modifier = Modifier.height(responsiveSpacingSmall()))
            
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = selectedValue.ifBlank { placeholder },
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (selectedValue.isBlank()) 
                        BrandPalette.SlateGrey.copy(alpha = 0.5f) 
                    else 
                        BrandPalette.DeepBlue,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = null,
                    tint = BrandPalette.OceanBlue,
                    modifier = Modifier.size(responsiveIconSize(baseSize = 20.dp))
                )
            }
        }
    }
}
```

---

## Example 2: TicketListScreen (List Screen)

### Before (Non-Responsive):
```kotlin
@Composable
fun TicketListScreen(modifier: Modifier = Modifier) {
    Scaffold(modifier = modifier.fillMaxSize()) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(20.dp) // ❌ Fixed padding
        ) {
            // Tab Row
            TabRow(selectedTabIndex = selectedTab.ordinal) {
                TicketFilter.values().forEach { filter ->
                    Tab(
                        selected = selectedTab == filter,
                        onClick = { selectedTab = filter },
                        text = { Text(filter.displayName, fontSize = 14.sp) } // ❌ Fixed size
                    )
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp)) // ❌ Fixed spacing
            
            // Ticket List
            tickets.forEach { ticket ->
                TicketCard(ticket) // ❌ No size constraints
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}
```

### After (Fully Responsive):
```kotlin
@Composable
fun TicketListScreen(
    initialTab: TicketFilter = TicketFilter.Paid,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit = {},
    onTicketClick: (Ticket) -> Unit = {}
) {
    var selectedTab by rememberSaveable { mutableStateOf(initialTab) }
    val tickets = remember(selectedTab) {
        TicketController.getTicketsByStatus(selectedTab)
    }
    
    val horizontalPadding = responsiveHorizontalPadding()
    val spacingSmall = responsiveSpacingSmall()
    val spacingMedium = responsiveSpacingMedium()
    val maxContentWidth = getMaxContentWidth()
    
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = "Phiếu khám của tôi",
                        style = MaterialTheme.typography.titleLarge,
                        maxLines = 1
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            val cardWidth = when (getWindowSize()) {
                WindowSize.COMPACT -> maxWidth - (horizontalPadding * 2)
                WindowSize.MEDIUM -> 600.dp
                WindowSize.EXPANDED -> 720.dp
            }
            
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.TopCenter)
            ) {
                // Filter Tabs
                ScrollableTabRow(
                    selectedTabIndex = selectedTab.ordinal,
                    modifier = Modifier.fillMaxWidth(),
                    edgePadding = horizontalPadding,
                    containerColor = Color.White,
                    contentColor = BrandPalette.OceanBlue
                ) {
                    TicketFilter.values().forEach { filter ->
                        Tab(
                            selected = selectedTab == filter,
                            onClick = { selectedTab = filter },
                            text = { 
                                Text(
                                    text = filter.displayName,
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = if (selectedTab == filter) 
                                        FontWeight.Bold 
                                    else 
                                        FontWeight.Normal,
                                    maxLines = 1
                                )
                            }
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(spacingMedium))
                
                // Ticket List
                if (tickets.isEmpty()) {
                    EmptyState(
                        message = when (selectedTab) {
                            TicketFilter.Paid -> "Chưa có phiếu khám đã thanh toán"
                            TicketFilter.Unpaid -> "Chưa có phiếu khám chưa thanh toán"
                            TicketFilter.Completed -> "Chưa có phiếu khám đã hoàn thành"
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                    )
                } else {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .widthIn(max = maxContentWidth),
                        contentPadding = PaddingValues(
                            horizontal = horizontalPadding,
                            vertical = spacingMedium
                        ),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(spacingMedium)
                    ) {
                        items(
                            items = tickets,
                            key = { it.id }
                        ) { ticket ->
                            TicketCard(
                                ticket = ticket,
                                onClick = { onTicketClick(ticket) },
                                modifier = Modifier
                                    .widthIn(max = cardWidth)
                                    .fillMaxWidth()
                            )
                        }
                        
                        // Bottom spacing
                        item {
                            Spacer(modifier = Modifier.height(spacingMedium))
                        }
                    }
                }
            }
        }
    }
}

// Responsive Ticket Card
@Composable
private fun TicketCard(
    ticket: Ticket,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val iconSize = responsiveIconSize(baseSize = 48.dp)
    
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(responsiveSpacingMedium())
        ) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_hospital),
                        contentDescription = null,
                        modifier = Modifier.size(iconSize),
                        tint = BrandPalette.OceanBlue
                    )
                    
                    Spacer(modifier = Modifier.width(responsiveSpacingSmall()))
                    
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = ticket.hospitalName,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = BrandPalette.DeepBlue,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        
                        Text(
                            text = ticket.specialty,
                            style = MaterialTheme.typography.bodyMedium,
                            color = BrandPalette.SlateGrey,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                
                StatusBadge(status = ticket.status)
            }
            
            Spacer(modifier = Modifier.height(responsiveSpacingMedium()))
            
            // Divider
            HorizontalDivider(color = BrandPalette.BorderColor)
            
            Spacer(modifier = Modifier.height(responsiveSpacingMedium()))
            
            // Details Grid (Responsive)
            if (isLandscape() || getWindowSize() != WindowSize.COMPACT) {
                // Two columns on landscape/tablets
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        InfoItem("Ngày khám", ticket.date)
                        Spacer(modifier = Modifier.height(responsiveSpacingSmall()))
                        InfoItem("Bác sĩ", ticket.doctorName)
                    }
                    
                    Spacer(modifier = Modifier.width(responsiveSpacingMedium()))
                    
                    Column(modifier = Modifier.weight(1f)) {
                        InfoItem("Giờ khám", ticket.time)
                        Spacer(modifier = Modifier.height(responsiveSpacingSmall()))
                        InfoItem("Phí khám", ticket.fee)
                    }
                }
            } else {
                // Single column on portrait phones
                Column(modifier = Modifier.fillMaxWidth()) {
                    InfoItem("Ngày khám", ticket.date)
                    Spacer(modifier = Modifier.height(responsiveSpacingSmall()))
                    InfoItem("Giờ khám", ticket.time)
                    Spacer(modifier = Modifier.height(responsiveSpacingSmall()))
                    InfoItem("Bác sĩ", ticket.doctorName)
                    Spacer(modifier = Modifier.height(responsiveSpacingSmall()))
                    InfoItem("Phí khám", ticket.fee)
                }
            }
        }
    }
}

@Composable
private fun InfoItem(label: String, value: String) {
    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = BrandPalette.SlateGrey,
            maxLines = 1
        )
        Text(
            text = value,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            color = BrandPalette.DeepBlue,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun EmptyState(message: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(responsiveSpacingMedium())
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_empty_tickets),
                contentDescription = null,
                modifier = Modifier.size(responsiveIconSize(baseSize = 120.dp)),
                tint = BrandPalette.SlateGrey.copy(alpha = 0.3f)
            )
            
            Text(
                text = message,
                style = MaterialTheme.typography.bodyLarge,
                color = BrandPalette.SlateGrey,
                textAlign = TextAlign.Center,
                maxLines = 2
            )
        }
    }
}
```

---

## Key Takeaways

### ✅ Form Screens (Booking, Profile, Registration):
1. Use `LazyColumn` for scrollable content
2. Constrain width with `widthIn(max = getMaxContentWidth())`
3. Make all inputs `fillMaxWidth()`
4. Buttons have `heightIn(min = 48.dp)`
5. Use responsive spacing between sections

### ✅ List Screens (Tickets, Notifications):
1. Use `BoxWithConstraints` for size-aware layouts
2. Cards adapt width based on screen size
3. Empty states centered and responsive
4. Tab bars use `ScrollableTabRow` on phones
5. Details adapt: 1 column (portrait) → 2 columns (landscape/tablet)

### ✅ Universal Patterns:
- Always import responsive utilities
- Use `MaterialTheme.typography` instead of fixed `fontSize`
- Add `maxLines` and `overflow = TextOverflow.Ellipsis` to all text
- Icons scale with `responsiveIconSize()`
- Spacing uses responsive functions
- Content centered on tablets with `Alignment.Center`

---

**Apply these patterns to remaining screens for complete responsive coverage!** 🎯
