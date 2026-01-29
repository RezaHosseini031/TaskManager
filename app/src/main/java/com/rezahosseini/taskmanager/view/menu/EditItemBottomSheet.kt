package com.rezahosseini.taskmanager.view.menu

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.rezahosseini.taskmanager.R
import com.rezahosseini.taskmanager.model.local.DataEntity

class EditItemBottomSheet(
    private val item: DataEntity,
    private val onApply: (DataEntity) -> Unit,
    private val onDelete: (DataEntity) -> Unit
) : BottomSheetDialogFragment() {

    private var priorityValue: Int = 0
    private val priorityColor = intArrayOf(Color.GRAY,Color.GREEN, Color.YELLOW, Color.RED )
    private lateinit var priority: ImageView
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : BottomSheetDialog(requireContext(), theme) {
            @SuppressLint("MissingSuperCall")
            override fun onBackPressed() {
                dismiss()
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v = inflater.inflate(R.layout.bottomsheet_edit_item, container, false)

        val name = v.findViewById<TextInputEditText>(R.id.editTextName)
        val about = v.findViewById<TextInputEditText>(R.id.editTextAbout)
        val save = v.findViewById<MaterialButton>(R.id.save)
        val more = v.findViewById<ImageView>(R.id.imageMore)
        priority = v.findViewById<ImageView>(R.id.priority)
        val check = v.findViewById<CheckBox>(R.id.checkBox)

        // init values
        name.setText(item.name)
        about.setText(item.about)
        check.isChecked = item.done == 1
        priorityValue = item.priority
        priority.imageTintList = ColorStateList.valueOf(priorityColor[priorityValue])

        // show keyboard & focus
        name.post {
            name.requestFocus()
            val imm = requireContext().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(name, InputMethodManager.SHOW_IMPLICIT)
        }

        // more menu
        more.setOnClickListener {
            PopupMenu(requireContext(), it).apply {
                menuInflater.inflate(R.menu.more_menu, menu)
                setOnMenuItemClickListener { m ->
                    when (m.itemId) {
                        R.id.delete -> {
                            onDelete(item)
                            dismiss()
                            this@EditItemBottomSheet.dismiss()
                        }
                        R.id.share -> Toast.makeText(context, "share", Toast.LENGTH_SHORT).show()
                    }
                    true
                }
                show()
            }
        }

        priority.setOnClickListener { anchor ->
            val popup = PopupMenu(requireContext(), anchor)
            popup.menuInflater.inflate(R.menu.more_menu_priority, popup.menu)

            // Force show icons
            try {
                val fields = popup.javaClass.declaredFields
                for (field in fields) {
                    if ("mPopup" == field.name) {
                        field.isAccessible = true
                        val menuPopupHelper = field.get(popup)
                        val classPopupHelper = Class.forName(menuPopupHelper.javaClass.name)
                        val setForceIcons = classPopupHelper.getMethod(
                            "setForceShowIcon",
                            Boolean::class.javaPrimitiveType
                        )
                        setForceIcons.invoke(menuPopupHelper, true)
                        break
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            // تغییر رنگ آیکون‌ها
            popup.menu.findItem(R.id.highPriority)?.icon?.setTint(resources.getColor(android.R.color.holo_red_dark, null))
            popup.menu.findItem(R.id.mediumPriority)?.icon?.setTint(resources.getColor(android.R.color.holo_orange_light, null))
            popup.menu.findItem(R.id.lowPriority)?.icon?.setTint(resources.getColor(android.R.color.holo_green_dark, null))
            popup.menu.findItem(R.id.noPriority)?.icon?.setTint(resources.getColor(android.R.color.darker_gray, null))

            popup.setOnMenuItemClickListener { item ->
                priorityValue = when (item.itemId) {
                    R.id.highPriority -> 3
                    R.id.mediumPriority -> 2
                    R.id.lowPriority -> 1
                    R.id.noPriority -> 0
                    else -> 0
                }
                setPriorityImage(priorityValue)
                true
            }
            popup.show()
        }

        save.setOnClickListener {
            onApply(
                item.copy(
                    name = name.text.toString(),
                    about = about.text.toString(),
                    done = if (check.isChecked) 1 else 0,
                    priority = priorityValue
                )
            )
            dismiss()
        }

        return v
    }
    private fun setPriorityImage(value: Int) {
        priority.setColorFilter(priorityColor[value])
    }
    override fun onStart() {
        super.onStart()
        val bottomSheet = dialog?.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
        val behavior = BottomSheetBehavior.from(bottomSheet!!)
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        behavior.skipCollapsed = true
        behavior.isHideable = true
    }

}
