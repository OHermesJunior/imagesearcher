package com.hermesjunior.imagesearcher.imageuploader

import java.io.File

/**
 * Uploads a file (image), and gives a URL for the image.
 */
interface ImageUploader {
    fun upload(file: File, callback: UploadCallback): Boolean

    interface UploadCallback {
        fun onResult(responseStr: String)
    }
}
