package com.hermesjunior.imagesearcher.imageuploader

/**
 * https://oshi.at
 * https://github.com/somenonymous/OshiUpload
 *
 * OshiUpload is a powerful anonymous public file sharing FLOSS.
 */
class OshiUploader : FormImageUploader() {
    override val formName: String
        get() = "f"
    override val serverURL: String
        get() = "https://oshi.at"
    override val formData: Map<String, String>
        get() = mapOf(
            "expire" to "60",
            "shorturl" to "1",
            "randomizefn" to "1"
        )
    override val reponseExtractor: (String) -> String
        get() = { res ->
            val dlStart = res.indexOf("DL") + 4
            val dlEnd = res.indexOf("\n", dlStart)
            res.substring(dlStart, dlEnd)
        }
}
