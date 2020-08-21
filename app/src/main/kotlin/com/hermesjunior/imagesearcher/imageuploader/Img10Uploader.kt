package com.hermesjunior.imagesearcher.imageuploader

/**
 * A free, temporary image host service.
 */
class Img10Uploader : FormImageUploader() {
    override val formName: String
        get() = "img"
    override val serverURL: String
        get() = "https://img10-1342.appspot.com/upload"
}
