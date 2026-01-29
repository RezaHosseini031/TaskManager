package com.rezahosseini.taskmanager.view.menu

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Paint
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.color.MaterialColors
import com.rezahosseini.taskmanager.R
import com.rezahosseini.taskmanager.model.local.DataEntity

class MenuFragmentAdapter(
    val context: Context,
    val listmenu: MutableList<DataEntity>,
    val isDone:Boolean
) : RecyclerView.Adapter<MenuFragmentAdapter.MenuViewHolder>(){
    val lockedMenuIds = mutableSetOf<Long>()
    var onCheckItemClick:((DataEntity)->Unit)?=null
    var onItemClick:((dataEntity:DataEntity)->Unit)?=null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_menu, parent, false)
        return MenuViewHolder(view)
    }

    override fun getItemCount(): Int {
        return listmenu.size

    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        val item = listmenu[position]
        holder.textItemName.text=item.name
        holder.checkItem.isChecked=isDone
        val priorityColorRes = intArrayOf(
            R.color.tm_gray,
            R.color.tm_green,
            R.color.tm_yellow,
            R.color.tm_red
        )
        val priorityColor = ContextCompat.getColor(context, priorityColorRes[item.priority])
        val accentColor = ContextCompat.getColor(context, R.color.colorAccent)

        val states = arrayOf(
            intArrayOf(android.R.attr.state_checked),      // checked
            intArrayOf(-android.R.attr.state_checked)      // unchecked
        )

        val colors = intArrayOf(
            accentColor,    // checked
            priorityColor   // unchecked
        )

        holder.checkItem.buttonTintList = ColorStateList(states, colors)
        if (isDone){
            Log.e("testIsDone","isDone")
            holder.textItemName.setTextColor(Color.parseColor("#9E9E9E"))
            holder.textItemName.paintFlags=
                Paint.STRIKE_THRU_TEXT_FLAG
        }
        Log.e("testDec",""+item.about)
        if (item.about.isNotEmpty()){
            holder.imageDeclaration.visibility=View.VISIBLE
        }
        else{
            holder.imageDeclaration.visibility=View.GONE
        }
        val isLocked = lockedMenuIds.contains(item.id)

        holder.checkItem.isEnabled = !isLocked
        holder.checkItem.alpha = if (isLocked) 0.5f else 1f
        holder.checkItem.setOnClickListener {
            onCheckItemClick?.invoke(item)
        }
        holder.itemView.setOnClickListener {
            onItemClick?.invoke(item)
        }
    }
    class MenuViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkItem: CheckBox = itemView.findViewById(R.id.checkItem)
        val textItemName: TextView =itemView.findViewById(R.id.textItemName)
        val imageDeclaration: ImageView =itemView.findViewById(R.id.imageDescription)
    }
    fun addItem(item: DataEntity) {
        if (containsId(item.id)) return
        listmenu.add(item)
        notifyItemInserted(listmenu.size - 1)
    }
    fun updateList(newList: List<DataEntity>) {
        listmenu.clear()
        listmenu.addAll(newList)
        notifyDataSetChanged()
    }
    fun removeById(id: Long): DataEntity? {
        val index = listmenu.indexOfFirst { it.id == id }
        if (index == -1) return null
        val item = listmenu[index]
        listmenu.removeAt(index)
        notifyItemRemoved(index)
        return item
    }
    fun containsId(id: Long): Boolean {
        return listmenu.any { it.id == id }
    }
    fun getIndexById(id: Long): Int {
        return listmenu.indexOfFirst { it.id == id }
    }
    fun lockItemMenu(id: Long) {
        lockedMenuIds.add(id)
        val index = getIndexById(id)
        if (index != -1) notifyItemChanged(index)
    }

    fun unlockItemMenu(id: Long) {
        lockedMenuIds.remove(id)
        val index = getIndexById(id)
        if (index != -1) notifyItemChanged(index)
    }
    fun isItemMenuLocked(id: Long): Boolean {
        return lockedMenuIds.contains(id)
    }
}