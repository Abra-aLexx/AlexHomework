package com.abra.homework_8_1
/**
 * Класс создавался с целью удобной работай над фрагментами.
 * Под этим я имею ввиду корректное восстановление данных после смены конфигурации.
 * То, что получилось немного похоже на паттерн Observer, но это не он.
 * @see FragmentData
 *
 * P.S если вы знаете способ получше, то дайте знать:)
 * Это превое, что мне пришло в голову.
 * */
class FragmentManager {
    companion object{
        private val data = FragmentData()
    }
    fun setCurrentFragment(currentFragment: Int){data.currentFragment = currentFragment}
    fun getCurrentFragment() = data.currentFragment
    fun setContactList(list: ArrayList<ContactItem>){data.list = list}
    fun getContactList() = data.list
    fun setCurrentElementPosition(position: Int){data.position = position}
    fun getCurrentElementPosition() = data.position
}