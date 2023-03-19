package com.example.pomodoro;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    /* Timer do Pomodoro */
    private static long POMODORO_TIME = 1500000; // 25min

    /* Text e Button */
    private TextView textTimer;
    private Button buttonStart;

    /* Contador */
    private CountDownTimer tempoCronometro;
    private long tempoMilissegundos = POMODORO_TIME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textTimer = findViewById(R.id.textTimer);
        buttonStart = findViewById(R.id.buttonStart);

        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                startTimer();
            }
        });

    }

    public void startTimer(){
        tempoCronometro = new CountDownTimer(tempoMilissegundos, 1000){
            @Override
            public void onTick(long tempoRestante) {
                tempoMilissegundos = tempoRestante;
                contagemRegressiva();
            }
            @Override
            public void onFinish() {

            }
        }.start();
        buttonStart.setText("PAUSAR"); // próxima feature
    }

    public void contagemRegressiva(){
        int minutos = (int) (tempoMilissegundos / 1000) / 60;
        int segundos = (int) (tempoMilissegundos / 1000) % 60;
        String tempoFormatado = String.format(Locale.getDefault(), "%02d:%02d", minutos, segundos);
        textTimer.setText(tempoFormatado);
    }
}