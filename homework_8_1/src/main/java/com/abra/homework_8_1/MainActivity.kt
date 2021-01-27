package com.abra.homework_8_1

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.os.bundleOf
import androidx.fragment.app.commit

class MainActivity : AppCompatActivity() {
    private lateinit var fragmentManager: FragmentManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        fragmentManager = FragmentManager()
        showFragment()
    }

    /**
     * Метод нужен для первого запуска фрагмента и дальнейшего
     * восстановления фрагментов после поворота.
     * Об объекте FragmentManager смотрите по ссылке
     * @see FragmentManager
     *
     * Заметил некую странность при поворотах экрана.
     * В общем тестил на двух девайсах.
     * На API 24 работает корректно и поворочивается, а
     * на API 28 не хочет поворачиваться, честно говоря я без понятия
     * в чем проблема.
     * */
    private fun showFragment() {
        supportFragmentManager.commit {
            val list = fragmentManager.getContactList()
            when (fragmentManager.getCurrentFragment()) {
                1 -> {
                    replace(R.id.fragmentContainer, FragmentContactsList::class.java,
                            bundleOf("list" to list))
                }
                2 -> {
                    replace(R.id.fragmentContainer, FragmentAddContact::class.java,
                            bundleOf("list" to list))
                }
                3 -> {
                    replace(R.id.fragmentContainer, FragmentEditContact::class.java,
                            bundleOf("list" to list, "position" to fragmentManager.getCurrentElementPosition()))
                }
            }
        }
    }
}