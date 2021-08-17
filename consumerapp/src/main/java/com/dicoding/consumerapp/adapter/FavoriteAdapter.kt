package com.dicoding.consumerapp.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.consumerapp.R
import com.dicoding.consumerapp.databinding.UserItemBinding
import com.dicoding.consumerapp.model.User

class FavoriteAdapter() :
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
        imgConsumerapp.loadImage(listUser[position].img)}
        Log.i("cekIMGAA", "listUser image: ${listUser[position].img})")
    }

    override fun getItemCount(): Int {
        return listUser.size
    }

    private fun ImageView.loadImage(url: String?) {
        Glide.with(this.context)
            .load(url)
            .apply(RequestOptions().override(500, 500))
            .centerCrop()
            .into(this)
    }

}