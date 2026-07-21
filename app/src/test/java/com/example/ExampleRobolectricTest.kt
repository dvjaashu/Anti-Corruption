package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("Anti-Corruption", appName)
  }

  @Test
  fun `verify sovereign cryptography encryption and decryption`() {
    val originalText = "This is a highly sensitive corruption allegation text."
    val encryptedText = com.example.data.SovereignCryptography.encrypt(originalText)
    
    // Encrypted text must not be empty and must not match original text
    org.junit.Assert.assertNotNull(encryptedText)
    org.junit.Assert.assertNotEquals(originalText, encryptedText)
    
    // Decrypted text must match the original text perfectly
    val decryptedText = com.example.data.SovereignCryptography.decrypt(encryptedText)
    assertEquals(originalText, decryptedText)
  }
}
