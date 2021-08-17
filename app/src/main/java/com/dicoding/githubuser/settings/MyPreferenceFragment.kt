package com.dicoding.githubuser.settings

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.provider.Settings
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.PreferenceScreen
import androidx.preference.SwitchPreference
import com.dicoding.githubuser.AlarmReceiver
import com.dicoding.githubuser.R

class MyPreferenceFragment : PreferenceFragmentCompat(),
    SharedPreferences.OnSharedPreferenceChangeListener {
    private lateinit var REMINDER: String
    private lateinit var LANGUAGE: String
    private lateinit var reminderPreference: SwitchPreference
    private lateinit var languagePreference: PreferenceScreen

    override fun onCreatePreferences(bundle: Bundle?, s: String?) {
        addPreferencesFromResource(R.xml.preferences)
        init()
        setSummaries()
    }

    private fun init() {
        REMINDER = resources.getString(R.string.key_reminder)
        LANGUAGE = resources.getString(R.string.key_change_language)
        reminderPreference = findPreference<SwitchPreference>(REMINDER) as SwitchPreference
        languagePreference = findPreference<PreferenceScreen>(LANGUAGE) as PreferenceScreen

        languagePreference.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
            true
        }

        val alarmReceiver = AlarmReceiver()
        reminderPreference.onPreferenceClickListener = Preference.OnPreferenceClickListener {
            if (reminderPreference.isChecked) {
                val repeatTime = resources.getString(R.string.alarm_repeat_time)
                val repeatMessage = resources.getString(R.string.alarm_repeat_message)
                alarmReceiver.setRepeatingAlarm(
                    requireContext(),
                    repeatTime, repeatMessage
                )
            } else {
                alarmReceiver.cancelAlarm(requireContext())
            }
            true
        }

    }

    private fun setSummaries() {
        val sh = preferenceManager.sharedPreferences
        reminderPreference.isChecked = sh.getBoolean(REMINDER, false)
    }

    override fun onResume() {
        super.onResume()
        preferenceScreen.sharedPreferences.registerOnSharedPreferenceChangeListener(this)
    }

    override fun onPause() {
        super.onPause()
        preferenceScreen.sharedPreferences.unregisterOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (sharedPreferences != null) {
            if (key == REMINDER) {
                reminderPreference.isChecked = sharedPreferences.getBoolean(REMINDER, false)
            }
        }
    }

}