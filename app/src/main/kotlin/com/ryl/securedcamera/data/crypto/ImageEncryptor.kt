package com.ryl.securedcamera.data.crypto

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream

interface ImageEncryptor {
    var encryptCipher: Cipher?

    suspend fun encryptImageFile(file: File, progressCallback: (Int) -> Unit)
    fun decryptImageFile(file: File): Bitmap?
}

class ImageEncryptorImpl(
    private val cacheDestination: File,
    private val cipherProvider: CipherProvider
) : ImageEncryptor {

    override var encryptCipher: Cipher? = null

    override suspend fun encryptImageFile(file: File, progressCallback: (Int) -> Unit) {
        withContext(Dispatchers.IO) {
            try {
                withContext(Dispatchers.Main) {
                    progressCallback(INITIAL_PROGRESS)
                }
                val input: InputStream = BufferedInputStream(file.inputStream())
                if (encryptCipher != null) {
                    encryptCipher = cipherProvider.provideInitializedEncryptCipher()
                }
                val fileOutputStream = FileOutputStream(getTempFile(file))
                // First prepend IV
                fileOutputStream.write(encryptCipher?.iv)

                val output = CipherOutputStream(fileOutputStream, encryptCipher)

                val data = ByteArray(1024)
                var total: Long = 0
                var count: Int
                while (input.read(data).also { count = it } != -1) {
                    total += count.toLong()
                    output.write(data, 0, count)
                    withContext(Dispatchers.Main) {
                        progressCallback((FULL_PROGRESS * total / file.length()).toInt())
                    }
                }

                output.flush()
                output.close()
                input.close()

                moveTempFileToDestination(file)

            } catch (e: IOException) {
                deleteTempFile(file)
            }
        }
    }

    private fun deleteTempFile(file: File) = getTempFile(file).delete()

    private fun getTempFile(file: File): File = File(cacheDestination, file.name)

    private fun moveTempFileToDestination(file: File) = getTempFile(file).renameTo(file)

    override fun decryptImageFile(file: File): Bitmap? {
        return try {
            encryptCipher?.let {
                // Retrieve IV
                val iv = ByteArray(12)
                val fileInputStream = file.inputStream()
                fileInputStream.read(iv)

                val decryptCipher = cipherProvider.provideInitializedDecryptCipher(iv)
                val input: InputStream = CipherInputStream(fileInputStream, decryptCipher)

                BitmapFactory.decodeStream(input)
            }
        } catch (e: IOException) {
            Log.e("Zonk", e.message + "")
            null
        }
    }

    companion object {
        private const val INITIAL_PROGRESS = 0
        private const val FULL_PROGRESS = 100
    }
}