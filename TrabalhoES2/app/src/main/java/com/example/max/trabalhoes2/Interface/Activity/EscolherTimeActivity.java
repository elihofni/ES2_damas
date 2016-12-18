package com.example.max.trabalhoes2.Interface.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.max.trabalhoes2.Interface.Fragment.EscolherTimeFragment;
import com.example.max.trabalhoes2.R;

public class EscolherTimeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escolher_time);

        if(savedInstanceState == null) {
            //TelaInicialFragment telaInicialFragment = new TelaInicialFragment();
            EscolherTimeFragment escolherTimeFragment = new EscolherTimeFragment();
            escolherTimeFragment.setArguments(getIntent().getExtras());
            getSupportFragmentManager().beginTransaction().add(R.id.container_EscolherTime, escolherTimeFragment).commit();
        }
    }
}
