package com.example.wonsz;

import android.graphics.Color;
import android.os.Bundle;
import android.ranging.RangingManager;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.window.SurfaceSyncGroup;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private List<Japka> ilejapka= new ArrayList<>();
    private int wynik=0;
    //surfaceview to pole ekranu, surfaceholder sprawia że sie na nim rysuje
    private SurfaceView ekran;
    private SurfaceHolder surfaceHolder;
    private TextView scor;
    private ImageButton gura, dul, lewo, prawo;

    private static int rozmiarskubanca=0;
    private static int rozmiarjapka=0;
    private static int dłskubanca=3;
    private static int kolorwonsz= Color.BLUE;
    private static int kolorjapko= Color.RED;
    //od 1-1000
    private static int prędkosc=800;

    private int PozycjaX, PozycjaY;

    //stoper do ruchu węża
    private Timer timer;

    // kierunek prodóży wężusia
    private String kierunek="prawo";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        SurfaceView ekran = findViewById(R.id.ekran);
        TextView scor=findViewById(R.id.scor);

        ImageButton gura = findViewById(R.id.gura);
        ImageButton dul = findViewById(R.id.dul);
        ImageButton lewo = findViewById(R.id.lewo);
        ImageButton prawo = findViewById(R.id.prawo);

        ekran.getHolder().addCallback(this);

        gura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(kierunek.equals("dul")){

                }
                else {

                    kierunek="gura";
                }
            }
        });
        dul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(kierunek.equals("gura")){

                }
                else {
                    kierunek="dul";

                }
            }
        });
        lewo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(kierunek.equals("prawo")){

                }
                else {
                    kierunek="lewo";

                }
            }
        });
        prawo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(kierunek.equals("lewo")){

                }
                else {
                    kierunek="prawo";
                }
            }
        });
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
        this.surfaceHolder = surfaceHolder;

        start();
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }
    private void start() {
        ilejapka.clear();

        scor.setText("wynik: 0");

        wynik=0;
        kierunek="prawo";

        int pozycjastartX = (rozmiarskubanca) * dłskubanca;
        int x=0;
        do {
            Japka japko = new Japka(pozycjastartX,rozmiarskubanca);
            ilejapka.add(japko);

            pozycjastartX = pozycjastartX - (rozmiarskubanca*2);
            x++;
        }while (x<dłskubanca);

        ustawjapko();
        //zacznij gre
        ruch();
    }
    private void ustawjapko(){
        int szerekran = ekran.getWidth()- (rozmiarskubanca*2);
        int dłekran = ekran.getHeight()- (rozmiarskubanca*2);

        int RandomX = new Random().nextInt(szerekran/rozmiarskubanca);
        int RandomY = new Random().nextInt(dłekran/rozmiarskubanca);

        if((RandomX % 2)!=0){
            RandomX=RandomX+1;
        }
        if((RandomY % 2)!=0){
            RandomY=RandomY+1;
        }
        PozycjaX = (rozmiarskubanca * RandomX) +rozmiarskubanca;
        PozycjaY = (rozmiarskubanca * RandomY) +rozmiarskubanca;
    }
    private void ruch(){
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                //pozycja głowy wensza
                int headposX = ilejapka.get(0).getPozycjaX();
                int headposY = ilejapka.get(0).getPozycjaY();

                //czy zjad japko
                if(headposX == PozycjaX && PozycjaY == headposY){
                    urośnijwensza();

                    ustawjapko();
                }
                //w którą patrzy wensz
                switch (kierunek) {
                    case "prawo":
                        ilejapka.get(0).setPozycjaX(headposX + (rozmiarskubanca * 2));
                        ilejapka.get(0).setPozycjaY(headposY);
                        break;
                    case "lewo":
                        ilejapka.get(0).setPozycjaX(headposX - (rozmiarskubanca * 2));
                        ilejapka.get(0).setPozycjaY(headposY);
                        break;
                    case "gura":
                        ilejapka.get(0).setPozycjaX(headposX);
                        ilejapka.get(0).setPozycjaY(headposY + (rozmiarskubanca * 2 ));
                        break;
                    case "dul":
                        ilejapka.get(0).setPozycjaX(headposX);
                        ilejapka.get(0).setPozycjaY(headposY - (rozmiarskubanca * 2 ));
                        break;
                }

                if(czyprzegrana(headposX,headposY)){
                    //jak przegrał, zatrzymajgre
                    timer.purge();
                    timer.cancel();
                }

            }
        }, 1000 -prędkosc, 1000-prędkosc);
    }
    private void urośnijwensza(){

    }
    private boolean czyprzegrana(int headposX, int headposY){
        boolean gameOver = false;
        return gameOver;
    }
}