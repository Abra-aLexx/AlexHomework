package com.abra.alexhomework;

import android.util.Log;

import java.util.ArrayList;
/**
 * Класс хранит методы для основных операций, указанных в условии задания
 * */
public class Operations {
    /**
     * Метод возвращает сумму всех чисел в наборе
     * */
    public int sum(ArrayList<Integer> numbers){
        int sum = 0;
        for (Integer i:numbers) sum+=i;
        return sum;
    }
    /**
     * Метод возвращает среднее арифметическое всех чисел в наборе
     * */
    public double arithmeticMean(ArrayList<Integer> numbers){
        double sum = 0;
        for (int i:numbers)sum+=i;
        return sum/numbers.size();
    }
    /**
     * Метод возвращает результат деления одной половины чисел на другую
     * */
    public double half(ArrayList<Integer> numbers){
        // переменная хранит номер элемента, расположенного по середине
        int half = numbers.size()/2;
        /*
        * sum будет хранить сумму чисел из первой половины, а
        * difference будет хранить разность чисел из второй половины,
        * ей присваивается значение первого элемента из второй половины чисел
        */
        int sum = 0, difference = numbers.get(half);
        // находим сумму чисел из первой половины
        for (int i = 0; i < half; i++) sum += numbers.get(i);
        // находим разность чисел из второй половины
        for (int i = half+1; i < numbers.size(); i++)difference-=numbers.get(i);
        // выводим сумму первой части и разность второй части в Log
        Log.d("Val","Первая часть равна "+sum);
        Log.d("Val","Вторая часть равна "+difference);
        // выполняем проверку деления на 0
        if(difference!=0)return (double) sum/difference;
        else return 0;
    }
}
