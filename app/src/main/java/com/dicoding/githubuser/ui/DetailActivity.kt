package com.dicoding.githubuser.ui

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.githubuser.R
import com.dicoding.githubuser.databinding.DetailActivityBinding
import com.dicoding.githubuser.model.User
import com.google.android.material.tabs.TabLayoutMediator

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: DetailActivityBinding

    companion object {
        const val EXTRA_USER = "extra_user"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DetailActivityBinding.inflate(layoutInflater)
        binding.progressBar.visibility = View.VISIBLE
        setContentView(binding.root)
        val dataUser = intent.getParcelableExtra<User>(EXTRA_USER)
        if (dataUser != null) {
            binding.img.loadImage(dataUser.img)
            if (dataUser.name.equals("null")) {
                binding.name.text = resources.getString(R.string.not_found_label)
            } else {
                binding.name.text = resources.getString(R.string.label_name, dataUser.name)
            }

            if (dataUser.username.equals("null")) {
                binding.username.text = resources.getString(R.string.not_found_label)
            } else {
                binding.username.text = resources.getString(R.string.label_username, dataUser.username)
            }

            if (dataUser.following.equals("null")) {
                binding.following.text =
                    resources.getString(R.string.not_found_label)
            } else {
                binding.following.text =
                    resources.getString(R.string.label_following, dataUser.following)
            }

            if (dataUser.followers.equals("null")) {
                binding.followers.text =
                    resources.getString(R.string.not_found_label)
            } else {
                binding.followers.text =
                    resources.getString(R.string.label_followers, dataUser.followers)
            }

            if (dataUser.company.equals("null")) {
                binding.company.text = resources.getString(R.string.not_found_label)
            } else {
                binding.company.text = resources.getString(R.string.label_company, dataUser.company)
            }

            if (dataUser.location.equals("null")) {
                binding.location.text = resources.getString(R.string.not_found_label)
            } else {
                binding.location.text = resources.getString(R.string.label_location, dataUser.location)
            }

            when {
                dataUser.repository.equals("null") -> {
                    binding.repository.text =
                        resources.getString(R.string.not_found_label)
                }
                dataUser.name.equals("null") -> {
                    val notFound = resources.getString(R.string.not_found_label)
                    binding.repository.text =
                        resources.getString(R.string.label_repo, notFound, dataUser.repository)
                }
                else -> {
                    binding.repository.text =
                        resources.getString(R.string.label_repo, dataUser.name, dataUser.repository)
                }
            }
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        binding.viewPager2.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager2) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
        setSupportActionBar(binding.toolbar)
        binding.progressBar.visibility = View.GONE
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.action_change_settings) {
            val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
            startActivity(mIntent)
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    private fun ImageView.loadImage(url: String?) {
        Glide.with(this.context)
            .load(url)
            .apply(RequestOptions().override(500, 500))
            .centerCrop()
            .into(this)
    }

}