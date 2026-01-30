package com.rezahosseini.taskmanager.view.menu

import android.annotation.SuppressLint
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.ImageView
import android.widget.PopupMenu
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.rezahosseini.taskmanager.R
import com.rezahosseini.taskmanager.model.local.DataEntity

class AddItemBottomSheet(
    private val onApply: (DataEntity) -> Unit
) : BottomSheetDialogFragment() {

    private var priorityValue: Int = 0
    private val priorityColor = intArrayOf(Color.GRAY,Color.GREEN, Color.YELLOW, Color.RED )
    private lateinit var priorityImage: ImageView

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bottomsheet_add_item, container, false)

        val editTextName: com.google.android.material.textfield.TextInputEditText = view.findViewById(R.id.editTextName)
        val editTextAbout: com.google.android.material.textfield.TextInputEditText = view.findViewById(R.id.editTextAbout)
        priorityImage = view.findViewById(R.id.priority)
        val save: com.google.android.material.button.MaterialButton = view.findViewById(R.id.save)

        setPriorityImage(priorityValue)

        priorityImage.setOnClickListener { anchor ->
            val popup = PopupMenu(requireContext(), anchor)
            popup.menuInflater.inflate(R.menu.more_menu_priority, popup.menu)

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
            val entity = DataEntity(
                id = 0,
                name = editTextName.text.toString(),
                about = editTextAbout.text.toString(),
                priority = priorityValue,
                done = 0
            )
            onApply(entity)
            dismiss()
        }

        return view
    }

    private fun setPriorityImage(value: Int) {
        priorityImage.setColorFilter(priorityColor[value])
    }

    override fun onStart() {
        super.onStart()

        val editName = dialog?.findViewById<EditText>(R.id.editTextName)

        editName?.post {
            editName.requestFocus()
            val imm = requireContext().getSystemService(
                android.content.Context.INPUT_METHOD_SERVICE
            ) as android.view.inputmethod.InputMethodManager
            imm.showSoftInput(editName, android.view.inputmethod.InputMethodManager.SHOW_IMPLICIT)
        }

        dialog?.window?.setSoftInputMode(
            WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE or
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE
        )
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return object : BottomSheetDialog(requireContext(), theme) {
            @SuppressLint("MissingSuperCall")
            override fun onBackPressed() {
                dismissAllowingStateLoss()
            }
        }
    }
}
