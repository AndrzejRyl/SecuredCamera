package com.ryl.securedcamera.data.crypto

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import java.io.*
import javax.crypto.Cipher
import javax.crypto.CipherInputStream
import javax.crypto.CipherOutputStream

interface ImageEncryptor {
    var cipher: Cipher?

    fun encryptImageFile(file: File)
    fun decryptImageFile(file: File): Bitmap?
}

class ImageEncryptorImpl(
    private val cacheDestination: File
) : ImageEncryptor {

    override var cipher: Cipher? = null

    override fun encryptImageFile(file: File) {
        try {
            val input: InputStream = BufferedInputStream(file.inputStream())
            val output = CipherOutputStream(FileOutputStream(getTempFile(file)), cipher)

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
            val input: InputStream = CipherInputStream(file.inputStream(), cipher)

            BitmapFactory.decodeStream(input)
        } catch (e: IOException) {
            null
        }
    }
}