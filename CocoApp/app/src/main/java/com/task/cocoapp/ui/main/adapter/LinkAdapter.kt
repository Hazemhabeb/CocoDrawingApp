package com.task.cocoapp.ui.main.adapter

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat.startActivity
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import com.task.cocoapp.databinding.ItemLinkBinding


class LinkAdapter(var holder: ItemViewHolder,var parent: ItemAdapter) : RecyclerView.Adapter<LinkViewHolder>() {

    var linkList = mutableListOf<String>()

    lateinit var context: Context
    fun setLinks(tags: ArrayList<String>) {
        this.linkList = tags.toMutableList()

        notifyDataSetChanged()
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LinkViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLinkBinding.inflate(inflater, parent, false)
        context = parent.context
        return LinkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: LinkViewHolder, position: Int) {

        val link = linkList[position]

        holder.binding.linkTV.text =  Html.fromHtml("<u>${link}</u> "
            , HtmlCompat.FROM_HTML_MODE_LEGACY)

        holder.binding.linkTV.setOnClickListener(View.OnClickListener {
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(link))
            context.startActivity(browserIntent)
        })
    }

    override fun getItemCount(): Int {
        return linkList.size
    }
}

class LinkViewHolder(val binding:ItemLinkBinding) : RecyclerView.ViewHolder(binding.root) {

}