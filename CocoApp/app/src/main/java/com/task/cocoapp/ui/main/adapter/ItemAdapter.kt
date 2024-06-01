package com.task.cocoapp.ui.main.adapter

import android.content.Context
import android.graphics.*
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import androidx.core.text.HtmlCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.task.cocoapp.R
import com.task.cocoapp.data.model.CaptionResponse
import com.task.cocoapp.data.model.ImageResponse
import com.task.cocoapp.data.model.SegmentResponse
import com.task.cocoapp.databinding.ItemListBinding
import com.task.cocoapp.ui.main.view.MainActivity
import com.task.cocoapp.ui.main.viewmodel.SegmentViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ItemAdapter() : RecyclerView.Adapter<ItemViewHolder>() {

    var imagesList = mutableListOf<ImageResponse>()
    var segmentList = mutableListOf<SegmentResponse>()
    var captionList = mutableListOf<CaptionResponse>()
    lateinit var context: Context


    fun addImages(images: List<ImageResponse>) {
        this.imagesList.addAll(images.toMutableList())
        notifyDataSetChanged()
    }

    fun addSegment(images: List<SegmentResponse>) {
        this.segmentList.addAll(images.toMutableList())
    }

    fun addCaption(images: List<CaptionResponse>) {
        this.captionList.addAll(images.toMutableList())
    }

    fun removeImages() {
        this.imagesList.clear()
        this.segmentList.clear()
        this.captionList.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {

        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemListBinding.inflate(inflater, parent, false)
        context = parent.context
        return ItemViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val images = imagesList[position]

        val segments = getSegment(images.id)
        // draw the segment on the image
        holder.binding.img.setImageBitmap(null)
        Glide.with(context)
            .asBitmap()
            .load(images.coco_url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    holder.binding.img.setImageBitmap(resource)
                    CoroutineScope(Dispatchers.Main).launch {
                        drawSegment(segments, holder, resource)
                    }


                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })

        // adding the categories recyclerview

        val categAdapter = CategAdapter(holder,this,position)
        val layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        holder.binding.CategRV.adapter = categAdapter
        holder.binding.CategRV.layoutManager = layoutManager
        // get the categories from the segment
        categAdapter.setCategs(getCateg(segments))


        // add the links
        val linkAdapter = LinkAdapter(holder,this)
        val layoutManagerLink = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        holder.binding.linksRV.adapter = linkAdapter
        holder.binding.linksRV.layoutManager = layoutManagerLink
        val linkArray =  ArrayList<String>()
        linkArray.add(images.flickr_url)
        linkArray.add(images.coco_url)
        linkAdapter.setLinks(linkArray)


        // add the caption
        var captionText = ""
        for (item in getCaption(images.id)){
            captionText += item+"\n"
        }
        holder.binding.captionTV.text = captionText


    }
    //action the caption item
    fun hideShowCaption(holder: ItemViewHolder){

        if (holder.binding.captionTV.visibility == View.VISIBLE){
            holder.binding.captionTV.setVisibility(View.GONE)
        }else{
            holder.binding.captionTV.setVisibility(View.VISIBLE)
        }
    }
    //action the link item
    fun hideShowLink(holder: ItemViewHolder){
        if (holder.binding.linksRV.visibility == View.VISIBLE){
            holder.binding.linksRV.setVisibility(View.GONE)
        }else{
            holder.binding.linksRV.setVisibility(View.VISIBLE)
        }
    }
    //action the show categ segment item
    fun showCategSegment(holder: ItemViewHolder,categId : Int,position: Int,selected :Boolean ){
        // here to get the segment by category
        val images = imagesList[position]
        holder.binding.img.setImageBitmap(null)
        Glide.with(context)
            .asBitmap()
            .load(images.coco_url)
            .into(object : CustomTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    holder.binding.img.setImageBitmap(resource)
                    CoroutineScope(Dispatchers.Main).launch {
                        if (selected){
                            drawSegment(getSegmentCateg(images.id,categId),holder,resource)
                        }else{
                            drawSegment(getSegment(images.id), holder, resource)
                        }

                    }


                }

                override fun onLoadCleared(placeholder: Drawable?) {
                }
            })

    }
    //--------------------------------------------   get categories from segment of image  ---------------------------------------

    private fun getCaption(imageId: Int): ArrayList<String> {

        val captionArray = ArrayList<String>()
        for (item in captionList) {
            if (item.image_id == imageId) {
                captionArray.add(item.caption)
            }
        }
        return captionArray
    }


    //--------------------------------------------   get categories from segment of image  ---------------------------------------
    private fun getCateg(segments: ArrayList<SegmentResponse>): ArrayList<Int> {

        val categArray = ArrayList<Int>()

        val categArrayFilter = ArrayList<Int>()
        for (item in segments){
            categArray.add(item.category_id)
        }

        for (item in categArray){
            if (!categArrayFilter.contains(item)){
                categArrayFilter.add(item)
            }
        }
//        Log.d("MainActivity","categories "+categArray.toString())
//        Log.d("MainActivity","categories filter   "+categArrayFilter.toString())

        return categArrayFilter
    }

    //--------------------------------------------   get segment of image  ---------------------------------------
    private fun getSegment(imageId: Int): ArrayList<SegmentResponse> {

        val segmentArray = ArrayList<SegmentResponse>()
        for (item in segmentList) {
            if (item.image_id == imageId) {
                segmentArray.add(item)
            }
        }
        return segmentArray
    }
    //--------------------------------------------   get segment by categ of image  ---------------------------------------
    private fun getSegmentCateg(imageId: Int,categId: Int): ArrayList<SegmentResponse> {

        val segmentArray = ArrayList<SegmentResponse>()
        val segmentArrayCateg = ArrayList<SegmentResponse>()
        for (item in segmentList) {
            if (item.image_id == imageId) {
                segmentArray.add(item)
            }
        }

        for (item in segmentArray){
            if (item.category_id == categId){
                segmentArrayCateg.add(item)
            }
        }
        return segmentArrayCateg
    }

    //--------------------------------------------   draw segment on image  ---------------------------------------
    private fun drawSegment(
        segment: ArrayList<SegmentResponse>, holder: ItemViewHolder, imageBitmap: Bitmap
    ) {
        val layer: Array<Drawable?>
        layer = Array(2) { null }
        layer[0] = imageBitmap.toDrawable(context.resources)
        layer[1] = imageBitmap.toDrawable(context.resources)


        var ldr = LayerDrawable(layer)

        holder.binding.img.setImageDrawable(ldr)

        val mark: Bitmap = Bitmap.createBitmap(
            imageBitmap.width,
            imageBitmap.height,
            Bitmap.Config.ARGB_8888
        )
        val ca = Canvas(mark)


        // to get the segment
        for (shape in segment) {

            val pa = Paint()
            pa.style = Paint.Style.FILL_AND_STROKE
            pa.isAntiAlias = true
            pa.setStrokeCap(Paint.Cap.ROUND);
            pa.setStrokeCap(Paint.Cap.ROUND);
            pa.strokeWidth = 10f
            pa.setARGB(200, (0..255).random(), (0..255).random(), (0..255).random())
//            pa.setColor(Color.argb(7,(0..255).random(), (0..255).random(), (0..255).random()));
            val segmentArray = ((shape.segmentation.replace("[", ""))
                .replace("]", "")).split(",")

            val numbers = ArrayList<Float>()
            for (number in segmentArray) {
                try {
                    numbers.add(number.toFloat())
                } catch (num: NumberFormatException) {
                    Log.d("MainActivity", "error  convert to float " + number)
                }
            }

            val linePath = Path()

            linePath.moveTo(numbers.get(0), numbers.get(1))

            for (i in 0..numbers.size - 3 step 2) {
//                Log.d("MainActivity","point "+segmentArray[i + 2]+"    point "+segmentArray[i + 2])
                linePath.lineTo(numbers.get(i + 2), numbers.get(i + 3))
            }
            linePath.lineTo(
                numbers.get(numbers.size - 2), numbers.get(numbers.size - 1)
            )
            linePath.lineTo(
                numbers.get(0), numbers.get(1)
            )
            linePath.close()
            ca.drawPath(linePath, pa)
        }


        val bmp = BitmapDrawable(context.resources, mark)
        layer[1] = bmp
        ldr = LayerDrawable(layer)
        holder.binding.img.setImageDrawable(ldr)


    }

    override fun getItemCount(): Int {
        return imagesList.size
    }

}

class ItemViewHolder(val binding: ItemListBinding) : RecyclerView.ViewHolder(binding.root) {

}