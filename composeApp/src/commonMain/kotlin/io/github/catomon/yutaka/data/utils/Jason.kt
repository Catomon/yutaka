package io.github.catomon.yutaka.data.utils

import kotlinx.serialization.json.Json

internal val json =
    Json { ignoreUnknownKeys = true; isLenient = true }