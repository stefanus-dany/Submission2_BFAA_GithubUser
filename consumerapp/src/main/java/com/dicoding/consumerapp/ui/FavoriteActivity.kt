package com.dicoding.consumerapp.ui

import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.consumerapp.adapter.FavoriteAdapter
import com.dicoding.consumerapp.databinding.ActivityFavoriteBinding
import com.dicoding.consumerapp.db.DatabaseUser.UserColumns.Companion.CONTENT_URI
import com.dicoding.consumerapp.helper.MappingHelper
import kotlinx.coroutines.*

class FavoriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var adapter: FavoriteAdapter

    companion object {
        const val EXTRA_STATE = "extra_state"
    }

    @DelicateCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        adapter = FavoriteAdapter()
        binding.rvFavorite.adapter = adapter
        binding.rvFavorite.layoutManager = LinearLayoutManager(this)

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)

        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadUserAsync()
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

    }

    @DelicateCoroutinesApi
    override fun onResume() {
        super.onResume()
        loadUserAsync()
    }

    @DelicateCoroutinesApi
    private fun loadUserAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            binding.progressBar.visibility = View.VISIBLE
            val deferredUser = async(Dispatchers.IO) {
//                val cursor = favoriteHelper.queryAll()
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }
            binding.progressBar.visibility = View.GONE
            val user = deferredUser.await()
            if (user.size > 0) {
                adapter.listUser = user
                Log.i("cek3kali", "user: $user")
                binding.dataNotFound.visibility = View.GONE
            } else {
                adapter.listUser = ArrayList()
                binding.dataNotFound.visibility = View.VISIBLE
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, adapter.listUser)
    }

}