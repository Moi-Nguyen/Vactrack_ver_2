package com.example.vactrack_ver1.view.specialty

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.vactrack_ver1.model.Specialty
import com.example.vactrack_ver1.model.specialties
import com.example.vactrack_ver1.ui.theme.Vactrack_ver1Theme

/* ==== Theme tokens ==== */
private val PrimaryBlue = Color(0xFF5BB7CF)
private val BackgroundColor = Color(0xFFF5F9FA)
private val TextPrimary = Color(0xFF1F2937)
private val TextSecondary = Color(0xFF6B7280)
private val SearchBarBackground = Color.White
private val BorderColor = Color(0xFFE5E7EB)

/**
 * Screen for selecting a medical specialty
 * Displays a searchable grid of specialty icons
 * 
 * @param modifier Modifier for the screen
 * @param specialties List of available specialties (defaults to all specialties)
 * @param onBackClick Callback when back button is pressed
 * @param onSpecialtySelected Callback when a specialty is tapped
 */
@Composable
fun SpecialtySelectionScreen(
    modifier: Modifier = Modifier,
    specialties: List<Specialty> = com.example.vactrack_ver1.model.specialties,
    onBackClick: () -> Unit = {},
    onSpecialtySelected: (Specialty) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    
    // Filter specialties based on search query
    val filteredSpecialties = remember(searchQuery, specialties) {
        if (searchQuery.isBlank()) {
            specialties
        } else {
            specialties.filter { 
                it.name.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Surface(
        modifier = modifier.fillMaxSize(),
        color = BackgroundColor
    ) {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                SpecialtySelectionTopBar(
                    onBackClick = onBackClick
                )
            }
        ) { innerPadding ->
            // FIXED: Removed wrapping Column to avoid infinite height constraints
            // Now LazyVerticalGrid is the only scrollable component
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(
                    horizontal = 16.dp,
                    vertical = 16.dp
                ),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                // Search bar as a grid item spanning all columns
                item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(3) }) {
                    Box(
                        modifier = Modifier.padding(bottom = 12.dp)
                    ) {
                        SpecialtySearchBar(
                            searchQuery = searchQuery,
                            onSearchQueryChange = { searchQuery = it }
                        )
                    }
                }
                
                // Grid of specialties - 3 columns with uniform spacing
                items(filteredSpecialties) { specialty ->
                    SpecialtyItem(
                        specialty = specialty,
                        onClick = { onSpecialtySelected(specialty) }
                    )
                }
            }
        }
    }
}

/**
 * Top app bar for specialty selection screen
 */
@Composable
private fun SpecialtySelectionTopBar(
    onBackClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            // Added statusBarsPadding() and padding(top = 20.dp) to move the top bar down slightly
            .padding(top = 20.dp),
        color = PrimaryBlue,
        shadowElevation = 4.dp
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(horizontal = 4.dp)
        ) {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }

            Text(
                text = "Đặt khám chuyên khoa",
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

/**
 * Search bar for filtering specialties
 * Full-width with white background, rounded corners, and border
 */
@Composable
private fun SpecialtySearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = SearchBarBackground,
        shadowElevation = 2.dp
    ) {
        OutlinedTextField(
            value = searchQuery,
            onValueChange = onSearchQueryChange,
            modifier = Modifier.fillMaxWidth(),
            placeholder = {
                Text(
                    text = "Tìm kiếm chuyên khoa",
                    color = TextSecondary.copy(alpha = 0.6f),
                    fontSize = 15.sp
                )
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = TextSecondary
                )
            },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = BorderColor,
                focusedBorderColor = PrimaryBlue,
                unfocusedContainerColor = SearchBarBackground,
                focusedContainerColor = SearchBarBackground
            ),
            singleLine = true
        )
    }
}

/**
 * Individual specialty item in the grid
 * Larger, visually balanced icons (72dp) with centered labels below
 */
@Composable
private fun SpecialtyItem(
    specialty: Specialty,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Specialty icon - large 72dp icon without background
        Image(
            painter = painterResource(id = specialty.iconRes),
            contentDescription = specialty.name,
            modifier = Modifier.size(72.dp),
            contentScale = ContentScale.Fit
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        // Specialty label
        Text(
            text = specialty.name,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodySmall.copy(
                color = TextPrimary,
                lineHeight = 16.sp
            ),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Preview(showBackground = true, widthDp = 360, heightDp = 800)
@Composable
private fun SpecialtySelectionScreenPreview() {
    Vactrack_ver1Theme {
        SpecialtySelectionScreen()
    }
}
