package com.hermesjunior.imagesearcher.ui

import android.app.Application
import android.content.SharedPreferences
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.preference.PreferenceManager
import com.hermesjunior.imagesearcher.R
import com.hermesjunior.imagesearcher.imageuploader.ImageUploader
import com.hermesjunior.imagesearcher.imageuploader.TransfershUploader
import com.hermesjunior.imagesearcher.model.SearchResult
import java.io.File

class MainViewModel(private val context: Application) : AndroidViewModel(context) {

    private var prefs: SharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
    private val imageUploader: ImageUploader = TransfershUploader()
    private val uploadedUrl = MutableLiveData("")
    private val imgPath = MutableLiveData("")
    private val appbarTitle = MutableLiveData(context.getString(R.string.app_name))
    private val allowGoBack = MutableLiveData(false)
    private val showSettingsIcon = MutableLiveData(true)
    private val showLinkIcon = MutableLiveData(true)
    private val searchResults = MutableLiveData<List<SearchResult>>(emptyList())
    private val error = MutableLiveData(false)
    var fragmentTag = ""
        get() = field
        set(value) {
            field = value
        }

    var croppedImgPath = ""

    fun setShowSettingsIcon(allow: Boolean) {
        showSettingsIcon.value = allow
    }

    fun getShowSettingsIcon(): LiveData<Boolean> = showSettingsIcon

    fun setShowLinkIcon(allow: Boolean) {
        showLinkIcon.value = allow
    }

    fun getShowLinkIcon(): LiveData<Boolean> = showLinkIcon

    fun getError(): LiveData<Boolean> = error

    fun shownError() {
        error.value = false
    }

    fun setAllowGoBack(allow: Boolean) {
        allowGoBack.value = allow
    }

    fun getAllowGoBack(): LiveData<Boolean> = allowGoBack

    fun setAppbarTitle(title: String) {
        if (title.isBlank())
            appbarTitle.value = context.getString(R.string.app_name)
        else
            appbarTitle.value = title
    }

    fun getAppbarTitle(): LiveData<String> = appbarTitle

    fun uploadImage() {
        imageUploader.upload(
            File(croppedImgPath),
            object : ImageUploader.UploadCallback {
                override fun onResult(url: String) {
                    if (url == "") {
                        notifyError()
                    } else {
                        setUploadedImageUrl(url)
                    }
                }
            })
    }

    fun getSearchResults(): LiveData<List<SearchResult>> = searchResults

    fun setUploadedImageUrl(url: String) {
        uploadedUrl.postValue(url)

        val engines: Set<String> =
            prefs.getStringSet("engines", setOf("google", "yandex", "bing")) as Set<String>
        val resultKeys = mapOf(
            "google" to SearchResult(
                "Google",
                "https://images.google.com/searchbyimage?image_url=$url",
                ContextCompat.getDrawable(context, R.drawable.ic_google)!!
            ),
            "yandex" to SearchResult(
                "Yandex",
                "https://yandex.com/images/search?rpt=imageview&url=$url",
                ContextCompat.getDrawable(context, R.drawable.ic_yandex)!!
            ),
            "bing" to SearchResult(
                "Bing",
                "https://bing.com/images/search?view=detailv2&iss=sbi&FORM=SBIHMP&q=imgurl:$url",
                ContextCompat.getDrawable(context, R.drawable.ic_bing)!!
            )
        )
        val results = mutableListOf<SearchResult>()
        for (engine in engines) {
            resultKeys[engine]?.let { results.add(it) }
        }
        searchResults.postValue(results)
    }

    fun notifyError() {
        error.postValue(true)
    }

    fun getUploadedImageUrl(): LiveData<String> = uploadedUrl

    fun setImageFilePath(path: String) {
        imgPath.value = path
        croppedImgPath = path // while there is no editing
    }

    fun getImageFilePath(): LiveData<String> = imgPath
}
