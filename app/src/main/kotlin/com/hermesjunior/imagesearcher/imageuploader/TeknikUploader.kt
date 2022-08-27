package com.hermesjunior.imagesearcher.imageuploader

/**
 * https://www.teknik.io/
 * https://git.teknik.io/Teknikode/Teknik
 *
 * Easy and fast file sharing from the command-line.
 */
class TeknikUploader : FormImageUploader() {
    override val formName: String
        get() = "file"
    override val serverURL: String
        get() = "https://api.teknik.io/v1/Upload"
    override val headerData: Map<String, String>
        get() = mapOf(
            "Max-Downloads" to "1",
            "Max-Days" to "1",
        )
}
