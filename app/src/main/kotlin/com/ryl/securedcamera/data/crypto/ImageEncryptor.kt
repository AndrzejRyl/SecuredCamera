package com.ryl.securedcamera.data.crypto

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream

interface ImageEncryptor {
    var encryptCipher: Cipher?

    suspend fun encryptImageFile(file: File, progressCallback: (Int) -> Unit)
    suspend fun decryptImageFile(file: File, onBitmapReady: (Bitmap?) -> Unit)
}

class ImageEncryptorImpl(
    private val cacheDestination: File,
    private val cipherProvider: CipherProvider
) : ImageEncryptor {

    override var encryptCipher: Cipher? = null

    override suspend fun encryptImageFile(file: File, progressCallback: (Int) -> Unit) {
        withContext(Dispatchers.IO) {
            var input: InputStream? = null
            var output: OutputStream? = null
            try {
                withContext(Dispatchers.Main) {
                    progressCallback(INITIAL_PROGRESS)
                }
                input = BufferedInputStream(file.inputStream())
                if (encryptCipher != null) {
                    encryptCipher = cipherProvider.provideInitializedEncryptCipher()
                }
                val fileOutputStream = FileOutputStream(getTempFile(file))
                // First prepend IV
                fileOutputStream.write(encryptCipher?.iv)
                // TODO: Prepend EXIF info as well so as to rotate images properly on decryption

                output = CipherOutputStream(fileOutputStream, encryptCipher)

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

                moveTempFileToDestination(file)

            } catch (e: IOException) {
                deleteTempFile(file)
            } finally {
                output?.close()
                input?.close()
            }
        }
    }

    private fun deleteTempFile(file: File) = getTempFile(file).delete()

    private fun getTempFile(file: File): File = File(cacheDestination, file.name)

    private fun moveTempFileToDestination(file: File) = getTempFile(file).renameTo(file)

    override suspend fun decryptImageFile(file: File, onBitmapReady: (Bitmap?) -> Unit) {
        withContext(Dispatchers.IO) {
            var fileInputStream: FileInputStream? = null
            var cipherInputStream: InputStream? = null
            try {
                encryptCipher?.let {
                    // TODO: Retrieve EXIF info
                    // Retrieve IV
                    val iv = ByteArray(12)
                    fileInputStream = file.inputStream()
                    fileInputStream?.read(iv)

                    val decryptCipher = cipherProvider.provideInitializedDecryptCipher(iv)
                    cipherInputStream = CipherInputStream(fileInputStream, decryptCipher)

                    val result = BitmapFactory.decodeStream(cipherInputStream)
                    withContext(Dispatchers.Main) { onBitmapReady(result) }
                }
            } catch (e: IOException) {
                onBitmapReady(null)
            } finally {
                cipherInputStream?.close()
                fileInputStream?.close()
            }
        }
    }

    companion object {
        private const val INITIAL_PROGRESS = 0
        private const val FULL_PROGRESS = 100
    }
}