package com.dicoding.githubuser.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.adapter.FavoriteAdapter
import com.dicoding.githubuser.databinding.ActivityFavoriteBinding
import com.dicoding.githubuser.db.FavoriteHelper
import com.dicoding.githubuser.helper.MappingHelper
import com.dicoding.githubuser.model.User
import kotlinx.coroutines.*

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var favoriteHelper: FavoriteHelper
    private lateinit var adapter: FavoriteAdapter

    companion object {
        const val EXTRA_STATE = "extra_state"
    }

    @DelicateCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = FavoriteAdapter(this)
        binding.rvFavorite.adapter = adapter
        binding.rvFavorite.layoutManager = LinearLayoutManager(this)
        if (savedInstanceState == null) {
            // proses ambil data
            loadUserAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<User>(EXTRA_STATE)
            if (list != null) {
                adapter.listUser = list
            }
        }
        binding.btnBack.setOnClickListener {
            finish()
        }
    }

    @DelicateCoroutinesApi
    private fun loadUserAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            binding.progressBar.visibility = View.VISIBLE
            favoriteHelper = FavoriteHelper.getInstance(applicationContext)
            favoriteHelper.open()
            val deferredUser = async(Dispatchers.IO) {
                val cursor = favoriteHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
            binding.progressBar.visibility = View.GONE
            val user = deferredUser.await()
            if (user.size > 0) {
                adapter.listUser = user
                binding.dataNotFound.visibility = View.GONE
            } else {
                adapter.listUser = ArrayList()
                binding.dataNotFound.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        favoriteHelper.close()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listUser)
    }


}