package ru.kettuproj.util

import com.google.common.hash.Hashing
import java.nio.charset.StandardCharsets
import kotlin.math.ceil


object PasswordUtil {
    fun generateHash(pass: String, salt: String): String{
        return Hashing.sha256()
            .hashString(StringBuilder()
                .append(pass.substring(0, ceil(pass.length / 2.0).toInt()))
                .append(salt)
                .append(pass.substring(ceil(pass.length / 2.0).toInt(),pass.length)), StandardCharsets.UTF_8)
            .toString()
    }
}