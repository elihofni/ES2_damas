package com.example.max.trabalhoes2.Interface.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.max.trabalhoes2.Interface.Fragment.JogoSalvoFragment;
import com.example.max.trabalhoes2.R;

public class JogoSalvoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jogo_salvo);

        if(savedInstanceState == null) {
            JogoSalvoFragment jogoSalvoFragment = new JogoSalvoFragment();
            getSupportFragmentManager().beginTransaction().add(R.id.containerJogoSalvo, jogoSalvoFragment).commit();
        }
    }
}
