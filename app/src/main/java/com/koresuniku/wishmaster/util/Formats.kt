package com.koresuniku.wishmaster.util

import java.util.*
import kotlin.collections.HashMap
import kotlin.collections.HashSet

object Formats {

    val VIDEO_FORMATS: Set<String> = HashSet(Arrays.asList("webm", "mp4"))

    val IMAGE_FORMATS: Set<String> = HashSet(Arrays.asList("jpeg", "jpg", "png"))

    val GIF_FORMAT: Set<String> = HashSet(Collections.singletonList("gif"))
}