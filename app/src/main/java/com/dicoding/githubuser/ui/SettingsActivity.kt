package com.dicoding.githubuser.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.dicoding.githubuser.R
import com.dicoding.githubuser.databinding.ActivitySettingsBinding
import com.dicoding.githubuser.settings.MyPreferenceFragment

class SettingsActivity : AppCompatActivity() {

    private lateinit var binding : ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction().add(R.id.setting_holder, MyPreferenceFragment()).commit()
        binding.btnBack.setOnClickListener {
            finish()
        }
    }
}