package com.abra.alexhomework;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import static com.abra.alexhomework.Operations.sum;
import static com.abra.alexhomework.Operations.half;
import static com.abra.alexhomework.Operations.arithmeticMean;
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
//     private Data data;
//     private final DataManager manager = DataManager.getInstance();

    private ArrayList<Integer> numbers;
    private int sumResult;
    private double halfResult, arithmeticMeanResult;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count);
        Button bt = findViewById(R.id.buttonResult);
        Intent intent = getIntent();
        if (intent != null) {
            numbers = intent.getIntegerArrayListExtra("numbers");
            sumResult = sum(numbers);
            arithmeticMeanResult = arithmeticMean(numbers);
            halfResult = half(numbers);
            bt.setOnClickListener(view -> {
                Intent intent1 = new Intent();
                intent1.putExtra("sumResult", sumResult);
                intent1.putExtra("arithmeticMeanResult", arithmeticMeanResult);
                intent1.putExtra("halfResult", halfResult);
                setResult(RESULT_OK, intent1);
                finish();
            });
// код написаный ниже нужен для решения с паттерном Observer, раскомментируйте для демонстрации
//        manager.addObserver(this);
//        manager.notify(this,new Data(Data.getPreviousNumbers(),0,0,0));
//        numbers = data.getNumbers();
//        sumResult = sum(numbers);
//        arithmeticMeanResult = arithmeticMean(numbers);
//        halfResult = half(numbers);
//        manager.notifyAllObservers(new Data(numbers,sumResult,halfResult,arithmeticMeanResult));
//        bt.setOnClickListener(view -> {
//            Intent intent1 = new Intent();
//            setResult(RESULT_OK,intent1);
//            manager.removeObserver(this);
//            finish();
//        });
        }
    }
        @Override
        public void update (Data data){
            // код написаный ниже нужен для решения с паттерном Observer, раскомментируйте для демонстрации
//        this.data = data;
        }
    }