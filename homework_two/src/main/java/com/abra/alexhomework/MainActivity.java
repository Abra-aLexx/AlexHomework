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
 * В классе есть два решения: одно с использованием Intenta,
 * второе с использованием паттерна Observer
 * Для удобного просмотра текст выводится в TextView, который находится в ScrollView
 * */
public class MainActivity extends AppCompatActivity implements Observer {
    /*
    * Объекты data и manager нужны для решения с паттерном Observer
    * раскомментируйте их для демонстрации решения
    */
//    private Data result;
//    public static final DataManager manager = new DataManager();
    // кнопка на главном экране
    private Button bt;
    // текст на главном экране, не виден при первом запуске
    private TextView text;
    /* Объект генератора, c помощью которого в дальнейшем
    * получаем набор случайных чисел*/
    private NumberGenerator generator = new NumberGenerator();
    // список, который будет хранить набор чисел
    private ArrayList<Integer> numbers;
    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        text = findViewById(R.id.text1);
        bt = findViewById(R.id.button2);
        /* обрабатываем нажатия кнопки,
        * т.е. передаем данные в другую активность, для получения
        * результата. */
        bt.setOnClickListener((view -> {
            // получаем набор чисел
            numbers = generator.generateNumbers();
            Intent intent = new Intent(this,CountActivity.class);
            intent.putExtra("numbers",numbers);
            startActivityForResult(intent,3);
// код написаный ниже нужен для решения с паттерном Observer, раскомментируйте для демонстрации
//              numbers = generator.generateNumbers();
//            manager.addObserver(this);
//            Intent intent = new Intent(this,CountActivity.class);
//            manager.setDate(numbers,0,0,0);
//            startActivityForResult(intent,3);
        }));

    }
    /**
     * В этом методе получаем результаты из другой активности.
     * */
    @SuppressLint({"SetTextI18n", "DefaultLocale"})
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // проверяем data на null
        if(data!=null){
            int sumResult;
            double halfResult, arithmeticMeanResult;
            sumResult = data.getIntExtra("sumResult",0);
            arithmeticMeanResult = data.getDoubleExtra("arithmeticMeanResult",0);
            halfResult = data.getDoubleExtra("halfResult",0);
            // вызываем метод для вывода в Log и передаем в него результаты
            logcat(sumResult,halfResult,arithmeticMeanResult);
            /* выводим в TextView текст с результатами
             * при выводе среднего арифметического и результата деления я
             * использовал форматирование, да бы не выводить большое кол-во знаков после запятой
             * */
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
    /**
     * Метод выводит информацию в Log.
     * Метод принимает сумму чисел, среднее арифметическое
     * и результат деления одной части на другую.
     * */
    private void logcat(int sumResult, double halfResult, double arithmeticMeanResult){
        Log.d("Val",getString(R.string.generated_numbers)+" "+numbers.toString()+"\n");
        Log.d("Val",getString(R.string.sum)+" "+sumResult+"\n");
        Log.d("Val",getString(R.string.arithmetic_mean)+" "+String.format("%.2f",arithmeticMeanResult)+"\n");
        Log.d("Val",getString(R.string.devision)+" "+String.format("%.2f",halfResult)+"\n");
    }
/**
 * Метод обновляет результаты, нужен при решении
 * с паттерном Observer
 * */
    @Override
    public void update(Data data) {
        // код написаный ниже нужен для решения с паттерном Observer, раскомментируйте для демонстрации
        //result = data;
    }
}