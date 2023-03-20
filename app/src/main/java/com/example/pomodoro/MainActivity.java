package com.example.pomodoro;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.time.LocalDate;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    /* Timer do Pomodoro */
    private static long POMODORO_TIME = 1500000; // 25min
    private static long LONG_BREAK_TIME = 600000; // 10min
    private static long SHORT_BREAK_TIME = 300000; // 5min

    /* Status Start/Pause e Timer */
    private boolean statusStartPause;
    private int statusModeTimer = 0;

    /* Text e Button */
    private TextView textNumberDay;
    private TextView textTimer;
    private TextView textStatusMode;
    private Button buttonStartPause;
    private Button buttonStatusMode;

    /* Contador */
    private CountDownTimer tempoCronometro;
    private long tempoMilissegundos = POMODORO_TIME;
    private int pomodoros = 0;
    private int pomodoros_day = 0;

    /* Datas */
    String nowPomodoro = new String();
    String lastPomodoro = new String();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textTimer = findViewById(R.id.textTimer);
        textNumberDay = findViewById(R.id.textNumberDay);
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
                messageNotification();
                seeStatus();
                timeCounter();
                statusStartPause = false;
                buttonStartPause.setText("INICIAR");
                buttonStatusMode.setVisibility(View.VISIBLE);
                textNumberDay.setText("" + pomodoros_day);
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

    private void messageNotification(){
        String titulo = new String();
        String texto = new String();
        pomodoros++;
        switch (statusModeTimer) {
            case 0:
                checkDay();
                switch (pomodoros){
                    case 1:
                        titulo = "1 Pomodoro Concluído - Boa";
                    break;
                    case 2:
                        titulo = "2 Pomodoros Concluídos - Muito Bem";
                    break;
                    case 3:
                        titulo = "3 Pomodoros Concluídos - Tens potencial";
                    break;
                    case 4:
                        titulo = "4 Pomodoros Concluídos - Parabéns";
                    break;
                }
                if(pomodoros == 4){
                    texto = "Descanse mais, você merece";
                    pomodoros=0;
                    statusModeTimer = 2;
                }else{
                    texto = "Hora de fazer uma pausa rápida";
                    statusModeTimer = 1;
                }
            break;
            case 1:
                titulo = "Descanso feito";
                texto = "Hora de voltar a rotina";
                statusModeTimer = 0;
            break;
            case 2:
                titulo = "Descansou bastante";
                texto = "Hora de voltar a rotina";
                statusModeTimer = 0;
            break;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Android Api >= 26
            final String id = "NOTIFICATION";
            NotificationChannel channel = new NotificationChannel(id,"Timer", NotificationManager.IMPORTANCE_HIGH);

            getSystemService(NotificationManager.class).createNotificationChannel(channel);
            Notification.Builder notification = new Notification.Builder(this, id)
                    .setContentTitle(titulo)
                    .setContentText(texto)
                    .setSmallIcon(R.mipmap.ic_launcher_foreground)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_foreground))
                    .setAutoCancel(true);
            NotificationManagerCompat.from(this).notify(1, notification.build());
        }
    }


    private void checkDay(){
        if(pomodoros_day == 0){
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                LocalDate hoje = LocalDate.now();
                lastPomodoro = hoje.toString();
                System.out.println(lastPomodoro);
            }
            pomodoros_day++;
        }else{
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                LocalDate agora = LocalDate.now();
                nowPomodoro = agora.toString();
                System.out.println(nowPomodoro);
            }
            if(nowPomodoro.equals(lastPomodoro)){
                pomodoros_day++;
            }else{
                pomodoros_day = 0;
                lastPomodoro = nowPomodoro;
            }
        }
    }

}