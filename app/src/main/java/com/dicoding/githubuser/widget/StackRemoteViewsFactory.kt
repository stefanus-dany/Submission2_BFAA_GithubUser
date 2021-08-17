package com.dicoding.githubuser.widget

import android.content.Context
import android.content.Intent
import android.os.Binder
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import androidx.core.os.bundleOf
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dicoding.githubuser.R
import com.dicoding.githubuser.db.DatabaseUser.UserColumns.Companion.CONTENT_URI
import com.dicoding.githubuser.helper.MappingHelper
import com.dicoding.githubuser.model.User

internal class StackRemoteViewsFactory(private val mContext: Context) :
    RemoteViewsService.RemoteViewsFactory{

    private var mWidgetItems = ArrayList<User>()

    override fun onCreate() {}

    override fun getLoadingView(): RemoteViews? = null

    override fun getItemId(position: Int): Long = 0

    override fun onDataSetChanged() {
        val identityToken = Binder.clearCallingIdentity()
        val cursor = mContext.contentResolver.query(
            CONTENT_URI,
            null,
            null,
            null,
            null)
        if (cursor != null) {
            val list = MappingHelper.mapCursorToArrayList(cursor)
            mWidgetItems.apply {
                clear()
                addAll(list)
            }
            cursor.close()
        }
        Binder.restoreCallingIdentity(identityToken)
    }

    override fun hasStableIds(): Boolean = false

    override fun getViewAt(position: Int): RemoteViews {
        val remoteViews = RemoteViews(mContext.packageName, R.layout.widget_item)
        val bitmap = Glide.with(mContext)
            .asBitmap()
            .load(mWidgetItems[position].img)
            .apply(RequestOptions().centerCrop())
            .submit()
            .get()
        remoteViews.setImageViewBitmap(R.id.imageView, bitmap)

        val extras = bundleOf(ImagesBannerWidget.EXTRA_ITEM to position)
        val fillIntent = Intent()
        fillIntent.putExtras(extras)
        remoteViews.setOnClickFillInIntent(R.id.imageView, fillIntent)
        return remoteViews
    }

    override fun getCount(): Int = mWidgetItems.size

    override fun getViewTypeCount(): Int = 1

    override fun onDestroy() {}
}