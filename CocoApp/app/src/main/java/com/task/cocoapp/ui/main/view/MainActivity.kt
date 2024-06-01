package com.task.cocoapp.ui.main.view

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.druggps.ui.main.view.baseActivity.BaseActivity
import com.example.druggps.utils.Categories
import com.example.druggps.utils.CategoriesIds
import com.task.cocoapp.data.api.RetrofitService
import com.task.cocoapp.data.repository.CaptionRepository
import com.task.cocoapp.data.repository.DataRepository
import com.task.cocoapp.data.repository.ImageRepository
import com.task.cocoapp.data.repository.SegmentRepository
import com.task.cocoapp.databinding.ActivityMainBinding
import com.task.cocoapp.ui.base.CaptionViewModelFactory
import com.task.cocoapp.ui.base.DataViewModelFactory
import com.task.cocoapp.ui.base.ImageViewModelFactory
import com.task.cocoapp.ui.base.SegmentViewModelFactory
import com.task.cocoapp.ui.main.adapter.*
import com.task.cocoapp.ui.main.viewmodel.CaptionViewModel
import com.task.cocoapp.ui.main.viewmodel.DataViewModel
import com.task.cocoapp.ui.main.viewmodel.ImageViewModel
import com.task.cocoapp.ui.main.viewmodel.SegmentViewModel
import com.task.cocoapp.utils.views.PaginationScrollListener
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.loading_progress.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : BaseActivity() {


    lateinit var dataViewModel: DataViewModel
    lateinit var imageViewModel: ImageViewModel
    lateinit var segmentViewModel: SegmentViewModel
    lateinit var captionViewModel: CaptionViewModel
    lateinit var binding: ActivityMainBinding
    lateinit var itemAdapter: ItemAdapter
    lateinit var tagAdapter: TagAdapter
    lateinit var images: ArrayList<String>
    lateinit var imageData: ArrayList<String>

    //pagination
//    private val pageStart: Int = 1
    private var isLoading: Boolean = false
    private var isCleared: Boolean = false
    private var isLastPage: Boolean = false
    private var totalPages: Int = 1
    private var currentPage: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        initViewModel()
        subscribeData()

        images = java.util.ArrayList()


    }

    fun callData(categName: List<String>) {
        currentPage = 0
        if (categName.size == 0) {
            isCleared = true
            images.clear()
            itemAdapter.removeImages()
            return
        }
        dataViewModel.getData(
            "getImagesByCats", findIdByName(categName)
        )
    }

    fun findIdByName(categNames: List<String>): ArrayList<String> {

        val ids: ArrayList<String> = java.util.ArrayList()
        for (item in categNames)
            for (i in 0..Categories.size - 1) {
                if (item.equals(Categories[i])) {
                    ids.add((CategoriesIds[i]).toString())
                    Log.d(
                        "MainActivity", "this is the data  " +
                                item + "    second   " + Categories[i] + "   index   " + (CategoriesIds[i])
                    )
                }
            }

        return ids
    }

    fun findNameById(categId: Int): String {

        var name = ""
        for (i in 0..CategoriesIds.size-1) {
            if (CategoriesIds[i].equals(categId)) {
                name = Categories[i]
            }
        }

        return name
    }

    //-------------------------------------------- initView ---------------------------------------
    private fun initView() {
        CoroutineScope(Dispatchers.IO).launch {
        }

//--------------------------------------------  make the categoties  ---------------------------------------
        val categAdapter = HomeCategAdapter(this)
        val layoutManagerCateg =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        CategRV.adapter = categAdapter
        CategRV.layoutManager = layoutManagerCateg
        categAdapter.setCategs(CategoriesIds.toMutableList())


        //--------------------------------------------  make the searchview  ---------------------------------------
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(this, android.R.layout.select_dialog_item, Categories)
        //Getting the instance of AutoCompleteTextView
        //Getting the instance of AutoCompleteTextView
        autoCompleteTextView.threshold = 1 //will start working from first character
        autoCompleteTextView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        autoCompleteTextView.setAdapter(adapter) //setting the adapter data into the AutoCompleteTextView


        autoCompleteTextView.setOnItemClickListener(OnItemClickListener { parent, arg1, position, arg3 ->
            val item = parent.getItemAtPosition(position)
//            Log.d("MainActivity", "this is the item selected   " + item.toString()+"   position  " +position)
            tagAdapter.addTags(item.toString())
            autoCompleteTextView.setText("")
            hideKeyboard(mainView)


            callData(tagAdapter.tagList.toList())
        })

        //--------------------------------------------  add the tags   ---------------------------------------
        tagAdapter = TagAdapter(this@MainActivity)
        val layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        tagsRV.adapter = tagAdapter
        tagsRV.layoutManager = layoutManager


        //--------------------------------------------  add the items   ---------------------------------------
        itemAdapter = ItemAdapter()


        val layoutManagerItem = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        itemsRV.adapter = itemAdapter
        itemsRV.layoutManager = layoutManagerItem

        itemsRV.addOnScrollListener(object :
            PaginationScrollListener(itemsRV.layoutManager as LinearLayoutManager) {
            override fun loadMoreItems() {
                isLoading = true
                currentPage += 1

                loading.visibility = View.VISIBLE
                Handler(Looper.myLooper()!!).postDelayed({
                    loadDataImages(currentPage)
//                    Log.d("MainActivity", "currentPage   " + currentPage)
                }, 1000)
            }

            override fun getTotalPageCount(): Int {
                return totalPages
            }

            override fun isLastPage(): Boolean {
                return isLastPage
            }

            override fun isLoading(): Boolean {
                return isLoading
            }

        })

    }


    //-------------------------------------------- subscribe get data ---------------------------------------

    fun addCategTag(categId: Int, selected: Boolean,holder: HomeCategViewHolder) {
        val categName = findNameById(categId)

        if (selected){
            tagAdapter.addTags(categName)
        }else{
            tagAdapter.removeTag(categName)
        }
        callData(tagAdapter.tagList.toList())

    }
    //-------------------------------------------- subscribe get data ---------------------------------------

    private fun loadDataImages(currentPage: Int) {
        Log.d("MainActivity", "currentPage   " + currentPage)
        imageData = ArrayList()

        if (currentPage < totalPages) {
            for (i in 0..4) {
                imageData.add(images.get(i + (currentPage * 4)))
            }
//            imageViewModel.getData("getImages",imageData)
            segmentViewModel.getData("getInstances", imageData)
//            captionViewModel.getData("getCaptions",imageData)
            isLoading = false
        } else {
            isLoading = false
            isLastPage = true
        }


    }

    private fun subscribeData() {

        //data
        dataViewModel.data.observe(this) {
            CoroutineScope(Dispatchers.IO).launch {
                Log.d("MainActivity", "this is the data   " + it)

                images.clear()
                withContext(Dispatchers.Main) {
                    itemAdapter.removeImages()
                    currentPage = 0
                }

                for (item in it) {
                    images.add(item.toString())
                }

                totalPages = images.size / 4
                Log.d("MainActivity", "totalPages   " + totalPages)
                loadDataImages(0)

                if(it.size==0){
                    isCleared = true
                    emptyTV.visibility = View.VISIBLE
                }else{
                    isCleared = false
                    emptyTV.visibility = View.GONE
                }

            }
        }
        dataViewModel.errorMessage.observe(this) {
            if (!it.isNullOrEmpty()) {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }

        }
        dataViewModel.loading.observe(this, Observer {
            if (it) {
                progressDialogLoginScreen.clLoadingProgress.visibility = View.VISIBLE
            } else {
                progressDialogLoginScreen.clLoadingProgress.visibility = View.GONE
            }
        })

        //images
        imageViewModel.data.observe(this) {
            CoroutineScope(Dispatchers.IO).launch {
                Log.d("MainActivity", "this is the images    " + it)
                if (!isCleared) {
                    withContext(Dispatchers.Main) {
                        itemAdapter.addImages(it.toMutableList())
                        loading.visibility = View.GONE
                    }


                    val images: ArrayList<String> = java.util.ArrayList()
                    for (item in it) {
                        images.add(item.id.toString())
                    }
                }

            }
        }
        imageViewModel.errorMessage.observe(this) {
            if (!it.isNullOrEmpty()) {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }

        }
        imageViewModel.loading.observe(this, Observer {
            if (it) {
                progressDialogLoginScreen.clLoadingProgress.visibility = View.VISIBLE
            } else {
                progressDialogLoginScreen.clLoadingProgress.visibility = View.GONE
            }
        })

        //segment
        segmentViewModel.data.observe(this) {
            CoroutineScope(Dispatchers.IO).launch {
                Log.d("MainActivity", "this is the segments    " + it)

                if (!isCleared){
                    withContext(Dispatchers.Main) {
                        itemAdapter.addSegment(it.toMutableList())
                    }

                    captionViewModel.getData("getCaptions", imageData)

                }

            }
        }
        segmentViewModel.errorMessage.observe(this) {
            if (!it.isNullOrEmpty()) {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }

        }
        segmentViewModel.loading.observe(this, Observer {
            if (it) {
                progressDialogLoginScreen.clLoadingProgress.visibility = View.VISIBLE
            } else {
                progressDialogLoginScreen.clLoadingProgress.visibility = View.GONE
            }
        })

        //caption
        captionViewModel.data.observe(this) {
            CoroutineScope(Dispatchers.IO).launch {
                Log.d("MainActivity", "this is the caption    " + it)

                if (!isCleared) {
                    withContext(Dispatchers.Main) {
                        itemAdapter.addCaption(it.toMutableList())
                    }
                    imageViewModel.getData("getImages", imageData)
                }
            }
        }
        captionViewModel.errorMessage.observe(this) {
            if (!it.isNullOrEmpty()) {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }

        }
        captionViewModel.loading.observe(this, Observer {
            if (it) {
                progressDialogLoginScreen.clLoadingProgress.visibility = View.VISIBLE
            } else {
                progressDialogLoginScreen.clLoadingProgress.visibility = View.GONE
            }
        })
    }


    //-------------------------------------------- init View Model -------------------------------------
    private fun initViewModel() {
        val retrofitService = RetrofitService.getInstance()
        //data
        val dataRepository = DataRepository(
            retrofitService,
        )
        dataViewModel = ViewModelProvider(
            this,
            DataViewModelFactory(dataRepository, application)
        ).get(DataViewModel::class.java)

        //images
        val imageRepository = ImageRepository(
            retrofitService,
        )
        imageViewModel = ViewModelProvider(
            this,
            ImageViewModelFactory(imageRepository, application)
        ).get(ImageViewModel::class.java)


        //segment
        val segmentRepository = SegmentRepository(
            retrofitService,
        )
        segmentViewModel = ViewModelProvider(
            this,
            SegmentViewModelFactory(segmentRepository, application)
        ).get(SegmentViewModel::class.java)

        //caption
        val captionRepository = CaptionRepository(
            retrofitService,
        )
        captionViewModel = ViewModelProvider(
            this,
            CaptionViewModelFactory(captionRepository, application)
        ).get(CaptionViewModel::class.java)

    }


}