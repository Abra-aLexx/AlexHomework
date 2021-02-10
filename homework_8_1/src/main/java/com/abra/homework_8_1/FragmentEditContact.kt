package com.abra.homework_8_1

import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

class FragmentEditContact : Fragment(R.layout.fragment_edit_contact) {
    private lateinit var loader: FragmentLoader
    private lateinit var name: EditText
    private lateinit var info: EditText
    private lateinit var imgBack: ImageButton
    private lateinit var btRemove: Button
    private lateinit var contactList: ArrayList<ContactItem>
    private var position = 0
    private lateinit var contactItem: ContactItem
    private lateinit var fragmentManager: FragmentManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(view) {
            name = findViewById(R.id.editTextName1)
            info = findViewById(R.id.editTextInfo1)
            imgBack = findViewById(R.id.imgButtonBack)
            btRemove = findViewById(R.id.buttonRemove)
        }
        loader = requireActivity() as FragmentLoader
        createFragmentManager()
        getData()

    }

    private fun createFragmentManager() {
        fragmentManager = FragmentManager()
        with(fragmentManager) {
            setCurrentFragment(3)
            // помещаю позицию элемента, для корректно восстановления после поворота экрана
            setCurrentElementPosition(position)
        }

    }

    private fun getData() {
        requireArguments().run {
            contactList = getParcelableArrayList<ContactItem>("list") as ArrayList<ContactItem>
            position = getInt("position", -1)
            /* эта проверка нужна для случая, если список был отфильтрован,
            * так как позиция элемента по сути меняется после фильтрации,
            * но при помощи indexOf() можно получить корректную позицию
            * нужного элемента.*/
            if (position == -1) {
                contactItem = getParcelable<ContactItem>("item") as ContactItem
                position = contactList.indexOf(contactItem)
            } else {
                contactItem = contactList[position]
            }
        }
        when (contactItem.typeInfo) {
            "phone" -> {
                info.inputType = InputType.TYPE_CLASS_PHONE
            }
            "email" -> {
                info.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
            }
        }
        setListeners(position)
        name.setText(contactItem.name)
        info.setText(contactItem.info)
    }

    private fun setListeners(position: Int) {
        imgBack.setOnClickListener {
            val iconId: Int = contactItem.iconId
            val typeInfo: String = contactItem.typeInfo
            val textName = name.text.toString()
            val textInfo = info.text.toString()
            if (textName != "" && textInfo != "") {
                contactList[position] = ContactItem(iconId, textName, textInfo, typeInfo)
                showFragment()
            } else {
                Toast.makeText(context, "Fields can't be empty!", Toast.LENGTH_SHORT).show()
            }
        }
        btRemove.setOnClickListener {
            contactList.remove(contactItem)
            showFragment()
        }
    }

    private fun showFragment() {
        loader.loadFragment(FragmentContactsList::class.java,
                FragmentTransaction.TRANSIT_FRAGMENT_CLOSE,
                bundleOf("list" to contactList))
    }
}