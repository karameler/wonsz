package com.example.wonsz;

import android.os.Bundle;
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

public class MainActivity extends AppCompatActivity implements SurfaceHolder.Callback {

    private Integer japka=0;
    //surfaceview to pole ekranu, surfaceholder sprawia że sie na nim rysuje
    private SurfaceView ekran;
    private SurfaceHolder surfaceHolder;
    private TextView scor;
    private ImageButton gura, dul, lewo, prawo;

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
    private void start(){
        japka=0;

        scor.setText("wynik: "+japka);
    }
}