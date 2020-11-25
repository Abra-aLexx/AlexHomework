package com.abra.alexhomework;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
/**
 * Класс отвечает за генерацию случайного набора чисел случайного размера.
 * Диапозон чисел от 0 до 1000,
 * Диапозон самого набора чисел от 10 до 200.
 * */
public class NumberGenerator {
    final private Random random = new Random();
    /*
    Решил использовать Set, т.к. он позволяет хранить только уникальные объекты,
    а значит нет необходимости делать проверки на совпадения чисел
    */
    private HashSet<Integer> busyNumbers;
    private ArrayList<Integer> numbers;
    /**
     * Метод генерирует набор случайных чисел случайной длинны
     * */
    public ArrayList<Integer> generateNumbers(){
        busyNumbers = new HashSet<>();
        numbers = new ArrayList<>();
        // длинна будущего набора чисел
        int length;
        /*
        * Цикл генерирует рандомную длинну набора чисел, если число нечетное, то цикл
        * выполняется заново(длинна от 10 до 200)
        * */
        do {
            length = random.nextInt(190) + 10;
        } while (length % 2 != 0);
        // генерируем рандомные числа и помещаем их в Set, повторы игнорируются(от 0 до 1000)
        while (busyNumbers.size()<length){
            busyNumbers.add(random.nextInt(1000));
        }
        // выводим в Log длинну набора чисел
        Log.d("Val", "Кол-во чисел "+busyNumbers.size()+" ");
        /*
        *  Преобразуем в ArrayList<Integer>, это нужно для дальнейшей
        *  передачи листа в другую активность
        * */
        numbers.addAll(busyNumbers);
        return numbers;
    }
}
