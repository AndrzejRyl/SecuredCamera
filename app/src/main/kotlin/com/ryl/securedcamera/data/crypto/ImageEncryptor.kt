package com.ryl.securedcamera.data.crypto

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Log
import java.io.*
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream

interface ImageEncryptor {
    var encryptCipher: Cipher?

    fun encryptImageFile(file: File)
    fun decryptImageFile(file: File): Bitmap?
}

class ImageEncryptorImpl(
    private val cacheDestination: File,
    private val cipherProvider: CipherProvider
) : ImageEncryptor {

    override var encryptCipher: Cipher? = null

    override fun encryptImageFile(file: File) {
        try {
            val input: InputStream = BufferedInputStream(file.inputStream())
            val output = CipherOutputStream(FileOutputStream(getTempFile(file)), encryptCipher)

            val data = ByteArray(1024)
            var total: Long = 0
            var count: Int
            while (input.read(data).also { count = it } != -1) {
                total += count.toLong()
                output.write(data, 0, count)
            }

            output.flush()
            output.close()
            input.close()

            moveTempFileToDestination(file)

        } catch (e: IOException) {
            deleteTempFile(file)
        }
    }

    private fun deleteTempFile(file: File) = getTempFile(file).delete()

    private fun getTempFile(file: File): File = File(cacheDestination, file.name)

    private fun moveTempFileToDestination(file: File) = getTempFile(file).renameTo(file)

    override fun decryptImageFile(file: File): Bitmap? {
        return try {
            encryptCipher?.let {
                val decryptCipher = cipherProvider.provideInitializedDecryptCipher(it.iv)
                val input: InputStream = CipherInputStream(file.inputStream(), decryptCipher)

                BitmapFactory.decodeStream(input)
            }
        } catch (e: IOException) {
            Log.e("Zonk", e.message + "")
            null
        }
    }
}