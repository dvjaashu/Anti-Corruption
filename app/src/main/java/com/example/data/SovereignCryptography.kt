package com.example.data

import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

object SovereignCryptography {
    // 32-character key for AES-256 encryption.
    private const val DEFAULT_KEY = "SovereignShieldKey2026SecureGate"
    private const val ALGORITHM = "AES/CBC/PKCS5Padding"

    private fun getSecretKeySpec(key: String): SecretKeySpec {
        val bytes = key.padEnd(32, ' ').take(32).toByteArray(Charsets.UTF_8)
        return SecretKeySpec(bytes, "AES")
    }

    fun encrypt(plainText: String, key: String = DEFAULT_KEY): String {
        if (plainText.isBlank()) return plainText
        return try {
            val cipher = Cipher.getInstance(ALGORITHM)
            val keySpec = getSecretKeySpec(key)
            // Use a constant/safe IV for consistent client-side hashing, or a 16-byte zero IV
            val iv = ByteArray(16) { 0 }
            val ivSpec = IvParameterSpec(iv)
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)
            val encryptedBytes = cipher.doFinal(plainText.toByteArray(Charsets.UTF_8))
            Base64.encodeToString(encryptedBytes, Base64.NO_WRAP)
        } catch (e: Exception) {
            e.printStackTrace()
            plainText
        }
    }

    fun decrypt(encryptedText: String, key: String = DEFAULT_KEY): String {
        if (encryptedText.isBlank()) return encryptedText
        return try {
            val cipher = Cipher.getInstance(ALGORITHM)
            val keySpec = getSecretKeySpec(key)
            val iv = ByteArray(16) { 0 }
            val ivSpec = IvParameterSpec(iv)
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)
            val decodedBytes = Base64.decode(encryptedText, Base64.NO_WRAP)
            val decryptedBytes = cipher.doFinal(decodedBytes)
            String(decryptedBytes, Charsets.UTF_8)
        } catch (e: Exception) {
            // Decryption failed: probably not encrypted, return as is
            encryptedText
        }
    }
}
