package com.hermesjunior.imagesearcher.imageuploader

import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException

/**
 * Uploader for form based services.
 */
abstract class FormImageUploader : ImageUploader {

    protected abstract val formName: String
    protected abstract val serverURL: String
    protected open val reponseExtractor: (String) -> String = { s -> s }
    protected open val formData: Map<String, String> = emptyMap()

    override fun upload(file: File, callback: ImageUploader.UploadCallback): Boolean {
        try {
            val requestBuilder = MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                    formName, file.name, file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                )
            formData.forEach {
                requestBuilder.addFormDataPart(it.key, it.value)
            }
            val requestBody = requestBuilder.build()
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
                        val imgUrl = reponseExtractor(response.body?.string().orEmpty())
                        callback.onResult(imgUrl)
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
