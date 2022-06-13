package com.intermediate.submission1.ui.settings

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.intermediate.submission1.R
import com.intermediate.submission1.data.preferences.SettingPreferences
import com.intermediate.submission1.databinding.FragmentSettingsBinding


private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class SettingsFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding

    private val viewModel: SettingsViewModel by activityViewModels {
        SettingsViewModelFactory(
            SettingPreferences.getInstance(context?.dataStore as DataStore<Preferences>)
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var isDarkMode = false
        viewModel.getThemeSettings().observe(viewLifecycleOwner) {
                isDarkModeActive: Boolean ->
            if(isDarkModeActive){
                binding?.themeIcon?.setImageResource(R.drawable.ic_themes_light)
                binding?.themeIcon?.contentDescription = resources.getString(R.string.light_mode)
                binding?.themeTitle?.text = resources.getString(R.string.light_mode)
            }else{
                binding?.themeIcon?.setImageResource(R.drawable.ic_themes_night)
                binding?.themeIcon?.contentDescription = resources.getString(R.string.dark_mode)
                binding?.themeTitle?.text = resources.getString(R.string.dark_mode)
                isDarkMode = true
            }
        }
        binding?.activate?.setOnClickListener {
            viewModel.saveThemeSetting(isDarkMode)
            dismiss()
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}