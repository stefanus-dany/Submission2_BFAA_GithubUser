package com.dicoding.githubuser.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.githubuser.adapter.UserAdapter
import com.dicoding.githubuser.databinding.ActivityMainBinding
import com.dicoding.githubuser.model.User
import com.dicoding.githubuser.viewModel.UserViewModel
import java.util.*

class MainActivity : AppCompatActivity(), UserAdapter.OnItemClickCallback {
    private lateinit var binding: ActivityMainBinding
    private lateinit var userAdapter: UserAdapter
    private lateinit var viewModel: UserViewModel
    private lateinit var data: MutableList<User>
    private var username = ""

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        const val KEY = "key"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        data = mutableListOf()
        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[UserViewModel()::class.java]
        getUser()
        search()
        if (savedInstanceState != null) {
            val uname = savedInstanceState.getString(KEY)
            if (uname != null) {
                getUserSearch(uname)
            }
            username = savedInstanceState.getString(KEY).toString()
        }
    }

    private fun getUser() {
        binding.progressBar.visibility = View.VISIBLE
        viewModel.getDataFromAPI().observe(this@MainActivity, {
            data = it
            userAdapter = UserAdapter(this@MainActivity, this)
            userAdapter.setData(data)
            binding.rv.adapter = userAdapter
            binding.rv.layoutManager = LinearLayoutManager(this@MainActivity)
            binding.progressBar.visibility = View.GONE
        })
    }

    private fun search() {
        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(query: String?): Boolean {
                Log.i("cekquery", "query: $query")
                if (query.isNullOrEmpty()) {
                    Log.i("cek", "onQueryTextSubmit: masuk null $query")
                    userAdapter.setData(data)
                } else {
                    username = query
                    getUserSearch(query)
                    Log.i("cek", "onQueryTextSubmit: masuk else $query")
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.equals("")) {
                    try {
                        userAdapter.setData(data)
                        binding.rv.visibility = View.VISIBLE
                        binding.dataNotFound.visibility = View.GONE
                    } catch (e: UninitializedPropertyAccessException) {
                        Log.d(TAG, "onQueryTextChange: ${e.message}")
                    }
                }
                return true
            }
        })
    }

    private fun getUserSearch(username: String) {
        binding.progressBar.visibility = View.VISIBLE
        viewModel.getDataSearchFromAPI(username).observe(this, {
            if (it.isNullOrEmpty()) {
                binding.dataNotFound.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
                binding.rv.visibility = View.GONE
            } else {
                binding.rv.visibility = View.VISIBLE
                binding.dataNotFound.visibility = View.GONE
                binding.progressBar.visibility = View.GONE
                Log.i("cek", "getUserSearch: $it")
                Log.i("cek", "getUserSearch: $username")
                userAdapter.setData(it)
            }
        })
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

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(KEY, username)
    }
}
