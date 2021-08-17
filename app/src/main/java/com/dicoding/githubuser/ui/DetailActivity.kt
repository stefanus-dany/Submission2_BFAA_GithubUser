package com.dicoding.githubuser.ui

import android.content.ContentValues
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.githubuser.R
import com.dicoding.githubuser.databinding.DetailActivityBinding
import com.dicoding.githubuser.db.DatabaseUser
import com.dicoding.githubuser.db.DatabaseUser.UserColumns.Companion.CONTENT_URI
import com.dicoding.githubuser.db.FavoriteHelper
import com.dicoding.githubuser.helper.MappingHelper
import com.dicoding.githubuser.model.User
import com.google.android.material.tabs.TabLayoutMediator
import kotlinx.coroutines.*

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: DetailActivityBinding
    private var isFavorit = false
    private lateinit var favoriteHelper: FavoriteHelper
    private lateinit var values: ContentValues
    private lateinit var uriWithId: Uri
    private var dataUser: User? = null

    companion object {
        const val EXTRA_USER = "extra_user"

        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    @DelicateCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DetailActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.progressBar.visibility = View.VISIBLE
        favoriteHelper = FavoriteHelper.getInstance(applicationContext)
        favoriteHelper.open()
        values = ContentValues()
        var dataUser = intent.getParcelableExtra<User>(EXTRA_USER)
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
                binding.username.text =
                    resources.getString(R.string.label_username, dataUser.username)
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
                binding.location.text =
                    resources.getString(R.string.label_location, dataUser.location)
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
            values.put(DatabaseUser.UserColumns.COMPANY, dataUser.company)
            values.put(DatabaseUser.UserColumns.FOLLOWERS, dataUser.followers)
            values.put(DatabaseUser.UserColumns.FOLLOWING, dataUser.following)
            values.put(DatabaseUser.UserColumns.IMG, dataUser.img)
            values.put(DatabaseUser.UserColumns.LOCATION, dataUser.location)
            values.put(DatabaseUser.UserColumns.NAME, dataUser.name)
            values.put(DatabaseUser.UserColumns._USERNAME, dataUser.username)
            values.put(DatabaseUser.UserColumns.REPOSITORY, dataUser.repository)
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        binding.viewPager2.adapter = sectionsPagerAdapter
        TabLayoutMediator(binding.tabs, binding.viewPager2) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        binding.progressBar.visibility = View.GONE
        binding.btnBack.setOnClickListener {
            finish()
        }
        Log.i("cueks", "isFavorite before load: $isFavorit")
        loadUsernameAsync(dataUser)
        Log.i("cueks", "isFavorite after load: $isFavorit")

        uriWithId = Uri.parse(CONTENT_URI.toString() + "/" + dataUser?.username)
        val cursor = contentResolver.query(uriWithId, null, null, null, null)
        if (cursor != null) {
            dataUser = MappingHelper.mapCursorToObject(cursor)
            cursor.close()
        }

        binding.icFav.setOnClickListener {
            if (!isFavorit) {
                isFavorit = true
                binding.icFav.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_favorite_enabled
                )
//                favoriteHelper.insert(values)
                contentResolver.insert(CONTENT_URI, values)
            } else {
                isFavorit = false
                binding.icFav.background = ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_favorite_disable
                )
//                contentResolver.delete(uriWithId, null, null)
                favoriteHelper.deleteByUsername(dataUser?.username.toString())
            }

        }
    }

    private fun ImageView.loadImage(url: String?) {
        Glide.with(this.context)
            .load(url)
            .apply(RequestOptions().override(500, 500))
            .centerCrop()
            .into(this)
    }

    @DelicateCoroutinesApi
    private fun loadUsernameAsync(dataUser: User?) {
        GlobalScope.launch(Dispatchers.Main) {
            val deferredNotes = async(Dispatchers.IO) {
                val cursor = favoriteHelper.queryByUsername(dataUser?.username.toString())
                MappingHelper.mapCursorToArrayList(cursor)
            }
            val user = deferredNotes.await()
            isFavorit = user.size > 0

            if (!isFavorit) {
                binding.icFav.background = ContextCompat.getDrawable(
                    this@DetailActivity,
                    R.drawable.ic_favorite_disable
                )
            } else {
                binding.icFav.background = ContextCompat.getDrawable(
                    this@DetailActivity,
                    R.drawable.ic_favorite_enabled
                )
            }
            Log.i("cueks", "isFavorite at in load: $isFavorit")
        }
        Log.i("cueks", "isFavorite at load: $isFavorit")
    }

    override fun onDestroy() {
        super.onDestroy()
        favoriteHelper.close()
    }


}