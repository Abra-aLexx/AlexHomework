package com.abra.homework_8_2.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.add
import androidx.fragment.app.commit
import com.abra.homework_8_2.R
import com.abra.homework_8_2.fragments.FragmentCarList
import com.abra.homework_8_2.fragments.FragmentLoader
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date

class MainActivity : AppCompatActivity(), FragmentLoader {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        writeLogToFile()
        showFragment()
    }

    private fun showFragment() {
        supportFragmentManager.commit {
            add<FragmentCarList>(R.id.fragmentContainer)
        }
    }

    override fun loadFragment(fragmentClass: Class<out Fragment>, transitionCode: Int, bundle: Bundle) {
        supportFragmentManager.commit {
            setTransition(transitionCode)
            replace(R.id.fragmentContainer, fragmentClass, bundle)
            addToBackStack(null)
        }
    }

    override fun loadFragment(fragment: Fragment, transitionCode: Int) {
        supportFragmentManager.commit {
            setTransition(transitionCode)
            replace(R.id.fragmentContainer, fragment)
            addToBackStack(null)
        }
    }

    private fun writeLogToFile() {
        val logList = StringBuilder()
        val file = File(filesDir, "Logs.txt")
        if (!file.exists()) {
            file.createNewFile()
        }
        logList.append("${file.readText()} \n")
        val simpleDateFormat = SimpleDateFormat.getDateTimeInstance()
        val date = simpleDateFormat.format(Date())
        logList.append(date)
        file.writeText(logList.toString())
    }
}