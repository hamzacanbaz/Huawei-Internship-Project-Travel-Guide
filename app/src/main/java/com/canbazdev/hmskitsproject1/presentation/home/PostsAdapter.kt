package com.canbazdev.hmskitsproject1.presentation.home

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.canbazdev.hmskitsproject1.databinding.ItemPostBinding
import com.canbazdev.hmskitsproject1.domain.model.landmark.Post

/*
*   Created by hamzacanbaz on 7/26/2022
*/
class PostsAdapter(
    private val listener: OnItemClickedListener?
) : RecyclerView.Adapter<PostsAdapter.PostsViewHolder>() {

    private var postsList = ArrayList<Post>()

    @SuppressLint("NotifyDataSetChanged")
    fun setPostsList(list: List<Post>) {
        postsList.clear()
        postsList.addAll(list)
        notifyDataSetChanged()
        println("list updated ${list.size}")
    }

    inner class PostsViewHolder(private val binding: ItemPostBinding) :
        BaseViewHolder<Post>(binding.root), View.OnClickListener {

        init {
            itemView.setOnClickListener(this)
        }


        override fun bind(item: Post) {
            binding.post = item
        }

        override fun onClick(p0: View?) {
            val position = layoutPosition
            if (position != RecyclerView.NO_POSITION) {
                postsList[position].let { listener?.onItemClicked(position, it) }
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PostsViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPostBinding.inflate(inflater, parent, false)
        return PostsViewHolder(binding)
    }


    abstract class BaseViewHolder<T>(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun bind(item: Post)
    }

    override fun onBindViewHolder(holder: PostsViewHolder, position: Int) {
        postsList[position].let {
            holder.bind(it)
        }

    }

    interface OnItemClickedListener {
        fun onItemClicked(position: Int, post: Post)
    }


    override fun getItemCount(): Int {
        return postsList.size
    }


}
