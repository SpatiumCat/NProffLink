package ru.netology.nprofflink.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import ru.netology.nprofflink.BuildConfig.BASE_URL
import ru.netology.nprofflink.R
import ru.netology.nprofflink.databinding.CardPostBinding
import ru.netology.nprofflink.dto.Post

interface OnInteractionListener {
    fun onLike(post: Post)
    fun onShare(post: Post)
}

class PostAdapter(
    private val onInteractionListener: OnInteractionListener
): PagingDataAdapter<Post, PostViewHolder>(PostDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PostViewHolder {
        val binding = CardPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PostViewHolder(binding, onInteractionListener )
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        val post = getItem(position)
        post?.let { holder.bind(it) }
    }
}

class PostViewHolder(
    private val binding: CardPostBinding,
    private val onInteractionListener: OnInteractionListener,
): RecyclerView.ViewHolder(binding.root) {
    fun bind(post: Post) {
        binding.apply {
            authorName.text = post.author
            authorJob.text = post.authorJob
            published.text = post.published
            textContent.text = post.content
            likeButton.isChecked = post.likedByMe
            likeButton.text = post.likeOwnerIds.size.toString()

            imageContent.visibility = if (post.attachment == null) View.GONE else {
                Glide.with(binding.imageContent)
                    .load(post.attachment.url)
                    .placeholder(R.drawable.ic_loading_100dp)
                    .error(R.drawable.ic_error_100dp)
                    .timeout(10_000)
                    .into(binding.imageContent)
                View.VISIBLE
            }

            if (!post.authorAvatar.isNullOrBlank()) {
                Glide.with(binding.avatar)
                    .load("${post.authorAvatar}")
                    .placeholder(R.drawable.ic_loading_100dp)
                    .error(R.drawable.ic_error_100dp)
                    .timeout(10000)
                    .transform(CircleCrop())
                    .into(binding.avatar)
            } else {
                Glide.with(binding.avatar).clear(binding.avatar)
                binding.avatar.setImageResource(R.drawable.account_circle_24)
            }

            likeButton.setOnClickListener {
                onInteractionListener.onLike(post)
            }
            shareButton.setOnClickListener {
                onInteractionListener.onShare(post)
            }
        }
    }
}

class PostDiffCallback: DiffUtil.ItemCallback<Post> () {
    override fun areItemsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Post, newItem: Post): Boolean {
        return oldItem == newItem
    }
}