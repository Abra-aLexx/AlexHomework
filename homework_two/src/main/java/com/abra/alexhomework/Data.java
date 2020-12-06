package com.abra.alexhomework;

import java.util.ArrayList;
/**
 * Класса Data нужен для хранения данных: набора чисел, результата суммы,
 * среднее арифметическое всех чисел из набора и результата деления одной части на другую.
 * Класс создан для удобства хранения данных и нужен для решения с паттерном Observer.
 * */
public class Data {
    private final ArrayList<Integer> numbers;
    private static ArrayList<Integer> previousNumbers;
    private final int sumResult;
    private final double halfResult;
    private final double arithmeticMeanResult;

    public Data(ArrayList<Integer> numbers, int sumResult, double halfResult, double arithmeticMeanResult) {
        this.numbers = numbers;
        this.sumResult = sumResult;
        this.halfResult = halfResult;
        this.arithmeticMeanResult = arithmeticMeanResult;
        previousNumbers = numbers;
    }
    public ArrayList<Integer> getNumbers() {
        return numbers;
    }
    public int getSumResult() {
        return sumResult;
    }
    public double getHalfResult() {
        return halfResult;
    }
    public double getArithmeticMeanResult() {
        return arithmeticMeanResult;
    }
    public static ArrayList<Integer> getPreviousNumbers(){
        return previousNumbers;
    }
}
