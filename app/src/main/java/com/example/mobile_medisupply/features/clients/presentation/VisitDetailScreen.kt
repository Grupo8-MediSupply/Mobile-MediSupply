package com.example.mobile_medisupply.features.clients.presentation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.MediaMetadataRetriever
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowBack
import androidx.compose.material.icons.outlined.AttachFile
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.PlayCircle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.mobile_medisupply.R
import com.example.mobile_medisupply.features.clients.domain.model.ClientVisitDetail
import com.example.mobile_medisupply.features.clients.domain.model.ClientVisitStatus
import com.example.mobile_medisupply.features.clients.domain.model.VisitVideoAttachment
import com.example.mobile_medisupply.ui.theme.MobileMediSupplyTheme
import com.example.mobile_medisupply.features.home.domain.model.VisitDetail as HomeVisitDetail
import com.example.mobile_medisupply.features.home.data.remote.Ubicacion
import kotlinx.coroutines.launch
import java.util.Locale

private const val MAX_VIDEO_SIZE_BYTES = 30L * 1024 * 1024 // 30MB
private const val MAX_VIDEO_DURATION_MS = 60_000L // 60 seconds

@Composable
fun VisitDetailScreen(
        visitId: String,
        modifier: Modifier = Modifier,
        onBackClick: () -> Unit,
        onSaveClick: (String, VisitVideoAttachment?) -> Unit,
        viewModel: VisitDetailViewModel = hiltViewModel(),

) {
    val visitDetailDomain by viewModel.visitDetail.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(visitId) {
        viewModel.loadVisitDetail(visitId)
    }
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    var showVideoOptions by remember { mutableStateOf(false) }
    var comment by remember { mutableStateOf("") }
    var selectedVideo by remember { mutableStateOf<VisitVideoAttachment?>(null) }
    val scope = rememberCoroutineScope()
    val visitDetail = visitDetailDomain?.toClientVisitDetail()
    LaunchedEffect(visitDetail?.notes) {
        if (visitDetail != null) {
            comment = visitDetail.notes.orEmpty()
        }
    }
    val statusLabel = visitDetail?.status?.localizedLabel().orEmpty()
    val clientName =
            visitDetailDomain?.client?.name
                    ?: visitDetail?.title
                    ?: stringResource(R.string.visit_detail_unknown_client)

    val galleryLauncher =
            rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.PickVisualMedia()
            ) { uri ->
                if (uri != null) {
                    handleVideoResult(
                            context = context,
                            uri = uri,
                            onValid = { selectedVideo = it },
                            onError = { message ->
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                            message = message,
                                            duration = SnackbarDuration.Short
                                    )
                                }
                            }
                    )
                }
            }

    val captureLauncher =
            rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.StartActivityForResult()
            ) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val uri = extractVideoUri(result)
                    if (uri != null) {
                        handleVideoResult(
                                context = context,
                                uri = uri,
                                onValid = { selectedVideo = it },
                                onError = { message ->
                                    scope.launch {
                                        snackbarHostState.showSnackbar(
                                                message = message,
                                                duration = SnackbarDuration.Short
                                        )
                                    }
                                }
                        )
                    } else {
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                    message = context.getString(
                                            R.string.visit_detail_video_capture_error
                                    ),
                                    duration = SnackbarDuration.Short
                            )
                        }
                    }
                }
            }

    Scaffold(
            modifier = modifier.fillMaxSize(),
            snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->
        when {
            isLoading && visitDetail == null ->
                    VisitDetailLoadingState(paddingValues = paddingValues)
            visitDetail == null ->
                    VisitDetailEmptyState(paddingValues = paddingValues)
            else ->
                    VisitDetailLoadedContent(
                            paddingValues = paddingValues,
                            clientName = clientName,
                            visitDetail = visitDetail,
                            statusLabel = statusLabel,
                            comment = comment,
                            onCommentChange = { comment = it },
                            showVideoOptions = showVideoOptions,
                            onShowVideoOptionsChange = { showVideoOptions = it },
                            onBackClick = onBackClick,
                            onSaveClick = { text, video -> onSaveClick(text, video) },
                            selectedVideo = selectedVideo,
                            onSelectedVideoChange = { selectedVideo = it },
                            onRecordVideo = {
                                val intent =
                                        Intent(MediaStore.ACTION_VIDEO_CAPTURE).apply {
                                            putExtra(MediaStore.EXTRA_DURATION_LIMIT, 60)
                                            putExtra(MediaStore.EXTRA_SIZE_LIMIT, MAX_VIDEO_SIZE_BYTES)
                                        }
                                captureLauncher.launch(intent)
                            },
                            onPickFromGallery = {
                                galleryLauncher.launch(
                                        PickVisualMediaRequest(
                                                ActivityResultContracts.PickVisualMedia.VideoOnly
                                        )
                                )
                            }
                    )
        }
    }
}

@Composable
private fun VisitDetailLoadedContent(
        paddingValues: PaddingValues,
        clientName: String,
        visitDetail: ClientVisitDetail,
        statusLabel: String,
        comment: String,
        onCommentChange: (String) -> Unit,
        showVideoOptions: Boolean,
        onShowVideoOptionsChange: (Boolean) -> Unit,
        onBackClick: () -> Unit,
        onSaveClick: (String, VisitVideoAttachment?) -> Unit,
        selectedVideo: VisitVideoAttachment?,
        onSelectedVideoChange: (VisitVideoAttachment?) -> Unit,
        onRecordVideo: () -> Unit,
        onPickFromGallery: () -> Unit,
) {
    if (showVideoOptions) {
        AttachVideoOptionsDialog(
                onDismiss = { onShowVideoOptionsChange(false) },
                onRecord = {
                    onShowVideoOptionsChange(false)
                    onRecordVideo()
                },
                onPickFromGallery = {
                    onShowVideoOptionsChange(false)
                    onPickFromGallery()
                }
        )
    }

    Column(
            modifier =
                    Modifier.fillMaxSize()
                            .padding(paddingValues)
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.2f))
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = 16.dp, vertical = 24.dp)
    ) {
        Icon(
                imageVector = Icons.Outlined.ArrowBack,
                contentDescription = stringResource(R.string.back),
                tint = MaterialTheme.colorScheme.primary,
                modifier =
                        Modifier.padding(bottom = 16.dp)
                                .clip(MaterialTheme.shapes.small)
                                .clickable(onClick = onBackClick)
                                .padding(4.dp)
        )

        Text(
                text = clientName,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary
        )
        Text(
                text = visitDetail.title,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
        )
        Text(
                text =
                        stringResource(
                                R.string.visit_detail_status_date,
                                statusLabel,
                                visitDetail.scheduledDate
                        ),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        VisitInfoCard(visitDetail = visitDetail, statusLabel = statusLabel)

        Spacer(modifier = Modifier.height(24.dp))

        Text(
                text = stringResource(R.string.visit_detail_comments),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface
        )

        OutlinedTextField(
                value = comment,
                onValueChange = onCommentChange,
                modifier =
                        Modifier.fillMaxWidth().height(160.dp).padding(top = 12.dp),
                placeholder = {
                    Text(stringResource(R.string.visit_detail_comments_placeholder))
                }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Card(
                modifier = Modifier.fillMaxWidth(),
                colors =
                        CardDefaults.cardColors(
                                containerColor =
                                        MaterialTheme.colorScheme.surface.copy(alpha = 0.92f)
                        )
        ) {
            Column(modifier = Modifier.fillMaxWidth().padding(16.dp)) {
                Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                            text = stringResource(R.string.visit_detail_video_section),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurface
                    )

                    OutlinedButton(onClick = { onShowVideoOptionsChange(true) }) {
                        Icon(
                                imageVector = Icons.Outlined.AttachFile,
                                contentDescription =
                                        stringResource(R.string.visit_detail_attach_video_cd)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = stringResource(R.string.visit_detail_attach_video))
                    }
                }

                if (selectedVideo != null) {
                    Spacer(modifier = Modifier.height(16.dp))
                    SelectedVideoSummary(
                            attachment = selectedVideo,
                            onRemove = { onSelectedVideoChange(null) }
                    )
                } else {
                    Text(
                            text = stringResource(R.string.visit_detail_no_video_selected),
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 12.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
                onClick = { onSaveClick(comment.trim(), selectedVideo) },
                modifier = Modifier.align(Alignment.CenterHorizontally),
                enabled = comment.isNotBlank() || selectedVideo != null
        ) {
            Text(text = stringResource(R.string.visit_detail_save))
        }
    }
}

@Composable
private fun VisitDetailLoadingState(paddingValues: PaddingValues) {
    Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun VisitDetailEmptyState(paddingValues: PaddingValues) {
    Box(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentAlignment = Alignment.Center
    ) {
        Text(
                text = stringResource(R.string.visit_detail_not_found),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun VisitInfoCard(visitDetail: ClientVisitDetail, statusLabel: String) {
    Card(
            modifier = Modifier.fillMaxWidth(),
            colors =
                    CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.onSurface
                    ),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth().padding(20.dp)) {
            Text(
                    text = stringResource(R.string.visit_detail_info_title),
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.SemiBold
            )

            Spacer(modifier = Modifier.height(12.dp))

            InfoRow(label = stringResource(R.string.visit_detail_info_location), value = visitDetail.location)
            InfoRow(
                    label = stringResource(R.string.visit_detail_info_status),
                    value = statusLabel
            )
            InfoRow(
                    label = stringResource(R.string.visit_detail_info_date),
                    value = visitDetail.scheduledDate
            )
        }
    }
}

@Composable
private fun InfoRow(label: String, value: String) {
    Row(
            modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
            horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
                text = label,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun SelectedVideoSummary(
        attachment: VisitVideoAttachment,
        onRemove: () -> Unit
) {
    Card(
            modifier = Modifier.fillMaxWidth(),
            colors =
                    CardDefaults.cardColors(
                            containerColor =
                                    MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)
                    ),
            shape = RoundedCornerShape(12.dp)
    ) {
        Row(
                modifier = Modifier.fillMaxWidth().padding(12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Icon(
                    imageVector = Icons.Outlined.PlayCircle,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary
            )
            Column(modifier = Modifier.weight(1f)) {
                Text(
                        text = attachment.displayName,
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                        text =
                                stringResource(
                                        R.string.visit_detail_video_meta,
                                        attachment.sizeLabel,
                                        attachment.durationLabel
                                ),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Icon(
                    imageVector = Icons.Outlined.Delete,
                    contentDescription =
                            stringResource(R.string.visit_detail_remove_video_cd),
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.clickable(onClick = onRemove)
            )
        }
    }
}

@Composable
private fun AttachVideoOptionsDialog(
        onDismiss: () -> Unit,
        onRecord: () -> Unit,
        onPickFromGallery: () -> Unit
) {
    AlertDialog(
            onDismissRequest = onDismiss,
            title = { Text(text = stringResource(R.string.visit_detail_attach_video)) },
            text = {
                Column {
                    Text(
                            text = stringResource(R.string.visit_detail_attach_options_help),
                            style = MaterialTheme.typography.bodyMedium
                    )
                }
            },
            confirmButton = {
                Column(modifier = Modifier.fillMaxWidth()) {
                    TextButton(onClick = onRecord, modifier = Modifier.fillMaxWidth()) {
                        Text(text = stringResource(R.string.visit_detail_record_video))
                    }
                    TextButton(onClick = onPickFromGallery, modifier = Modifier.fillMaxWidth()) {
                        Text(text = stringResource(R.string.visit_detail_pick_gallery))
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) { Text(text = stringResource(R.string.cancel)) }
            }
    )
}

private fun handleVideoResult(
        context: Context,
        uri: Uri,
        onValid: (VisitVideoAttachment) -> Unit,
        onError: (String) -> Unit
) {
    val size = resolveFileSize(context, uri)
    if (size == null || size <= 0L) {
        onError(context.getString(R.string.visit_detail_video_unknown_size))
        return
    }
    if (size > MAX_VIDEO_SIZE_BYTES) {
        onError(context.getString(R.string.visit_detail_video_size_error))
        return
    }

    val duration = resolveDuration(context, uri)
    if (duration == null) {
        onError(context.getString(R.string.visit_detail_video_duration_error))
        return
    }
    if (duration > MAX_VIDEO_DURATION_MS) {
        onError(context.getString(R.string.visit_detail_video_duration_limit))
        return
    }

    val displayName =
            resolveDisplayName(context, uri)
                    ?: context.getString(R.string.visit_detail_video_default_name)

    onValid(
            VisitVideoAttachment(
                    uri = uri,
                    displayName = displayName,
                    sizeBytes = size,
                    durationMs = duration
            )
    )
}

private fun resolveDisplayName(context: Context, uri: Uri): String? =
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
            if (nameIndex != -1 && cursor.moveToFirst()) cursor.getString(nameIndex) else null
        }

private fun resolveFileSize(context: Context, uri: Uri): Long? =
        context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val sizeIndex = cursor.getColumnIndex(android.provider.OpenableColumns.SIZE)
            if (sizeIndex != -1 && cursor.moveToFirst()) cursor.getLong(sizeIndex) else null
        }
                ?: context.contentResolver.openAssetFileDescriptor(uri, "r")?.use { it.length }

private fun resolveDuration(context: Context, uri: Uri): Long? {
    val retriever = MediaMetadataRetriever()
    return try {
        retriever.setDataSource(context, uri)
        retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong()
    } catch (e: IllegalArgumentException) {
        null
    } finally {
        retriever.release()
    }
}

private fun extractVideoUri(result: ActivityResult): Uri? {
    val data = result.data ?: return null
    return data.data
}

private fun HomeVisitDetail.toClientVisitDetail(): ClientVisitDetail =
        ClientVisitDetail(
                id = id,
                title = client.name,
                scheduledDate = date,
                status = status.toClientVisitStatus(),
                location = client.Location.toDisplayLocation(),
                notes = comments
        )

private fun String?.toClientVisitStatus(): ClientVisitStatus =
        when (this?.lowercase(Locale.getDefault())) {
            "en_curso", "en curso", "en_proceso", "en proceso", "in_progress", "progreso" ->
                    ClientVisitStatus.EN_CURSO
            "finalizada", "finalizado", "completada", "completado", "completed" ->
                    ClientVisitStatus.FINALIZADA
            else -> ClientVisitStatus.PROGRAMADA
        }

private fun Ubicacion?.toDisplayLocation(): String {
    if (this == null) return "-"
    val latValue = this.lat.toCoordinateString()
    val lngValue = this.lng.toCoordinateString()
    return "$latValue, $lngValue"
}

private fun Double.toCoordinateString(): String =
        String.format(Locale.getDefault(), "%.4f", this)

@Composable
private fun ClientVisitStatus.localizedLabel(): String =
        when (this) {
            ClientVisitStatus.PROGRAMADA ->
                    stringResource(R.string.client_visit_status_programmed)
            ClientVisitStatus.EN_CURSO ->
                    stringResource(R.string.client_visit_status_in_progress)
            ClientVisitStatus.FINALIZADA ->
                    stringResource(R.string.client_visit_status_completed)
        }

@Preview(showBackground = true, widthDp = 360)
@Composable
private fun VisitDetailPreview() {
    MobileMediSupplyTheme {
        val detail =
                ClientVisitDetail(
                        id = "visit-1001",
                        title = "Visita 1",
                        scheduledDate = "05/20/2024",
                        status = ClientVisitStatus.PROGRAMADA,
                        location = "Bogotá",
                        notes = "Recordar entrega de manual técnico."
                )
        VisitDetailLoadedContent(
                paddingValues = PaddingValues(),
                clientName = "Clínica San Rafael",
                visitDetail = detail,
                statusLabel = detail.status.localizedLabel(),
                comment = detail.notes.orEmpty(),
                onCommentChange = {},
                showVideoOptions = false,
                onShowVideoOptionsChange = {},
                onBackClick = {},
                onSaveClick = { _, _ -> },
                selectedVideo = null,
                onSelectedVideoChange = {},
                onRecordVideo = {},
                onPickFromGallery = {}
        )
    }
}
