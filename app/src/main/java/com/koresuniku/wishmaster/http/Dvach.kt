package com.koresuniku.wishmaster.http

import java.util.*
import kotlin.collections.HashSet

object Dvach {
    val DVACH_BASE_URL: String = "https://2ch.hk"

    val threadForPagesBoards: Set<String> = HashSet<String>(arrayListOf("d"))

    val disableSubject: Set<String> = HashSet<String>(arrayListOf("b"))
}