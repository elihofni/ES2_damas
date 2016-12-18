package com.example.max.trabalhoes2.Interface.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.max.trabalhoes2.Interface.Fragment.TabuleiroFragment;
import com.example.max.trabalhoes2.Interface.Fragment.TelaInicialFragment;
import com.example.max.trabalhoes2.R;

public class PartidaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partida);

        if(savedInstanceState == null) {
            TabuleiroFragment tabuleiroFragment = new TabuleiroFragment();
            tabuleiroFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(R.id.containerPartida, tabuleiroFragment).commit();
        }
    }
}
