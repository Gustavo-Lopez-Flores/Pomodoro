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
    private static long LONG_BREAK_TIME = 600000; // 10min
    private static long SHORT_BREAK_TIME = 300000; // 5min
    private int statusModeTimer = 0;

    /* Text e Button */
    private TextView textTimer;
    private TextView textStatusMode;
    private Button buttonStartPause;
    private Button buttonStatusMode;

    /* Contador */
    private CountDownTimer tempoCronometro;
    private boolean statusStartPause;
    private long tempoMilissegundos = POMODORO_TIME;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textTimer = findViewById(R.id.textTimer);
        textStatusMode = findViewById(R.id.textStatusMode);
        buttonStartPause = findViewById(R.id.buttonStartPause);
        buttonStatusMode = findViewById(R.id.buttonStatusMode);

        buttonStatusMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                statusModeTimer = (statusModeTimer+1) % 3;
                seeStatus();
                timeCounter();
            }
        });

        buttonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                buttonStatusMode.setVisibility(View.INVISIBLE);
                if(statusStartPause){
                    pauseTimer();
                }else{
                    startTimer();
                }
            }
        });

        timeCounter();
    }

    private void startTimer(){
        tempoCronometro = new CountDownTimer(tempoMilissegundos, 1000){
            @Override
            public void onTick(long tempoRestante) {
                tempoMilissegundos = tempoRestante;
                timeCounter();
            }
            @Override
            public void onFinish() {
                if(statusModeTimer == 0){
                    statusModeTimer++;
                }else{
                    statusModeTimer=0;
                }
                seeStatus();
                timeCounter();
                statusStartPause = false;
                buttonStartPause.setText("INICIAR");
                buttonStatusMode.setVisibility(View.VISIBLE);
            }
        }.start();
        statusStartPause = true;
        buttonStartPause.setText("PAUSAR");
    }

    private void pauseTimer(){
        tempoCronometro.cancel();
        statusStartPause = false;
        buttonStartPause.setText("CONTINUAR");
    }

    private void timeCounter(){
        int minutos = (int) (tempoMilissegundos / 1000) / 60;
        int segundos = (int) (tempoMilissegundos / 1000) % 60;
        String tempoFormatado = String.format(Locale.getDefault(), "%02d:%02d", minutos, segundos);
        textTimer.setText(tempoFormatado);
    }

    private void seeStatus(){
        switch (statusModeTimer){
            case 0:
                textStatusMode.setText("POMODORO");
                tempoMilissegundos = POMODORO_TIME;
                break;
            case 1:
                textStatusMode.setText("PAUSA CURTA");
                tempoMilissegundos = SHORT_BREAK_TIME;
                break;
            case 2:
                textStatusMode.setText("PAUSA LONGA");
                tempoMilissegundos = LONG_BREAK_TIME;
                break;
        }
    }
}