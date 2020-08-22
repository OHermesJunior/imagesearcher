package com.hermesjunior.imagesearcher.imageuploader

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File
import java.io.IOException

/**
 * Uploader for form based services.
 */
abstract class FormImageUploader : ImageUploader {

    protected abstract val formName: String
    protected abstract val serverURL: String

    override fun upload(file: File, callback: ImageUploader.UploadCallback): Boolean {
        try {
            val requestBody: RequestBody = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                    formName, file.name, file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                )
                .build()
            val request: Request = Request.Builder()
                .url(serverURL)
                .header("User-Agent", "curl")
                .post(requestBody)
                .build()
            val client = OkHttpClient()

            client.newCall(request).enqueue(object : Callback {
                override fun onFailure(call: Call, e: IOException) {
                    callback.onResult("")
                }

                override fun onResponse(call: Call, response: Response) {
                    if (response.isSuccessful) {
                        callback.onResult(response.body?.string().orEmpty())
                    }
                }
            })

            return true
        } catch (e: Exception) {
            e.printStackTrace()
            callback.onResult("")
        }
        return false
    }
}
