package com.abra.alexhomework;

import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Random;
/**
 * Класс отвечает за генерацию случайного набора чисел случайного размера.
 * Диапозон чисел от 0 до 1000,
 * Диапозон самого набора чисел от 10 до 200.
 * */
public class NumberGenerator {
    final private Random random = new Random();
    public ArrayList<Integer> generateNumbers(){
        /*
        Решил использовать Set, т.к. он позволяет хранить только уникальные объекты,
        а значит нет необходимости делать проверки на совпадения чисел
        */
        HashSet<Integer> busyNumbers = new HashSet<>();
        int length;
        do {
            length = random.nextInt(190) + 10;
        } while (length % 2 != 0);

        while (busyNumbers.size()<length) {
            busyNumbers.add(random.nextInt(1000));
        }
        return new ArrayList<>(busyNumbers);
    }
}
