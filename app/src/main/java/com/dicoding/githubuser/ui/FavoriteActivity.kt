package com.dicoding.githubuser.ui

import android.content.Intent
import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.adapter.FavoriteAdapter
import com.dicoding.githubuser.databinding.ActivityFavoriteBinding
import com.dicoding.githubuser.db.DatabaseUser.UserColumns.Companion.CONTENT_URI
import com.dicoding.githubuser.db.FavoriteHelper
import com.dicoding.githubuser.helper.MappingHelper
import com.dicoding.githubuser.model.User
import com.dicoding.githubuser.viewModel.UserViewModel
import kotlinx.coroutines.*

class FavoriteActivity : AppCompatActivity(), FavoriteAdapter.OnItemClickCallback {

    private lateinit var binding: ActivityFavoriteBinding
    private lateinit var favoriteHelper: FavoriteHelper
    private lateinit var adapter: FavoriteAdapter
    private lateinit var viewModel: UserViewModel

    companion object {
        const val EXTRA_STATE = "extra_state"
    }

    @DelicateCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[UserViewModel()::class.java]
        adapter = FavoriteAdapter(this, this)
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
    private fun loadUserAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            binding.progressBar.visibility = View.VISIBLE
            favoriteHelper = FavoriteHelper.getInstance(applicationContext)
            favoriteHelper.open()
            val deferredUser = async(Dispatchers.IO) {
//                val cursor = favoriteHelper.queryAll()
                val cursor = contentResolver.query(CONTENT_URI, null, null, null, null)
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

    private fun <T> LiveData<T>.observeOnce(lifecycleOwner: LifecycleOwner, observer: Observer<T>) {
        observe(lifecycleOwner, object : Observer<T> {
            override fun onChanged(t: T?) {
                observer.onChanged(t)
                removeObserver(this)
            }
        })
    }

    override fun onItemClicked(login: String) {
        val observe = viewModel.getDetailDataFromAPI(login)
        observe.observeOnce(this, {
            if (it != null) {
                val intent = Intent(this, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_USER, it)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            }
        })
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