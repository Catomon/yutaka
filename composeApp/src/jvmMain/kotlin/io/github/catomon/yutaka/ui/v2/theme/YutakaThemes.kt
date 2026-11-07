package io.github.catomon.yutaka.ui.v2.theme

import androidx.compose.ui.graphics.Color

object YutakaThemes {

    private val starPink = Color(0xFFF6A6C1)
    private val starGreen = Color(0xFF7BC96F)
    private val starAmber = Color(0xFFF2C14E)

    val Yutaka = YutakaColors(
        name = "yutaka",
        displayName = "Yutaka",

        backgroundStart = Color(0xFFFFF6EA),
        backgroundMid = Color(0xFFFFF1E2),
        backgroundEnd = Color(0xFFFFE3CC),

        headerStart = Color(0xFFC76B8E),
        headerEnd = Color(0xFF9E4A6B),
        headerContent = Color(0xFFFFF9F2),
        headerOutline = Color(0xFF5A2438),

        panelStart = Color(0xFFB25F80),
        panelEnd = Color(0xFF8E4262),
        panelBorderStart = Color(0xFFF6A6C1),
        panelBorderEnd = Color(0xFF7BC96F),
        panelContent = Color(0xFFFFF9F2),
        panelContentSecondary = Color(0xFFFFE3EC),

        itemStart = Color(0xFFD98BA8),
        itemEnd = Color(0xFFB25F80),
        itemContent = Color(0xFFFFF9F2),
        itemContentSecondary = Color(0xFFFFE3EC),
        itemSelectedStart = Color(0xFFFFE3EC),
        itemSelectedEnd = Color(0xFFFFB7D5),
        itemSelectedContent = Color(0xFF5A2438),
        itemSelectedContentSecondary = Color(0xFF8E4262),
        itemSelectedOutline = Color(0xFFFFF9F2),
        itemSelectedIcon = Color(0xFF5A2438),

        accent = Color(0xFFF6A6C1),
        accentSoft = Color(0xFFFFB7D5),
        onAccent = Color(0xFF5A2438),
        text = Color(0xFF5A2438),
        textSecondary = Color(0xFF8E4262),
        outline = Color(0xFFFFF9F2),
        disabled = Color(0xFFC9A3B3),

        controlStart = Color(0xFFB25F80),
        controlEnd = Color(0xFF8E4262),
        controlContent = Color(0xFFFFF9F2),
        playStart = Color(0xFFFFE3EC),
        playEnd = Color(0xFFFFB7D5),
        playContent = Color(0xFF5A2438),

        windowBorderStart = Color(0xFFF6A6C1),
        windowBorderEnd = Color(0xFF7BC96F),
        glow = Color(0xFFFFFFFF),

        starColors = listOf(starPink, starGreen, starAmber),
    )

    val default = Yutaka
}
