package com.example.max.trabalhoes2.Interface.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.max.trabalhoes2.Interface.Fragment.*;
import com.example.max.trabalhoes2.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null) {
            //TabuleiroFragment2 tabuleiroFragment = new TabuleiroFragment2();
            //TabuleiroFragment tabuleiroFragment = new TabuleiroFragment();
            TelaInicialFragment telaInicialFragment = new TelaInicialFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.container_main, telaInicialFragment).commit();
        }
    }
}