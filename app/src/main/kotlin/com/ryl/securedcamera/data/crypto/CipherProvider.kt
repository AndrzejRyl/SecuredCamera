package com.ryl.securedcamera.data.crypto

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

class CipherProvider {

    private val secretKey = getOrCreateSecretKey()

    fun provideInitializedEncryptCipher(): Cipher =
        Cipher.getInstance(CIPHER_TRANSFORMATION).apply {
            init(
                Cipher.ENCRYPT_MODE,
                secretKey
            )
        }

    fun provideInitializedDecryptCipher(iv: ByteArray): Cipher =
        Cipher.getInstance(CIPHER_TRANSFORMATION).apply {
            init(
                Cipher.DECRYPT_MODE,
                secretKey,
                GCMParameterSpec(128, iv)
            )
        }

    private fun getOrCreateSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEYSTORE_NAME)
        keyStore.load(null)
        val key = keyStore.getKey(KEY_NAME, null) as? SecretKey

        return key ?: run {
            val keyGenParams = KeyGenParameterSpec.Builder(
                KEY_NAME,
                KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
            ).apply {
                setBlockModes(CIPHER_BLOCK_MODE)
                setEncryptionPaddings(CIPHER_PADDING)
                setKeySize(CIPHER_KEY_SIZE)
                setUserAuthenticationRequired(false)
            }.build()

            KeyGenerator.getInstance(
                KeyProperties.KEY_ALGORITHM_AES,
                ANDROID_KEYSTORE_NAME
            ).run {
                init(keyGenParams)
                generateKey()
            }
        }
    }

    companion object {
        private const val CIPHER_ALGORITHM = KeyProperties.KEY_ALGORITHM_AES
        private const val CIPHER_BLOCK_MODE = KeyProperties.BLOCK_MODE_GCM
        private const val CIPHER_PADDING = KeyProperties.ENCRYPTION_PADDING_NONE
        private const val CIPHER_KEY_SIZE = 256

        private const val CIPHER_TRANSFORMATION =
            "$CIPHER_ALGORITHM/$CIPHER_BLOCK_MODE/$CIPHER_PADDING"

        private const val KEY_NAME = "SECURED_CAMERA_KEEY"
        private const val ANDROID_KEYSTORE_NAME = "AndroidKeyStore"
    }
}