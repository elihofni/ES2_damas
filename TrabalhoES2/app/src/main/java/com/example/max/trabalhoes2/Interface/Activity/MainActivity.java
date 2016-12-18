package com.example.max.trabalhoes2.Interface.Activity;

import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.max.trabalhoes2.Interface.Adapter.SwipeMain;
import com.example.max.trabalhoes2.Interface.Fragment.*;
import com.example.max.trabalhoes2.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null) {
            TelaInicialFragment telaInicialFragment = new TelaInicialFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.containerMain, telaInicialFragment).commit();
        }
    }
}