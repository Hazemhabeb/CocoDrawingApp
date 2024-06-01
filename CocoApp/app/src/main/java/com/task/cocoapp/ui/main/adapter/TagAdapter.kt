package com.task.cocoapp.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.task.cocoapp.databinding.ItemTagBinding
import com.task.cocoapp.ui.main.view.MainActivity

class TagAdapter(private var mActivity: MainActivity) : RecyclerView.Adapter<MainViewHolder>() {

    var tagList = mutableListOf<String>()

    fun setTags(tags: List<String>) {
        this.tagList = tags.toMutableList()
        notifyDataSetChanged()
    }
    fun addTags(tag: String) {

        if (!tagList.contains(tag)){
            tagList.add(tag)
        }
        notifyDataSetChanged()
    }
    fun removeTag(tag: String){
        tagList.remove(tag)
        mActivity.callData(tagList)
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemTagBinding.inflate(inflater, parent, false)
        return MainViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {

        val tag = tagList[position]
        holder.binding.tagNameTV.text = tag
        holder.binding.removeIV.setOnClickListener(View.OnClickListener {
            removeTag(tag)
        })

    }

    override fun getItemCount(): Int {
        return tagList.size
    }
}

class MainViewHolder(val binding:ItemTagBinding) : RecyclerView.ViewHolder(binding.root) {

}