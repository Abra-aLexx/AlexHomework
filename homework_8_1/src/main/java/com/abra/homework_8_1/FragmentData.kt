package com.abra.homework_8_1

/**
 * Класс я создал с целью хранения инвформации, для
 * корректного восстановления листа(главным образом после поворота экрана).
 * @property currentFragment я использовал для загрузки нужного фрагмента после
 * поворота экрана.
 * @property position я использовал для корректного восстановления данных в
 * @see FragmentEditContact, так как нужно было после поворота восстановить нужный контакт.
 * Класс я использовал как Singleton в FragmentManager
 *
 * P.S если вы знаете способ получше, то дайте знать:)
 * Это превое, что мне пришло в голову.
 * */
class FragmentData {
    var currentFragment: Int = 1
    var position: Int = 0
    var list: ArrayList<ContactItem> = arrayListOf()
}