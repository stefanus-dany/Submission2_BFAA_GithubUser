package com.dicoding.githubuser.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.githubuser.R
import com.dicoding.githubuser.databinding.UserItemBinding
import com.dicoding.githubuser.model.User

class FavoriteAdapter(private val mContext: Context) :
    RecyclerView.Adapter<FavoriteAdapter.ViewHolder>() {

    var listUser = ArrayList<User>()
        set(listUser) {
            if (listUser.size > 0) {
                this.listUser.clear()
            }
            this.listUser.addAll(listUser)
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
        with(holder.binding){
        username.text =
            holder.itemView.resources.getString(R.string.label_username, listUser[position].username)
        img.loadImage(listUser[position].img)}
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

//    fun addItem(user: User) {
//        this.listUser.add(user)
//        notifyItemInserted(this.listUser.size - 1)
//    }
//    fun updateItem(position: Int, user: User) {
//        this.listUser[position] = user
//        notifyItemChanged(position, user)
//    }
    fun removeItem(position: Int) {
        this.listUser.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, this.listUser.size)
    }

    private fun ImageView.loadImage(url: String?) {
        Glide.with(this.context)
            .load(url)
            .apply(RequestOptions().override(500, 500))
            .centerCrop()
            .into(this)
    }

}