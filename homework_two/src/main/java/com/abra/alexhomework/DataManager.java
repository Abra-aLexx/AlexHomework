package com.abra.alexhomework;

import java.util.ArrayList;
/**
 * Класс DataManager является объектом Observable и нужен для уведомления
 * объектов Observer о изменении своего состояния.
 * Класс нужен для решения с паттерном Observer
 * */
public class DataManager {
    private static final DataManager manager = new DataManager();
    private final ArrayList<Observer> observers = new ArrayList<>();

    private DataManager(){}

    public static DataManager getInstance(){
        return manager;
    }
    public void addObserver(Observer observer){
        observers.add(observer);
    }
    public void removeObserver(Observer observer){
        observers.remove(observer);
    }
    public void notifyAllObservers(Data data){
        for (Observer observer: observers) {
            observer.update(data);
        }
    }
    public void notify(Observer observer, Data data){
        observer.update(data);
    }
}
