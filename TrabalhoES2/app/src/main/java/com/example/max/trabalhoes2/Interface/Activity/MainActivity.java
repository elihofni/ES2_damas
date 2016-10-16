package com.example.max.trabalhoes2.Interface.Activity;

import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.max.trabalhoes2.Interface.Fragment.TabuleiroFragment;
import com.example.max.trabalhoes2.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState == null) {
            TabuleiroFragment tabuleiroFragment = new TabuleiroFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.container_main, tabuleiroFragment).commit();
        }
    }
}