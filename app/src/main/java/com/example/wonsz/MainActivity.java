package com.example.wonsz;

import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.ranging.RangingManager;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.window.SurfaceSyncGroup;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private final List<Japka> ilejapka= new ArrayList<>();
    private int wynik=0;
    //surfaceview to pole ekranu, surfaceholder sprawia że sie na nim rysuje
    private SurfaceView ekran;
    private SurfaceHolder surfaceHolder;
    private TextView scor;
    private ImageButton gura, dul, lewo, prawo;

    private static int rozmiarskubanca=28;
    private static int dłskubanca=3;
    private static int kolorjapek= Color.RED;
    private Paint kolorjapko= null;
    //od 1-1000
    private static int prędkosc=800;

    private int PozycjaX=0, PozycjaY=0;

    //stoper do ruchu węża
    private Timer timer;
    //do malowania na surfaceview
    private Canvas canvas = null;

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
        ekran = findViewById(R.id.ekran);
        scor=findViewById(R.id.scor);

        final ImageButton gura = findViewById(R.id.gura);
        final ImageButton dul = findViewById(R.id.dul);
        final ImageButton lewo = findViewById(R.id.lewo);
        final ImageButton prawo = findViewById(R.id.prawo);

        ekran.getHolder().addCallback(this);

        gura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!kierunek.equals("dul")){
                    kierunek = "gura";
                }
            }
        });
        dul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!kierunek.equals("gura")){
                    kierunek = "dul";
                }
            }
        });
        lewo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!kierunek.equals("prawo")){
                    kierunek = "lewo";
                }
            }
        });
        prawo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!kierunek.equals("lewo")){
                    kierunek = "prawo";
                }
            }
        });
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;

        start();
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }
    private void start() {
        ilejapka.clear();

        scor.setText("0");

        wynik=0;
        kierunek="prawo";

        int pozycjastartX = (rozmiarskubanca) * dłskubanca;
        for(int i=0; i<dłskubanca;i++){
            Japka japko = new Japka(pozycjastartX, rozmiarskubanca);
            ilejapka.add(japko);

            pozycjastartX = pozycjastartX - (rozmiarskubanca * 2);
        }

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
                        ilejapka.get(0).setPozycjaY(headposY - (rozmiarskubanca * 2 ));
                        break;
                    case "dul":
                        ilejapka.get(0).setPozycjaX(headposX);
                        ilejapka.get(0).setPozycjaY(headposY + (rozmiarskubanca * 2 ));
                        break;
                }

                if(czyprzegrana(headposX,headposY)){
                    //jak przegrał, zatrzymajgre
                    timer.purge();
                    timer.cancel();

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setMessage("twój wynik to: "+ scor);
                    builder.setTitle("PRZEGRAŁEŚ!");
                    builder.setCancelable(false);
                    builder.setPositiveButton("zcznij od nowa", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //odnów gre :fire:
                            start();
                        }
                    });
                    //nowy timer sie aktywuje w tle więc pokaż
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            builder.show();
                        }
                    });
                }
                else {
                    //łączy surfaceholder z canvas by na nim rysować
                    canvas = surfaceHolder.lockCanvas();
                    canvas.drawColor(Color.WHITE, PorterDuff.Mode.CLEAR);
                    //rusza głową wensza
                    canvas.drawCircle(ilejapka.get(0).getPozycjaX(), ilejapka.get(0).getPozycjaY(), rozmiarskubanca, namaluj());
                    //generuje japko
                    canvas.drawCircle(PozycjaX,PozycjaY,rozmiarskubanca, namaluj());
                    // reszta punktów podąrza
                    for(int i=1;i<ilejapka.size();i++){
                        int pozycjatymczasX = ilejapka.get(i).getPozycjaX();
                        int pozycjatymczasY = ilejapka.get(i).getPozycjaY();
                        //rusza punktami
                        ilejapka.get(i).setPozycjaX(headposX);
                        ilejapka.get(i).setPozycjaY(headposY);
                        canvas.drawCircle(ilejapka.get(i).getPozycjaX(), ilejapka.get(i).getPozycjaY(), rozmiarskubanca,namaluj());

                        //zmienia pozycje głowy
                        headposX = pozycjatymczasX;
                        headposY = pozycjatymczasY;
                    }
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }

            }
        }, 1000 -prędkosc, 1000-prędkosc);
    }
    private void urośnijwensza(){
        //generuje nowy punkt
        Japka punkty = new Japka(0, 0);
        //dodaje ten punkt do węża
        ilejapka.add(punkty);
        //zwiększa wynik
        wynik++;
        //dodaj wynik do textview wyniku
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                scor.setText(String.valueOf(wynik));
            }
        });
    }
    private boolean czyprzegrana(int headposX, int headposY){
        boolean gameOver = false;

        if(ilejapka.get(0).getPozycjaX()<0 ||
                ilejapka.get(0).getPozycjaY()<0 ||
                ilejapka.get(0).getPozycjaX()>= ekran.getWidth() ||
                ilejapka.get(0).getPozycjaY()>= ekran.getHeight())
        {
            gameOver=true;
        }
        for(int i=1; i<ilejapka.size();i++){

            if (headposX == ilejapka.get(i).getPozycjaX() &&
                    headposY == ilejapka.get(i).getPozycjaY()){
                gameOver=true;
                break;
            }
        }
        return gameOver;
    }
    private Paint namaluj(){

        if (kolorjapko==null) {
            kolorjapko = new Paint();
            kolorjapko.setColor(kolorjapek);
            kolorjapko.setStyle(Paint.Style.FILL);
            kolorjapko.setAntiAlias(true); //daje płynności
        }
            return kolorjapko;
    }
}