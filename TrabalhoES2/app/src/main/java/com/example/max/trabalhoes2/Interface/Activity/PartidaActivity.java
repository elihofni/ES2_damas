package com.example.max.trabalhoes2.Interface.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.example.max.trabalhoes2.Interface.Fragment.BotVsBotFragment;
import com.example.max.trabalhoes2.Interface.Fragment.ModoDeJogoFragment;
import com.example.max.trabalhoes2.Interface.Fragment.TabuleiroFragment;
import com.example.max.trabalhoes2.Interface.Fragment.TelaInicialFragment;
import com.example.max.trabalhoes2.R;

public class PartidaActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_partida);

        int modo = getIntent().getExtras().getInt("modo");

        if(savedInstanceState == null) {
            if(modo == ModoDeJogoFragment.IA_VS_IA){
                BotVsBotFragment botVsBotFragment = new BotVsBotFragment();
                botVsBotFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().add(R.id.containerPartida, botVsBotFragment).commit();
            }else {
                TabuleiroFragment tabuleiroFragment = new TabuleiroFragment();
                tabuleiroFragment.setArguments(getIntent().getExtras());
                getSupportFragmentManager().beginTransaction().add(R.id.containerPartida, tabuleiroFragment).commit();
            }
        }

        setUpToolBar("", R.id.toolbar1);
    }

    public void setUpToolBar(String titulo, int toolbarId) {
        Toolbar toolbar = (Toolbar) findViewById(toolbarId);

        if (toolbar != null) {
            setSupportActionBar(toolbar);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle(titulo);
        }
    }

    public void trocarTituloToolbar(String titulo){
        getSupportActionBar().setTitle(titulo);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.progressBar);

        if(!titulo.equals("")) {
            progressBar.setVisibility(View.VISIBLE);
        }else{
            progressBar.setVisibility(View.GONE);
        }
    }
}
