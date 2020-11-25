package com.abra.alexhomework;

import java.util.ArrayList;
/**
 * Класса Data нужен для хранения данных: набора чисел, результата суммы,
 * среднее арифметическое всех чисел из набора и результата деления одной части на другую.
 * Класс создан для удобства хранения данных и нужен для решения с паттерном Observer.
 * */
public class Data {
    private ArrayList<Integer> numbers;
    private int sumResult;
    private double halfResult, arithmeticMeanResult;
    public Data() {
    }
    /**
     * метод устанавливает значения у необходимых данных
     * */
    public void setValues(ArrayList<Integer> numbers, int sumResult, double halfResult, double arithmeticMeanResult){
        this.numbers = numbers;
        this.sumResult = sumResult;
        this.halfResult = halfResult;
        this.arithmeticMeanResult = arithmeticMeanResult;
    }
    /**
     * Метод возвращает набор чисел
     * */
    public ArrayList<Integer> getNumbers() {
        return numbers;
    }
    /**
     * Метод возвращает результат суммы
     * */
    public int getSumResult() {
        return sumResult;
    }
    /**
     * Метод возвращает результат деления одной части на другую
     * */
    public double getHalfResult() {
        return halfResult;
    }
    /**
     * Метод возвращает среднее арифметическое набора чисел
     * */
    public double getArithmeticMeanResult() {
        return arithmeticMeanResult;
    }
}
