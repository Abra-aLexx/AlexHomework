package com.abra.homework_8_1

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.fragment.app.commit
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class FragmentContactsList : Fragment(R.layout.fragment_contacts_list) {
    private lateinit var recyclerView: RecyclerView
    private lateinit var contactItemAdapter: ContactItemAdapter
    private lateinit var button: FloatingActionButton
    private lateinit var searchView: SearchView
    private lateinit var fragmentManager: FragmentManager

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(view) {
            button = findViewById(R.id.floatingActionButton)
            recyclerView = findViewById(R.id.recyclerView)
            searchView = findViewById(R.id.searchView)
        }
        createFragmentManager()
        setRecyclerViewSettings()
        getData(requireArguments())
        setSearchViewSettings()
        setFabListener()
    }

    private fun createFragmentManager() {
        fragmentManager = FragmentManager()
        fragmentManager.setCurrentFragment(1)
    }

    private fun setSearchViewSettings() {
        searchView.apply {
            imeOptions = EditorInfo.IME_ACTION_DONE
            setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(p0: String?) = false

                override fun onQueryTextChange(p0: String?): Boolean {
                    contactItemAdapter.filter.filter(p0)
                    return false
                }
            })
            setOnCloseListener {
                contactItemAdapter.updateList(contactItemAdapter.getListBeforeUsingSearchView())
                onActionViewCollapsed()
                true
            }
        }
    }

    private fun getData(bundle: Bundle?) {
        bundle?.run {
            val list = getParcelableArrayList<ContactItem>("list") as ArrayList<ContactItem>
            contactItemAdapter.updateList(list)
            fragmentManager.setContactList(list)
        }
    }

    private fun setRecyclerViewSettings() {
        contactItemAdapter = ContactItemAdapter()
        val rotation = requireActivity().windowManager.defaultDisplay.rotation
        with(recyclerView) {
            // почему-то работает наоборот, я так и не понял почему
            layoutManager = if (rotation == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
                LinearLayoutManager(context, RecyclerView.VERTICAL, false)
            else
                GridLayoutManager(context, 2)
            adapter = contactItemAdapter
        }
        contactItemAdapter.itemClickListener = { item, position ->
            /*
            * Тут я таким способом реализовал корректную работу с SearchView,
            * так как с ним возникало множество баггов.
            * Поясню суть проблемы, которая была.
            * В общем, когда я использовал фильтрацию и потом
            * пытался отредактировать, то у меня подчищался список
            * и оставались только отфильтрованные контакты, а если я пробовал
            * отредактировать, то всегда менялся самый первый элемент.
            * Надеюсь объяснил нормально, но главное, что проблема решена.
            * */
            val bundle = if (!searchView.isIconified)
                bundleOf("list" to contactItemAdapter.getListBeforeUsingSearchView(), "item" to item)
            else
                bundleOf("list" to contactItemAdapter.getList(), "position" to position)
            if (!searchView.isIconified) {
                searchView.onActionViewCollapsed()
            }
            /*
            * Не совсем уверен в правильности этого действия.
            * Возможно нужно организовать код по смене фрагментов
            * в отдельном классе
            * */
            requireActivity().supportFragmentManager.commit {
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                replace(R.id.fragmentContainer, FragmentEditContact::class.java,
                        bundle)
            }
        }
    }

    private fun setFabListener() {
        val bundle = if (!searchView.isIconified)
            bundleOf("list" to contactItemAdapter.getListBeforeUsingSearchView())
        else
            bundleOf("list" to contactItemAdapter.getList())
        button.setOnClickListener {
            if (!searchView.isIconified) {
                searchView.onActionViewCollapsed()
            }
            requireActivity().supportFragmentManager.commit {
                setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                replace(R.id.fragmentContainer, FragmentAddContact::class.java, bundle)
            }
        }
    }
}