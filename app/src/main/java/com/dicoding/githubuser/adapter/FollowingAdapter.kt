package com.dicoding.githubuser.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.githubuser.R
import com.dicoding.githubuser.databinding.UserItemBinding
import com.dicoding.githubuser.model.User

class FollowingAdapter : RecyclerView.Adapter<FollowingAdapter.ViewHolder>() {

    private val user = ArrayList<User>()

    fun setData(listUser: List<User>?) {
        if (listUser == null) return
        user.clear()
        user.addAll(listUser)
        notifyDataSetChanged()
    }

    class ViewHolder(val binding: UserItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            UserItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.username.text =
            holder.itemView.resources.getString(R.string.label_username, user[position].username)
        holder.binding.img.loadImage(user[position].img)
    }

    override fun getItemCount(): Int {
        return user.size
    }

    private fun ImageView.loadImage(url: String?) {
        Glide.with(this.context)
            .load(url)
            .apply(RequestOptions().override(500, 500))
            .centerCrop()
            .into(this)
    }

}