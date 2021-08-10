package com.dicoding.githubuser.viewModel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.githubuser.model.User
import com.dicoding.githubuser.ui.MainActivity
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import org.json.JSONObject

class UserViewModel : ViewModel() {

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    fun getDataFromAPI(): LiveData<MutableList<User>> {
        val data = MutableLiveData<MutableList<User>>()
        val dataUser = mutableListOf<User>()

        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users"
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                try {
                    val jsonArray = JSONArray(result)
                    for (i in 0 until jsonArray.length()) {
                        val jsonObject = jsonArray.getJSONObject(i)
                        val img = jsonObject.getString("avatar_url")
                        val username = jsonObject.getString("login")
                        dataUser.add(User(img = img, username = username))
                    }
                    data.value = dataUser
                } catch (e: Exception) {
                    Log.d(TAG, "Error: ${e.message}")
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message}"
                }
                Log.d(TAG, errorMessage)
            }

        })
        return data
    }

    fun getDataSearchFromAPI(username: String): LiveData<MutableList<User>> {
        val data = MutableLiveData<MutableList<User>>()
        val dataUser = mutableListOf<User>()

        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/search/users?q=$username"
        Log.i("cek", "getDataSearchFromAPI: $url")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                try {
                    Log.i("cek", "onSuccessbro: $username")
                    val jsonObject = JSONObject(result)
                    Log.i("cek", "onSuccess: $jsonObject")
                    val jsonArray = jsonObject.getJSONArray("items")
                    Log.i("cek", "onSuccess: $jsonArray")
                    for (i in 0 until jsonArray.length()) {
                        val item = jsonArray.getJSONObject(i)
                        val img = item.getString("avatar_url")
                        val usernameDataSearch = item.getString("login")
                        dataUser.add(User(img = img, username = usernameDataSearch))
                        Log.i("cek", "onSuccess: cek img + username$img + $username")
                    }
                    data.value = dataUser
                    Log.i("cek", "onSuccess cek data value: ${data.value}")
                } catch (e: Exception) {
                    Log.d(TAG, "Error: ${e.message}")
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message}"
                }
                Log.d(TAG, errorMessage)
            }

        })
        return data
    }

    fun getDetailDataFromAPI(login: String): LiveData<User> {
        val data = MutableLiveData<User>()

        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/$login"
        Log.i("cek", "getDataSearchFromAPI: $url")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                try {
                    Log.i("cek", "onSuccessbro: $login")
                    val item = JSONObject(result)
                    Log.i("cek", "onSuccess: $item")
                    for (i in 0 until item.length()) {
                        val img = item.getString("avatar_url")
                        val name = item.getString("name")
                        val usernameDetailData = item.getString("login")
                        val following = item.getString("following")
                        val followers = item.getString("followers")
                        val company = item.getString("company")
                        val location = item.getString("location")
                        val repository = item.getString("public_repos")
                        data.value = User(
                            img,
                            name,
                            usernameDetailData, following, followers, company, location, repository
                        )
                    }
                    Log.i("cek", "onSuccess cek data value: ${data.value}")
                } catch (e: Exception) {
                    Log.d(TAG, "Error: ${e.message}")
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message}"
                }
                Log.d(TAG, errorMessage)
            }

        })
        return data
    }

    fun getDataFollowingFromAPI(username: String): LiveData<MutableList<User>> {
        val data = MutableLiveData<MutableList<User>>()
        val dataUser = mutableListOf<User>()

        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/$username/following"
        Log.i("cek", "getDataSearchFromAPI: $url")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                try {
                    Log.i("cek", "onSuccessbro: $username")
                    val jsonArray = JSONArray(result)
                    for (i in 0 until jsonArray.length()) {
                        val item = jsonArray.getJSONObject(i)
                        val img = item.getString("avatar_url")
                        val usernameDataFollowing = item.getString("login")
                        dataUser.add(User(img = img, username = usernameDataFollowing))
                        Log.i("cek", "onSuccess: cek img + username$img + $username")
                    }
                    data.value = dataUser
                    Log.i("cek", "onSuccess cek data value: ${data.value}")
                } catch (e: Exception) {
                    Log.d(TAG, "Error: ${e.message}")
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message}"
                }
                Log.d(TAG, errorMessage)
            }

        })
        return data
    }

    fun getDataFollowersFromAPI(username: String): LiveData<MutableList<User>> {
        val data = MutableLiveData<MutableList<User>>()
        val dataUser = mutableListOf<User>()

        val client = AsyncHttpClient()
        client.addHeader("Authorization", "token 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")
        client.addHeader("User-Agent", "request")
        val url = "https://api.github.com/users/$username/followers"
        Log.i("cek", "getDataSearchFromAPI: $url")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>,
                responseBody: ByteArray
            ) {
                val result = String(responseBody)
                try {
                    Log.i("cek", "onSuccessbro: $username")
                    val jsonArray = JSONArray(result)
                    for (i in 0 until jsonArray.length()) {
                        val item = jsonArray.getJSONObject(i)
                        val img = item.getString("avatar_url")
                        val usernameDataFollowing = item.getString("login")
                        dataUser.add(User(img = img, username = usernameDataFollowing))
                        Log.i("cek", "onSuccess: cek img + username$img + $username")
                    }
                    data.value = dataUser
                    Log.i("cek", "onSuccess cek data value: ${data.value}")
                } catch (e: Exception) {
                    Log.d(TAG, "Error: ${e.message}")
                }
            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message}"
                }
                Log.d(TAG, errorMessage)
            }

        })
        return data
    }

}