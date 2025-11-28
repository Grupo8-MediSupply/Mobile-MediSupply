package com.example.mobile_medisupply.features.clients.domain.model

import android.net.Uri
import java.io.File

data class VisitVideoAttachment(
        val uri: Uri,
        val displayName: String,
        val sizeBytes: Long,
        val durationMs: Long,
) {
    val sizeLabel: String
        get() = "%.1f MB".format(sizeBytes / (1024.0 * 1024.0))

    val durationLabel: String
        get() = buildString {
            val totalSeconds = durationMs / 1000
            val minutes = totalSeconds / 60
            val seconds = totalSeconds % 60
            append(String.format("%02d:%02d", minutes, seconds))
        }
}
