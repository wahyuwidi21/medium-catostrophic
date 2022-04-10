package com.catastrophic.app.ui.adapter

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.catastrophic.app.data.model.Cat
import com.catastrophic.app.databinding.ItemCatBinding




class CatsAdapter(diffCallback: DiffUtil.ItemCallback<Cat>, private val onItemClicked: (userName:String?) -> Unit?) :
    PagingDataAdapter<Cat,CatsAdapter.ViewHolder>(diffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCatBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val layoutParams: ViewGroup.LayoutParams =binding.root.layoutParams
        layoutParams.width = parent.width/3
        binding.root.layoutParams = layoutParams
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(getItem(position))

        holder.itemView.setOnClickListener {

        }
    }

    class ViewHolder(private val binding: ItemCatBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(data: Cat?) {
            binding.catSkeleton.showSkeleton()
            val image = GlideUrl(
                data?.url, LazyHeaders.Builder()
                    .addHeader("User-Agent", "5")
                    .build()
            )

            Glide.with(itemView.context)
                .load(data?.image_blob)
                .addListener(object : RequestListener<Drawable>{
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.catSkeleton.showOriginal()
                        return false
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.catSkeleton.showOriginal()
                        return false
                    }

                })
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .centerCrop()
                .skipMemoryCache(false)
                .into(binding.catImg)
        }
    }



    object UserComparator : DiffUtil.ItemCallback<Cat>() {
        override fun areItemsTheSame(oldItem: Cat, newItem: Cat): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Cat, newItem: Cat): Boolean {
            return oldItem == newItem
        }
    }
}