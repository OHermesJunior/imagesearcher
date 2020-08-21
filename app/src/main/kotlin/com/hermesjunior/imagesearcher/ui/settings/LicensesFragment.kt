package com.hermesjunior.imagesearcher.ui.settings

import android.os.Bundle
import androidx.annotation.Keep
import androidx.fragment.app.activityViewModels
import androidx.preference.PreferenceFragmentCompat
import com.hermesjunior.imagesearcher.R
import com.hermesjunior.imagesearcher.ui.MainViewModel

@Keep
class LicensesFragment : PreferenceFragmentCompat() {

    private val viewModel by activityViewModels<MainViewModel>()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.licenses_preferences, rootKey)

        viewModel.setAllowGoBack(true)
        viewModel.setAppbarTitle(getString(R.string.licenses_title))
    }
}
