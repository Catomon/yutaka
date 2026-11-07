package io.github.catomon.yutaka.ui.v2.theme

import androidx.compose.ui.graphics.Color

/**
 * Color slots for the Yutaka v2 layout.
 *
 * Yutaka Kobayakawa: pink hair, green eyes, cream and white uniform.
 * Panels are opaque gradients, so most slots are used at full alpha.
 */
data class YutakaColors(
    val name: String,
    val displayName: String,

    // Page background
    val backgroundStart: Color,
    val backgroundMid: Color,
    val backgroundEnd: Color,

    // Title bar
    val headerStart: Color,
    val headerEnd: Color,
    val headerContent: Color,
    val headerOutline: Color,

    // Side panels
    val panelStart: Color,
    val panelEnd: Color,
    val panelBorderStart: Color,
    val panelBorderEnd: Color,
    val panelContent: Color,
    val panelContentSecondary: Color,

    // Gallery cards
    val itemStart: Color,
    val itemEnd: Color,
    val itemContent: Color,
    val itemContentSecondary: Color,
    val itemSelectedStart: Color,
    val itemSelectedEnd: Color,
    val itemSelectedContent: Color,
    val itemSelectedContentSecondary: Color,
    val itemSelectedOutline: Color,
    val itemSelectedIcon: Color,

    // Text over the page background
    val accent: Color,
    val accentSoft: Color,
    val onAccent: Color,
    val text: Color,
    val textSecondary: Color,
    val outline: Color,
    val disabled: Color,

    // Buttons and controls
    val controlStart: Color,
    val controlEnd: Color,
    val controlContent: Color,
    val playStart: Color,
    val playEnd: Color,
    val playContent: Color,

    // Window frame
    val windowBorderStart: Color,
    val windowBorderEnd: Color,
    val glow: Color,

    // Falling stars
    val starColors: List<Color>,
)
