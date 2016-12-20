package com.example.max.trabalhoes2.Interface.Util;

import android.content.Context;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SalvarCarregarUtil {
    private Context context;

    public SalvarCarregarUtil(Context context){
        this.context = context;
    }

    public void salvarJogo(String nomeSave, int turno, int jogadorAtual, int peca1, int peca2,
                           String tabuleiro, int modo, int bot1, int bot2) throws IOException {
        File path = context.getFilesDir();

        File tabuleiroFile = new File(path, nomeSave + "_tabuleiro.txt");
        File configFile = new File(path, nomeSave + "_config.txt");

        String config = String.valueOf(turno) + "!" + String.valueOf(jogadorAtual) + "!" +
                String.valueOf(peca1) + "!" + String.valueOf(peca2) + "!" + String.valueOf(modo) +
                "!" + String.valueOf(bot1) + "!" + String.valueOf(bot2);

        FileOutputStream stream = new FileOutputStream(tabuleiroFile);
        FileOutputStream stream2 = new FileOutputStream(configFile);

        try {
            stream.write(tabuleiro.getBytes());
            stream2.write(config.getBytes());
        } finally {
            stream.close();
            stream2.close();
        }
    }

    public JogoSalvo carregarJogo(String nomeSave) throws IOException {
        File path = context.getFilesDir();

        File tabuleiroFile = new File(path, nomeSave + "_tabuleiro.txt");
        File configFile = new File(path, nomeSave + "_config.txt");

        if(!tabuleiroFile.exists() || !configFile.exists()){
            return null;
        }

        int length = (int) configFile.length();

        byte[] bytes = new byte[length];

        FileInputStream in = new FileInputStream(configFile);

        InputStream inputStream = new FileInputStream(tabuleiroFile);
        try {
            in.read(bytes);
        } finally {
            in.close();
        }

        String contents = new String(bytes);

        String[] vetor = contents.split("!");

        JogoSalvo jogoSalvo = new JogoSalvo();
        jogoSalvo.setNome(nomeSave);
        jogoSalvo.setTurno(Integer.valueOf(vetor[0]));
        jogoSalvo.setJogadorAtual(Integer.valueOf(vetor[1]));
        jogoSalvo.setPeca1(Integer.valueOf(vetor[2]));
        jogoSalvo.setPeca2(Integer.valueOf(vetor[3]));
        jogoSalvo.setModo(Integer.valueOf(vetor[4]));
        jogoSalvo.setInputStream(inputStream);

        return jogoSalvo;
    }

    public List<JogoSalvo> getAllSaves() throws IOException {
        List<JogoSalvo> saves = new ArrayList<>();
        File dirFiles = context.getFilesDir();
        for (String strFile : dirFiles.list()) {
            if(!strFile.equals("instant-run") && strFile.contains("config")){
                saves.add(carregarJogo(getNomeSave(strFile)));
            }
        }

        return saves;
    }

    private String getNomeSave(String nomeArquivo){
        int index = nomeArquivo.indexOf("_");

        return nomeArquivo.substring(0, index);
    }

    public class JogoSalvo{
        private InputStream inputStream;
        private int turno;
        private int peca1;
        private int peca2;
        private int jogadorAtual;
        private int modo;
        private int bot1;
        private int bot2;
        private String nome;

        public JogoSalvo(){

        }

        public InputStream getInputStream() {
            return inputStream;
        }

        public void setInputStream(InputStream inputStream) {
            this.inputStream = inputStream;
        }

        public int getTurno() {
            return turno;
        }

        public void setTurno(int turno) {
            this.turno = turno;
        }

        public int getPeca1() {
            return peca1;
        }

        public void setPeca1(int peca1) {
            this.peca1 = peca1;
        }

        public int getPeca2() {
            return peca2;
        }

        public void setPeca2(int peca2) {
            this.peca2 = peca2;
        }

        public int getJogadorAtual() {
            return jogadorAtual;
        }

        public void setJogadorAtual(int jogadorAtual) {
            this.jogadorAtual = jogadorAtual;
        }

        public int getModo() {
            return modo;
        }

        public void setModo(int modo) {
            this.modo = modo;
        }

        public String getNome() {
            return nome;
        }

        public void setNome(String nome) {
            this.nome = nome;
        }

        public int getBot1() {
            return bot1;
        }

        public void setBot1(int bot1) {
            this.bot1 = bot1;
        }

        public int getBot2() {
            return bot2;
        }

        public void setBot2(int bot2) {
            this.bot2 = bot2;
        }

        @Override
        public String toString() {
            return "JogoSalvo{" +
                    "inputStream=" + inputStream +
                    ", turno=" + turno +
                    ", peca1=" + peca1 +
                    ", peca2=" + peca2 +
                    ", jogadorAtual=" + jogadorAtual +
                    ", modo=" + modo +
                    ", bot1=" + bot1 +
                    ", bot2=" + bot2 +
                    ", nome='" + nome + '\'' +
                    '}';
        }
    }
}
