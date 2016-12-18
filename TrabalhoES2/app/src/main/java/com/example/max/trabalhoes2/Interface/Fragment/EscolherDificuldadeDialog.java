package com.example.max.trabalhoes2.Interface.Fragment;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.max.trabalhoes2.R;

import regradejogo.Bot;


public class EscolherDificuldadeDialog extends DialogFragment {
    private OnDificuldadeSelected onDificuldadeSelected;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setStyle(DialogFragment.STYLE_NO_TITLE, android.R.style.Theme_Holo_Light_Dialog);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dialog_escolher_dificuldade, container, false);

        TextView facil = (TextView) view.findViewById(R.id.facil);
        TextView medio = (TextView) view.findViewById(R.id.medio);
        TextView dificil = (TextView) view.findViewById(R.id.dificil);

        facil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onDificuldadeSelected != null){
                    onDificuldadeSelected.dificuldadeSelected(0);
                    dismiss();
                }
            }
        });

        medio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onDificuldadeSelected != null){
                    onDificuldadeSelected.dificuldadeSelected(1);
                    dismiss();
                }
            }
        });

        dificil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onDificuldadeSelected != null){
                    onDificuldadeSelected.dificuldadeSelected(2);
                    dismiss();
                }
            }
        });

        return view;
    }

    public interface OnDificuldadeSelected{
        public void dificuldadeSelected(int dificuldade);
    }

    public void setOnDificuldadeSelected(OnDificuldadeSelected onDificuldadeSelected) {
        this.onDificuldadeSelected = onDificuldadeSelected;
    }
}
