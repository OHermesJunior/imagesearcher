package com.hermesjunior.imagesearcher.ui.settings

import android.os.Bundle
import android.widget.Toast
import androidx.annotation.Keep
import androidx.fragment.app.activityViewModels
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.hermesjunior.imagesearcher.BuildConfig
import com.hermesjunior.imagesearcher.R
import com.hermesjunior.imagesearcher.ui.MainViewModel

@Keep
class SettingsFragment : PreferenceFragmentCompat() {

    private val viewModel by activityViewModels<MainViewModel>()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.main_preferences, rootKey)

        findPreference<Preference>("version")?.apply {
            summary = getString(R.string.version_summary, BuildConfig.VERSION_NAME)
            var clickedTimes = 0
            setOnPreferenceClickListener {
                clickedTimes++
                if (clickedTimes > 6) {
                    Toast.makeText(activity, R.string.easter_egg, Toast.LENGTH_SHORT).show()
                    clickedTimes = 0
                }
                return@setOnPreferenceClickListener true
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.setAppbarTitle(getString(R.string.settings_title))
        viewModel.setAllowGoBack(true)
        viewModel.setShowSettingsIcon(false)
    }
}
