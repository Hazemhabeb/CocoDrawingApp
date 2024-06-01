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

class CategAdapter(var holder: ItemViewHolder,var parent: ItemAdapter,var position: Int) : RecyclerView.Adapter<CategViewHolder>() {

    var categList = mutableListOf<Int>()
    var urlItem = 200
    var sentenceItem = 300
    var blankItem = 400
    lateinit var context: Context
    private var mCheckedPostion = -1
    fun setCategs(tags: ArrayList<Int>) {
        this.categList = tags.toMutableList()

        this.categList.add(0,urlItem)
        this.categList.add(1,sentenceItem)
        this.categList.add(blankItem)
        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategViewHolder {


        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCategBinding.inflate(inflater, parent, false)
        context = parent.context
        return CategViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategViewHolder, @SuppressLint("RecyclerView") position: Int) {

        val categ = categList[position]

        if (categ == urlItem){
            holder.binding.categIV.setImageDrawable(context.getDrawable(R.drawable.url_icon))
        }else  if (categ == sentenceItem){
            holder.binding.categIV.setImageDrawable(context.getDrawable(R.drawable.sentences))
        }else if (categ == blankItem){
            holder.binding.categIV.setImageDrawable(context.getDrawable(R.drawable.blank))
        }else{
            Glide.with(context)
                .load(CATEG_BASE_URL+categ+ JPEG)
                .into(holder.binding.categIV)
        }

        if (mCheckedPostion == position){
            holder.binding.selectedV.visibility = View.VISIBLE
        }else{
            holder.binding.selectedV.visibility = View.GONE
        }
        holder.binding.categIV.setOnClickListener(View.OnClickListener {
            if (categ == sentenceItem){
                this.parent.hideShowCaption(this.holder)

            }else if (categ == urlItem){
                this.parent.hideShowLink(this.holder)
            }else{


                if (mCheckedPostion != position){
                    mCheckedPostion = position
                    this.parent.showCategSegment(this.holder,categ,this.position,true)
                }else{
                    mCheckedPostion = -1
                    this.parent.showCategSegment(this.holder,categ,this.position,false)
                }
                notifyDataSetChanged()
            }

        })

    }

    override fun getItemCount(): Int {
        return categList.size
    }
}

class CategViewHolder(val binding:ItemCategBinding) : RecyclerView.ViewHolder(binding.root) {

}