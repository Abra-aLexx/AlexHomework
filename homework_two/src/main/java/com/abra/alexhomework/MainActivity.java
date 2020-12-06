package com.abra.alexhomework;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
/**
 * Главный класс, отвечает за отправку сгенерированных чисел
 * в другую активность.
 * В классе есть два решения: одно с использованием Intent,
 * второе с использованием паттерна Observer
 * Для удобного просмотра текст выводится в TextView, который находится в ScrollView
 * */
public class MainActivity extends AppCompatActivity implements Observer {
    /*
    * Объекты data и manager нужны для решения с паттерном Observer
    * раскомментируйте их для демонстрации решения
    */
//    private Data result;
//    private final DataManager manager = DataManager.getInstance();
    private static final int REQUEST_CODE = 3;
    private TextView text;
    private final NumberGenerator generator = new NumberGenerator();
    private ArrayList<Integer> numbers;
    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = findViewById(R.id.text1);
        Button bt = findViewById(R.id.button2);
        bt.setOnClickListener((view -> {
            numbers = generator.generateNumbers();
            Intent intent = new Intent(this,CountActivity.class);
            intent.putExtra("numbers",numbers);
            startActivityForResult(intent,REQUEST_CODE);
// код написаный ниже нужен для решения с паттерном Observer, раскомментируйте для демонстрации
//            numbers = generator.generateNumbers();
//            manager.addObserver(this);
//            Intent intent = new Intent(this,CountActivity.class);
//            manager.notifyAllObservers(new Data(numbers,0,0,0));
//            startActivityForResult(intent,REQUEST_CODE);
        }));

    }
    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            int sumResult;
            double halfResult, arithmeticMeanResult;
            sumResult = data.getIntExtra("sumResult",0);
            arithmeticMeanResult = data.getDoubleExtra("arithmeticMeanResult",0);
            halfResult = data.getDoubleExtra("halfResult",0);
            text.setText(getString(R.string.generated_numbers)+"\n"+ numbers.toString() +"\n"+
                    getString(R.string.sum)+" "+sumResult+"\n"+
                    getString(R.string.arithmetic_mean)+" "+String.format("%.2f",arithmeticMeanResult)+"\n"+
                            getString(R.string.devision)+" "+String.format("%.2f",halfResult));

// код написаный ниже нужен для решения с паттерном Observer, раскомментируйте для демонстрации
//            text.setText(getString(R.string.generated_numbers)+"\n"+ numbers.toString() +"\n"+
//                    getString(R.string.sum)+" "+result.getSumResult()+"\n"+
//                    getString(R.string.arithmetic_mean)+" "+String.format("%.2f",result.getArithmeticMeanResult())+"\n"+
//                    getString(R.string.devision)+" "+String.format("%.2f",result.getHalfResult()));

        }
    }
    @Override
    public void update(Data data) {
        // код написаный ниже нужен для решения с паттерном Observer, раскомментируйте для демонстрации
//        result = data;
    }
}