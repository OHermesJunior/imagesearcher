package com.hermesjunior.imagesearcher.imageuploader

/**
 * https://transfer.sh
 * https://github.com/dutchcoders/transfer.sh
 *
 * Easy and fast file sharing from the command-line.
 */
class TransfershUploader : FormImageUploader() {
    override val formName: String
        get() = "filedata"
    override val serverURL: String
        get() = "https://transfer.sh"
    override val headerData: Map<String, String>
        get() = mapOf(
            "Max-Downloads" to "1",
            "Max-Days" to "1",
        )
}
