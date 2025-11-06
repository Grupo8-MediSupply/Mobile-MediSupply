package com.example.mobile_medisupply.features.home.presentation

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.ChevronRight
import androidx.compose.material.icons.outlined.EventBusy
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.mobile_medisupply.R
import com.example.mobile_medisupply.features.home.data.ScheduledVisitsRepositoryProvider
import com.example.mobile_medisupply.features.home.domain.model.ScheduledVisit
import com.example.mobile_medisupply.ui.theme.MobileMediSupplyTheme
@Composable
fun HomeScreen(
        viewModel: HomeViewModel,
        onScheduleVisitClick: () -> Unit = {},
        onVisitClick: (ScheduledVisit) -> Unit = {},
        modifier: Modifier = Modifier
) {
    val visits by viewModel.visits.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadVisits()
    }
    Surface(
            modifier = modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surface
    ) {
        Column(
                modifier =
                        Modifier.fillMaxSize()
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f))
                                .padding(horizontal = 16.dp, vertical = 24.dp)
        ) {
            Header(onScheduleVisitClick = onScheduleVisitClick)

            Spacer(modifier = Modifier.height(16.dp))

            ScheduledVisitsList(
                    visits = visits,
                    onVisitClick = onVisitClick,
                    modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun Header(onScheduleVisitClick: () -> Unit) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                    text = stringResource(R.string.scheduled_visits_title),
                    style = MaterialTheme.typography.headlineMedium,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f, fill = true)
            )

            Spacer(modifier = Modifier.size(16.dp))

            ElevatedButton(onClick = onScheduleVisitClick) {
                Icon(imageVector = Icons.Outlined.CalendarMonth, contentDescription = null)
                Spacer(modifier = Modifier.size(8.dp))
                Text(text = stringResource(R.string.schedule_visit_button))
            }
        }

        Text(
                text = stringResource(R.string.scheduled_visits_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
        )
    }
}

@Composable
private fun ScheduledVisitsList(
        visits: List<ScheduledVisit>,
        onVisitClick: (ScheduledVisit) -> Unit,
        modifier: Modifier = Modifier
) {
    Card(
            modifier = modifier,
            colors =
                    CardDefaults.cardColors(
                            containerColor =
                                    MaterialTheme.colorScheme.surface.copy(alpha = 0.96f)
                    ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        if (visits.isEmpty()) {
            EmptyState()
        } else {
            LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                itemsIndexed(visits, key = { _, visit -> visit.id }) { index, visit ->
                    ScheduledVisitRow(
                            visit = visit,
                            onClick = { onVisitClick(visit) }
                    )
                    if (index < visits.lastIndex) {
                        HorizontalDivider(
                                color = MaterialTheme.colorScheme.surfaceVariant,
                                thickness = 1.dp,
                                modifier = Modifier.padding(horizontal = 16.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ScheduledVisitRow(
        visit: ScheduledVisit,
        onClick: () -> Unit
) {
    Row(
            modifier =
                    Modifier.fillMaxWidth()
                            .clip(MaterialTheme.shapes.medium)
                            .clickable(onClick = onClick)
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
                modifier =
                        Modifier.size(48.dp)
                                .clip(MaterialTheme.shapes.small)
                                .background(
                                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.15f)
                                ),
                contentAlignment = Alignment.Center
        ) {
            Icon(
                    imageVector = Icons.Outlined.CalendarMonth,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
            )
        }

        Column(modifier = Modifier.padding(start = 16.dp).weight(1f)) {
            Text(
                    text = visit.clientName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                    text = stringResource(R.string.scheduled_visit_date, visit.scheduledDate),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            visit.location?.takeIf { it.isNotBlank() }?.let { location ->
                Text(
                        text = location,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                )
            }
        }

        Icon(
                imageVector = Icons.Outlined.ChevronRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun EmptyState() {
    Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
                imageVector = Icons.Outlined.EventBusy,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(48.dp)
        )
        Text(
                text = stringResource(R.string.scheduled_visits_empty_title),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurface
        )
        Text(
                text = stringResource(R.string.scheduled_visits_empty_subtitle),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

