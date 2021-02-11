package com.abra.homework_8_1

import android.os.Bundle
import android.text.InputType
import android.view.View
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction

class FragmentAddContact : Fragment(R.layout.fragment_add_contact) {
    private lateinit var loader: FragmentLoader
    private lateinit var name: EditText
    private lateinit var info: EditText
    private lateinit var group: RadioGroup
    private lateinit var imgBack: ImageButton
    private lateinit var imgAdd: ImageButton
    private lateinit var contactList: ArrayList<ContactItem>
    private lateinit var fragmentManager: FragmentManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(view) {
            name = findViewById(R.id.editTextName)
            info = findViewById(R.id.editTextInfo)
            group = findViewById(R.id.radioGroup)
            imgBack = findViewById(R.id.imgButtonBack1)
            imgAdd = findViewById(R.id.imgButtonApply)
        }
        loader = requireActivity() as FragmentLoader
        createFragmentManager()
        getData()
        setListeners()
    }

    private fun getData() {
        contactList = requireArguments().getParcelableArrayList<ContactItem>("list") as ArrayList<ContactItem>
    }

    private fun createFragmentManager() {
        fragmentManager = FragmentManager()
        fragmentManager.setCurrentFragment(2)
    }

    private fun setListeners() {
        group.setOnCheckedChangeListener { radioGroup, i ->
            when (radioGroup.checkedRadioButtonId) {
                R.id.radioButtonPhone -> {
                    with(info) {
                        setText("")
                        hint = getString(R.string.phone_number)
                        inputType = InputType.TYPE_CLASS_PHONE
                    }
                }
                R.id.radioButtonEmail -> {
                    with(info) {
                        setText("")
                        hint = getString(R.string.email)
                        inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                    }
                }
            }
        }
        imgBack.setOnClickListener {
            showFragment()
        }
        imgAdd.setOnClickListener {
            var iconId = 0
            var typeInfo = ""
            when (group.checkedRadioButtonId) {
                R.id.radioButtonPhone -> {
                    iconId = R.drawable.icon_contact
                    typeInfo = "phone"
                }
                R.id.radioButtonEmail -> {
                    iconId = R.drawable.icon_email
                    typeInfo = "email"
                }
            }
            val textName = name.text.toString()
            val textInfo = info.text.toString()
            if (textName != "" && textInfo != "") {
                contactList.add(ContactItem(iconId, textName, textInfo, typeInfo))
                showFragment()
            } else {
                Toast.makeText(context, "Fields can't be empty!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showFragment() {
        loader.loadFragment(FragmentContactsList::class.java,
                FragmentTransaction.TRANSIT_FRAGMENT_CLOSE,
                bundleOf("list" to contactList))
    }
}

