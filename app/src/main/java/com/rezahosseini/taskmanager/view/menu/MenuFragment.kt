package com.rezahosseini.taskmanager.view.menu

import android.accessibilityservice.AccessibilityService.SoftKeyboardController
import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.AutoCompleteTextView
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.rezahosseini.taskmanager.R
import com.rezahosseini.taskmanager.model.local.DataEntity
import com.rezahosseini.taskmanager.vm.ViewModelData
import com.rezahosseini.taskmanager.vm.state.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import android.widget.PopupMenu
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.repeatOnLifecycle

@AndroidEntryPoint
class MenuFragment : Fragment() {
    private val viewModelDataLocal: ViewModelData by viewModels()
    lateinit var imageClearWork:ImageView
    lateinit var imageSort:ImageView
    lateinit var autoCompleteTextViewMenu: AutoCompleteTextView
    lateinit var rvUnDone: RecyclerView
    lateinit var rvDone:RecyclerView
    lateinit var textRvUnDone: TextView
    lateinit var textRvDone:TextView
    lateinit var clearSearch: ImageView
    lateinit var iconRVUnDone:ImageView
    lateinit var iconRvDone:ImageView
    lateinit var dividerMenu:View
    lateinit var imageNoItem:ImageView
    lateinit var textNoItem: TextView
    lateinit var progressBar: ProgressBar
    lateinit var menuFragmentAdapterUnDone: MenuFragmentAdapter
    lateinit var menuFragmentAdapterDone:MenuFragmentAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        imageClearWork=view.findViewById(R.id.imageClearWork)
        imageSort=view.findViewById(R.id.imageSort)
        rvUnDone=view.findViewById(R.id.rvWork)
        rvDone=view.findViewById(R.id.rvFragmentWorkDone)
        textRvUnDone=view.findViewById(R.id.tvRvWork)
        textRvDone=view.findViewById(R.id.tvRvWorkDone)
        clearSearch=view.findViewById(R.id.clearSearch)
        iconRVUnDone=view.findViewById(R.id.iconRvWork)
        iconRvDone=view.findViewById(R.id.iconRvWorkDone)
        dividerMenu=view.findViewById(R.id.dividerMenu)
        autoCompleteTextViewMenu=view.findViewById(R.id.autoCompleteTextViewMenu)
        imageNoItem=view.findViewById(R.id.imageNoData)
        textNoItem=view.findViewById(R.id.textNoData)
        progressBar=view.findViewById(R.id.loading_progress_bar_menu)
        rvDone.visibility = View.VISIBLE
        observeDataFlow()
        createMenuToolbar()
    }
    @SuppressLint("UseRequireInsteadOfGet")
    private fun observeDataFlow() {
        lifecycleScope.launch {
            viewModelDataLocal.dataStateFlow.collect { state ->
                when (state) {
                    is UiState.Loading -> showLoading()
                    is UiState.Success -> {
                        val listUnDone = state.data.filter { it.done == 0 }
                        val listDone = state.data.filter { it.done == 1 }
                        Log.e("testIsDone","listDone"+listDone)

                        if(::menuFragmentAdapterUnDone.isInitialized)
                            menuFragmentAdapterUnDone.updateList(listUnDone.toMutableList())
                        else
                            menuFragmentAdapterUnDone = MenuFragmentAdapter(this@MenuFragment.context!!,listUnDone.toMutableList(), false).also {
                                rvUnDone.adapter = it
                                rvUnDone.layoutManager = LinearLayoutManager(requireContext())
                                setupAdapterClick(it)
                            }

                        if(::menuFragmentAdapterDone.isInitialized)
                            menuFragmentAdapterDone.updateList(listDone.toMutableList())
                        else
                            menuFragmentAdapterDone = MenuFragmentAdapter(this@MenuFragment.context!!,listDone.toMutableList(), true).also {
                                rvDone.adapter = it
                                rvDone.layoutManager = LinearLayoutManager(requireContext())
                                setupAdapterClick(it)
                            }

                        checkIsEmptyList()
                    }
                    is UiState.Error -> {
                        Toast.makeText(requireContext(), state.message, Toast.LENGTH_SHORT).show()
                        showNoItem()
                    }
                }
            }
        }
    }


    private fun setupAdapterClick(adapter: MenuFragmentAdapter) {
        adapter.onItemClick = { data ->
            val bottomSheet = EditItemBottomSheet(
                item = data,
                onApply = { updatedItem -> changeItem(updatedItem) },
                onDelete = { deletedItem -> deleteItem(deletedItem) }
            )
            bottomSheet.show(childFragmentManager, "editItem")
        }
        adapter.onCheckItemClick = onCheckItemClick@{ data ->
            if (adapter.isItemMenuLocked(data.id)) return@onCheckItemClick
            adapter.lockItemMenu(data.id)
            Log.e("testChangeItem","data="+data)
            data.done = 1 - data.done
            changeItem(data)
        }
    }

    private fun createMenuToolbar(){
        imageClearWork.setOnClickListener { view ->
            val popup = PopupMenu(requireContext(), view)
            popup.menuInflater.inflate(R.menu.menu_delete, popup.menu)

            // Force show icons
            try {
                val fields = popup.javaClass.declaredFields
                for (field in fields) {
                    if ("mPopup" == field.name) {
                        field.isAccessible = true
                        val menuPopupHelper = field.get(popup)
                        val classPopupHelper = Class.forName(menuPopupHelper.javaClass.name)
                        val setForceIcons = classPopupHelper.getMethod("setForceShowIcon", Boolean::class.javaPrimitiveType)
                        setForceIcons.invoke(menuPopupHelper, true)
                        break
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_delete_all -> {
                        deleteAll()
                        true
                    }
                    R.id.action_delete_selected -> {
                        deleteDone()
                        true
                    }
                    else -> false
                }
            }
            popup.show()
        }
        autoCompleteTextViewMenu.doOnTextChanged { text, _, _, _ ->
            viewModelDataLocal.setQuery(text.toString())
        }

        imageSort.setOnClickListener {
            PopupMenu(requireContext(), it).apply {
                menuInflater.inflate(R.menu.menu_sort, menu)
                setOnMenuItemClickListener { item ->
                    when(item.itemId) {
                        R.id.sortNameAsc -> viewModelDataLocal.setSort("AZ")
                        R.id.sortNameDesc -> viewModelDataLocal.setSort("ZA")
                        R.id.sortPriorityHigh -> viewModelDataLocal.setSort("PRIO_HIGH")
                        R.id.sortPriorityLow -> viewModelDataLocal.setSort("PRIO_LOW")
                        R.id.sortDateNew -> viewModelDataLocal.setSort("DATE_NEW")
                        R.id.sortDateOld -> viewModelDataLocal.setSort("DATE_OLD")
                    }
                    true
                }
                show()
            }
        }

    }

    private fun changeItem(data: DataEntity) {

        viewModelDataLocal.update(data)?.let {

            if (data.done == 1) {

                menuFragmentAdapterUnDone.unlockItemMenu(data.id)
                menuFragmentAdapterUnDone.lockedMenuIds.remove(data.id)

                val item = menuFragmentAdapterUnDone.removeById(data.id)

                item?.let { moved ->
                    menuFragmentAdapterDone.addItem(moved)
                }

            } else {

                menuFragmentAdapterDone.unlockItemMenu(data.id)
                menuFragmentAdapterDone.lockedMenuIds.remove(data.id)

                val item = menuFragmentAdapterDone.removeById(data.id)
                item?.let { moved ->
                    menuFragmentAdapterUnDone.addItem(moved)
                }
            }
            menuFragmentAdapterDone.notifyDataSetChanged()
            menuFragmentAdapterUnDone.notifyDataSetChanged()
        }
    }


    private fun deleteItem(data: DataEntity){
        viewModelDataLocal.delete(data)?.let {
            if (data.done==0){
                menuFragmentAdapterUnDone.removeById(data.id)
            }else{
                menuFragmentAdapterDone.removeById(data.id)
            }
        }
    }
    private fun checkIsEmptyList() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModelDataLocal.allDataState.collect { state ->
                    when(state) {
                        is UiState.Loading -> Log.e("@logViewModel","loadingAll")
                        is UiState.Success ->{
                            if (state.data.isEmpty()){
                                showNoItem()
                            }else{
                                showSuccess()
                            }
                    }
                        is UiState.Error -> Log.e("@logViewModel","errorAll=${state.message}")
                    }
                }
            }
        }
    }

    private fun deleteAll() {
        lifecycleScope.launch {
            viewModelDataLocal.deleteAll() // DAO
        }
    }

    private fun deleteDone() {
        lifecycleScope.launch {
            viewModelDataLocal.deleteByDone(1)
        }
    }

    private fun showNoItem(){
        rvUnDone.visibility=View.GONE
        rvDone.visibility=View.GONE
        textRvUnDone.visibility=View.GONE
        textRvDone.visibility=View.GONE
        iconRVUnDone.visibility=View.GONE
        iconRvDone.visibility=View.GONE
        dividerMenu.visibility=View.GONE
        imageNoItem.visibility=View.VISIBLE
        textNoItem.visibility=View.VISIBLE
        progressBar.visibility=View.GONE
    }
    private fun showLoading(){
        rvUnDone.visibility=View.GONE
        rvDone.visibility=View.GONE
        textRvUnDone.visibility=View.GONE
        textRvDone.visibility=View.GONE
        iconRVUnDone.visibility=View.GONE
        iconRvDone.visibility=View.GONE
        dividerMenu.visibility=View.GONE
        imageNoItem.visibility=View.GONE
        textNoItem.visibility=View.GONE
        progressBar.visibility=View.VISIBLE
    }
    private fun showSuccess(){
        rvUnDone.visibility=View.VISIBLE
        rvDone.visibility=View.VISIBLE
        textRvUnDone.visibility=View.VISIBLE
        textRvDone.visibility=View.VISIBLE
        iconRVUnDone.visibility=View.VISIBLE
        iconRvDone.visibility=View.VISIBLE
        dividerMenu.visibility=View.VISIBLE
        imageNoItem.visibility=View.GONE
        textNoItem.visibility=View.GONE
        progressBar.visibility=View.GONE
    }
}