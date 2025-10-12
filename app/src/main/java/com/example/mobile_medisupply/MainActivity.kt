package com.example.mobile_medisupply

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mobile_medisupply.ui.theme.MobileMediSupplyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MobileMediSupplyTheme(
                    dynamicColor = false // Disable dynamic color to see custom colors
            ) {
                Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                ) { ThemeTestScreen() }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeTestScreen() {
    Column(
            modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
                text = "Mobile MediSupply Theme",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
        )

        Text(
                text = "Testing custom theme colors and typography",
                style = MaterialTheme.typography.bodyLarge
        )

        // Primary colors showcase
        Card(
                modifier = Modifier.fillMaxWidth(),
                colors =
                        CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                        )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                        text = "Primary Container",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Text(
                        text = "This card uses primary container colors",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }

        // Secondary colors showcase
        Card(
                modifier = Modifier.fillMaxWidth(),
                colors =
                        CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.secondaryContainer
                        )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                        text = "Secondary Container",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                )
                Text(
                        text = "This card uses secondary container colors",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }
        }

        // Buttons showcase
        Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(onClick = {}, modifier = Modifier.weight(1f)) { Text("Primary Button") }

            OutlinedButton(onClick = {}, modifier = Modifier.weight(1f)) { Text("Outlined Button") }
        }

        // Surface variants
        Surface(
                modifier = Modifier.fillMaxWidth(),
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = MaterialTheme.shapes.medium
        ) {
            Text(
                    text = "Surface Variant",
                    modifier = Modifier.padding(16.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyLarge
            )
        }

        // Typography showcase
        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(text = "Typography Showcase", style = MaterialTheme.typography.titleLarge)
            Text(text = "Body Large Text", style = MaterialTheme.typography.bodyLarge)
            Text(text = "Body Medium Text", style = MaterialTheme.typography.bodyMedium)
            Text(text = "Label Small Text", style = MaterialTheme.typography.labelSmall)
        }

        // Color palette showcase
        LazyColorGrid()
    }
}

@Composable
fun LazyColorGrid() {
    Column {
        Text(
                text = "Color Palette",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(bottom = 8.dp)
        )

        val colorScheme = MaterialTheme.colorScheme
        val colors =
                listOf(
                        "Primary" to colorScheme.primary,
                        "Secondary" to colorScheme.secondary,
                        "Tertiary" to colorScheme.tertiary,
                        "Surface" to colorScheme.surface,
                        "Background" to colorScheme.background,
                        "Error" to colorScheme.error
                )

        colors.chunked(2).forEach { rowColors ->
            Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                rowColors.forEach { (name, color) ->
                    Surface(
                            modifier = Modifier.weight(1f).height(60.dp),
                            color = color,
                            shape = MaterialTheme.shapes.small
                    ) {
                        Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                        ) {
                            Text(
                                    text = name,
                                    style = MaterialTheme.typography.labelSmall,
                                    color =
                                            if (name == "Background" || name == "Surface") {
                                                MaterialTheme.colorScheme.onSurface
                                            } else {
                                                MaterialTheme.colorScheme.onPrimary
                                            }
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ThemeTestPreview() {
    MobileMediSupplyTheme { ThemeTestScreen() }
}

@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
fun ThemeTestDarkPreview() {
    MobileMediSupplyTheme { ThemeTestScreen() }
}
