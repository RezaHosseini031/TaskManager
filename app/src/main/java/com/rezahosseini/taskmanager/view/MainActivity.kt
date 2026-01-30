package com.rezahosseini.taskmanager.view

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.rezahosseini.taskmanager.R
import com.rezahosseini.taskmanager.model.local.DataEntity
import com.rezahosseini.taskmanager.view.menu.AddItemBottomSheet
import com.rezahosseini.taskmanager.vm.ViewModelData
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val viewModelDataLocal: ViewModelData by viewModels()
    private lateinit var viewPager: ViewPager2
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var imageMore: ImageButton
    private lateinit var textViewAppBar: TextView
    lateinit var floatAdd: FloatingActionButton
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.viewPager)
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        val appBarLayout = findViewById<AppBarLayout>(R.id.appBarMainActivity)
        floatAdd=findViewById(R.id.floatAdd)
        imageMore=findViewById(R.id.image_button_more)
        textViewAppBar=findViewById(R.id.titleAppBarMainActivity)
        appBarLayout.setExpanded(true, true)
        val adapter = AdapterViewPager(this)
        viewPager.adapter = adapter
        viewPager.offscreenPageLimit = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
        viewPager.isUserInputEnabled = false
        viewPager.offscreenPageLimit = 2
        viewPager.setCurrentItem(0, false)
        bottomNavigationView.selectedItemId = R.id.menu
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu ->{
                    viewPager.currentItem = 0
                }
                R.id.timer ->{
                    viewPager.currentItem = 1
                }
            }
            true
        }
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                appBarLayout.setExpanded(true, true)
                bottomNavigationView.menu.getItem(position).isChecked = true
                textViewAppBar.text = when (position) {
                    0 -> getString(R.string.works)
                    1 -> getString(R.string.timer)
                    else -> getString(R.string.empty)
                }
                floatAdd.visibility=when(position){
                    0 -> View.VISIBLE
                    1 -> View.GONE
                    else -> View.GONE
                }
            }
        })

        imageMore.setOnClickListener {
            PopupMenu(this, it).apply {
                menuInflater.inflate(R.menu.more_main_menu, menu)
                setOnMenuItemClickListener { m ->
                    when (m.itemId) {
                        R.id.aboutUs -> {
                            val dialog = Dialog(this@MainActivity)
                            dialog.setContentView(R.layout.dialog_about_us)
                            dialog.window?.setLayout(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.WRAP_CONTENT
                            )
                            dialog.setCancelable(true)
                            dialog.window?.attributes?.windowAnimations = R.style.animation

                            val okBtn: TextView = dialog.findViewById(R.id.more_text)
                            val cancelBtn: TextView = dialog.findViewById(R.id.ok_text)
                            okBtn.setOnClickListener { dialog.dismiss() }
                            cancelBtn.setOnClickListener {  }
                            dialog.show()
                        }

                        R.id.share -> {
                            Toast.makeText(this@MainActivity, "share", Toast.LENGTH_SHORT).show()
                        }
                    }
                    true
                }
                show()
            }
        }

        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                appBarLayout.setExpanded(true, true)
                bottomNavigationView.menu.getItem(position).isChecked = true
            }
        })
        floatAdd.setOnClickListener {
            val bottomSheet = AddItemBottomSheet() { dataEntity ->
                addItem(dataEntity)
            }
            bottomSheet.show(supportFragmentManager, "addItem")
        }

    }
    private fun addItem(data: DataEntity){
        viewModelDataLocal.insert(data)
    }
}