package com.hermesjunior.imagesearcher.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import androidx.appcompat.widget.AppCompatImageView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.hermesjunior.imagesearcher.R

class EditFragment : Fragment() {

    private lateinit var fab: FloatingActionButton
    private lateinit var progressBar: ProgressBar
    private val viewModel by activityViewModels<MainViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_pic, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.setAppbarTitle("")
        viewModel.setAllowGoBack(true)
        viewModel.setShowSettingsIcon(true)

        val imgView = view.findViewById<AppCompatImageView>(R.id.img_selected)
        Glide.with(this).load(viewModel.getImageFilePath().value).into(imgView)

        progressBar = view.findViewById(R.id.progress_bar)
        fab = view.findViewById(R.id.fab)
        fab.setOnClickListener {
            viewModel.uploadImage()
            progressBar.visibility = View.VISIBLE
        }
    }
}
