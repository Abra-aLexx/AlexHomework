package com.abra.alexhomework;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
/**
 * Класс получает список чисел из главной активности, а далее
 * получает нужные результаты при помощи вспомогательного класса Operations и
 * возвращает результаты в главную активность
 * */
public class CountActivity extends AppCompatActivity implements Observer {
    /*
     * Объект data нужен для решения с паттерном Observer
     * раскомментируйте его для демонстрации решения
     */
    // private Data data;
    // кнопка на главном экране
    private Button bt;
    // объект operations нужен для использования операций, требуемых в условии задания
    private final Operations operations = new Operations();
    // список, который будет хранить набор чисел
    private ArrayList<Integer> numbers;
    // сумма всех чисел
    private int sumResult;
    // среднее арифметическое всех чисел и результат деления одной части на другую
    private double halfResult, arithmeticMeanResult;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count);
        bt = findViewById(R.id.buttonResult);
        Intent intent = getIntent();
        if(intent!=null){
            // получаем набор чисел из предыдущей активности
            numbers = intent.getIntegerArrayListExtra("numbers");
        }
        // получаем все нужные результаты
        sumResult = operations.sum(numbers);
        arithmeticMeanResult = operations.arithmeticMean(numbers);
        halfResult = operations.half(numbers);
        // обрабатываем нажатие кнопки, добавляя данные в Intent и отправляем их в главную активность
        bt.setOnClickListener(view->{
            Intent intent1 = new Intent();
            intent1.putExtra("sumResult",sumResult);
            intent1.putExtra("arithmeticMeanResult",arithmeticMeanResult);
            intent1.putExtra("halfResult",halfResult);
            setResult(RESULT_OK,intent1);
            finish();
        });
// код написаный ниже нужен для решения с паттерном Observer, раскомментируйте для демонстрации
//        MainActivity.manager.addObserver(this);
//        MainActivity.manager.notify(this);
//        numbers = data.getNumbers();
//        sumResult = operations.sum(numbers);
//        arithmeticMeanResult = operations.arithmeticMean(numbers);
//        halfResult = operations.half(numbers);
//        MainActivity.manager.setDate(numbers,sumResult,halfResult,arithmeticMeanResult);
//        bt.setOnClickListener(view -> {
//            Intent intent = new Intent();
//            setResult(RESULT_OK,intent);
//            MainActivity.manager.removeObserver(this);
//            finish();
//        });
    }

    @Override
    public void update(Data data) {
        // код написаный ниже нужен для решения с паттерном Observer, раскомментируйте для демонстрации
//        this.data = data;
    }
}
