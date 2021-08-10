package com.dicoding.githubuser.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.githubuser.R
import com.dicoding.githubuser.databinding.UserItemBinding
import com.dicoding.githubuser.model.User

class FollowersAdapter(private val mContext : Context) : RecyclerView.Adapter<FollowersAdapter.ViewHolder>() {

    private val user = ArrayList<User>()

    fun setData(listUser: List<User>?) {
        if (listUser == null) return
        user.clear()
        user.addAll(listUser)
        notifyDataSetChanged()
    }

    class ViewHolder(val binding : UserItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(UserItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.username.text = holder.itemView.resources.getString(R.string.label_username, user[position].username)
        Glide
            .with(mContext)
            .load(user[position].img)
            .into(holder.binding.img)
    }

    override fun getItemCount(): Int {
        return user.size
    }

}