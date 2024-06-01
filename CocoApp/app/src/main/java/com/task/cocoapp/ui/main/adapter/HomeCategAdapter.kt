package com.task.cocoapp.ui.main.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.druggps.utils.CATEG_BASE_URL
import com.example.druggps.utils.JPEG
import com.task.cocoapp.R
import com.task.cocoapp.databinding.ItemCategBinding
import com.task.cocoapp.ui.main.view.MainActivity

class HomeCategAdapter(var parent: MainActivity) : RecyclerView.Adapter<HomeCategViewHolder>() {

    var categList = mutableListOf<Int>()

    lateinit var context: Context
    private var mCheckedPostion = -1
    fun setCategs(tags: MutableList<Int>) {
        this.categList = tags.toMutableList()
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeCategViewHolder {


        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCategBinding.inflate(inflater, parent, false)
        context = parent.context
        return HomeCategViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HomeCategViewHolder, @SuppressLint("RecyclerView") position: Int) {

        val categ = categList[position]

        Glide.with(context)
            .load(CATEG_BASE_URL + categ + JPEG)
            .into(holder.binding.categIV)



        holder.binding.categIV.setOnClickListener(View.OnClickListener {

            if (holder.binding.selectedV.visibility==View.GONE) {
                holder.binding.selectedV.setVisibility(View.VISIBLE)
                this.parent.addCategTag(categ,true,holder)
            } else {
                holder.binding.selectedV.setVisibility(View.GONE)
                this.parent.addCategTag(categ,false,holder)
            }



        })

    }

    override fun getItemCount(): Int {
        return categList.size
    }
}

class HomeCategViewHolder(val binding: ItemCategBinding) : RecyclerView.ViewHolder(binding.root) {

}