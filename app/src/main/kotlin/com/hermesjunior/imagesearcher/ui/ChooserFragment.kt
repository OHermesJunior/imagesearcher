package com.hermesjunior.imagesearcher.ui

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.annotation.Keep
import androidx.fragment.app.activityViewModels
import com.aminography.choosephotohelper.ChoosePhotoHelper
import com.hermesjunior.imagesearcher.R
import com.hermesjunior.imagesearcher.ui.customview.BaseFragment

@Keep
class ChooserFragment : BaseFragment() {

    companion object {
        const val TAG = "ChooserFragment"
    }

    private var choosePhotoHelper: ChoosePhotoHelper? = null
    private val viewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_chooser, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setAppbarTitle("")
        viewModel.setAllowGoBack(false)
        viewModel.setShowSettingsIcon(true)
        viewModel.setShowLinkIcon(false)
        viewModel.fragmentTag = TAG

        view.findViewById<Button>(R.id.btn_pick_image).setOnClickListener {
            choosePhotoHelper?.chooseFromGallery()
        }
        view.findViewById<Button>(R.id.btn_take_pic).setOnClickListener {
            choosePhotoHelper?.takePhoto()
        }

        choosePhotoHelper = ChoosePhotoHelper.with(this)
            .asFilePath()
            .withState(savedInstanceState)
            .build {
                viewModel.setImageFilePath(it.orEmpty())
            }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        choosePhotoHelper?.onActivityResult(requestCode, resultCode, data)
    }

    override fun onRequestPermissionsResult(rqCode: Int, perms: Array<String>, grantRes: IntArray) {
        super.onRequestPermissionsResult(rqCode, perms, grantRes)
        choosePhotoHelper?.onRequestPermissionsResult(rqCode, perms, grantRes)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        choosePhotoHelper?.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }
}
