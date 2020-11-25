package com.abra.alexhomework;

import java.util.ArrayList;
/**
 * Класс DataManager является объектом Observable и нужен для уведомления
 * объектов Observer о изменении своего состояния.
 * Класс нужен для решения с паттерном Observer
 * */
public class DataManager {
    /*
    * Объект используется для удобного хранения данных,
    * при изменении его данных объект Observable информирует объекты
    * Observer о его изменении
    */
    private Data data = new Data();
    // список хранит объекты Observer
    private final ArrayList<Observer> observers = new ArrayList<>();
    /**
     * Метод добавляет объект Observer в список
     * */
    public void addObserver(Observer observer){
        observers.add(observer);
    }
    /**
     * Метод удаляет объект Observer из списка
     * */
    public void removeObserver(Observer observer){
        observers.remove(observer);
    }
    /**
     * Метод устанавливает данные в объект Data
     * */
    public void setDate(ArrayList<Integer> numbers, int sumResult, double halfResult, double arithmeticMeanResult){
        data.setValues(numbers,sumResult,halfResult,arithmeticMeanResult);
        // унформируем объекты Observer о изменении данных
        notifyAllObservers();
    }
    /**
     * Метод унформирует все объекты Observer о изменении данных
     * */
    public void notifyAllObservers(){
        for (Observer observer: observers) {
            observer.update(data);
        }
    }
    /**
     * Метод унформирует конкретный объект Observer о изменении данных
     * */
    public void notify(Observer observer){
        observer.update(data);
    }
}
