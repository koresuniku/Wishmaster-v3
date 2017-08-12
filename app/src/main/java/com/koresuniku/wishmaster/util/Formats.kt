package com.koresuniku.wishmaster.util

import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

object Formats {

    val WEBM_FORMAT: String = "webm"
    val MP4_FORMAT: String = "mp4"

    val JPEG_FORMAT: String = "jpeg"
    val JPG_FORMAT: String = "jpg"
    val PNG_FORMAT: String = "png"

    val A_GIF_FORMAT: String = "gif"

    val VIDEO_FORMATS: Set<String> = HashSet(Arrays.asList(WEBM_FORMAT, MP4_FORMAT))

    val IMAGE_FORMATS: Set<String> = HashSet(Arrays.asList(JPEG_FORMAT, JPG_FORMAT, PNG_FORMAT))

    val GIF_FORMAT: Set<String> = HashSet(Collections.singletonList(A_GIF_FORMAT))
}