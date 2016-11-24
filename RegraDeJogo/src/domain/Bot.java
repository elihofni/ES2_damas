/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package domain;

import java.util.List;
import regradejogo.Jogada;
import regradejogo.Peca;
import regradejogo.Peça;
import regradejogo.Regras;

/**
 *
 * @author Max
 */
public class Bot extends Jogador {

    private int numero_iteracoes;
    Jogada proximaJogada;
    
    public Bot(Regras regras, Dificuldade dificuldade, int time) {

        super(regras,time);
        
        
        switch(dificuldade){
            case FACIL:
                numero_iteracoes = 1;
                break;
            case MEDIO:
                numero_iteracoes = 4;
                break;
            case DIFICIL:
                numero_iteracoes = 10;
                break;
        }
    }

    public enum Dificuldade {

        FACIL, MEDIO, DIFICIL

    }
    public void Jogar() {
        Regras regra_auxiliar;
        proximaJogada = null;
        regra_auxiliar = regras.copia();
        minMax(regra_auxiliar, Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
        System.out.println("Bot joga:" + proximaJogada.toString());
        regras.moverPeca(proximaJogada.getPosInicial(), proximaJogada.getPosFinal());

    }

    public int heuristica(Regras regra) {
        if(time == Regras.JOGADOR_UM){
            if (regra.getJogadorAtual() == Regras.JOGADOR_UM) {
                return regra.getnPecasJogador1() - regra.getnPecasJogador2();
            } else {
                return regra.getnPecasJogador2() - regra.getnPecasJogador1();
            }
        }
        else{
            if (regra.getJogadorAtual() == Regras.JOGADOR_DOIS) {
                return regra.getnPecasJogador2() - regra.getnPecasJogador1();                
            } else {
                return regra.getnPecasJogador1() - regra.getnPecasJogador2();
            }
            
        }
    }
    
//    private Jogada escolherMelhorJogada(Regras regra){
//        Jogada jogadaEscolhida = null;
//        
//            Regras regra_auxiliar;
//            boolean jogou = false;
//            int minMax;
//            int minMaxAtual = 0;
//            int turn;
//            //int casa_candidata = CASA_INICIAL_IA;
//            //int[] tab = tabuleiro.getTabuleiro();
//            
//            List<Peça> peçasAptas = regra.getPeçasAptasDoJogadorAtual();
//                for (Peça peça : peçasAptas) {
//                    List<Jogada> jogadasPossiveis = regra.jogadasPossiveis(peça);
//                    for (Jogada jogada : jogadasPossiveis) {
//                        regra_auxiliar = regra.copia();
//                        try{
//                            regra_auxiliar.moverPeça(jogada.getPosInicial(), jogada.getPosFinal());
//                        }catch(Exception e){
//                            System.out.println(e);
//                            System.out.println(regra.getTabuleiro().toString());
//                        }
//                        minMax = minMax(regra_auxiliar, Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
//                        if ((minMax >= minMaxAtual)) {
//                            minMaxAtual = minMax;
//                            //casa_candidata = casa;
//                            //jogou = true;
//                            jogadaEscolhida = jogada;
//                        }
//                        
//                    }
//                }           
//        
//        
//        return jogadaEscolhida;
//    }
    
    
    private int minMax(Regras regra, int alpha, int beta, int iteracao) {
        if ((iteracao == numero_iteracoes) || regra.isJogoFinalizado()) {
            return heuristica(regra);
        } else {            
            Regras regra_auxiliar;
            int minMax;
            if (regra.getJogadorAtual() == time) {
                Jogada jogadaCandidata = null;
                List<Peca> pecasAptas = regra.getPeçasAptasDoJogadorAtual();
                for (Peca peca : pecasAptas) {
                    List<Jogada> jogadasPossiveis = regra.jogadasPossiveis(peca);
                    for (Jogada jogada : jogadasPossiveis) {                        
                        regra_auxiliar = regra.copia();
                        try{
                            regra_auxiliar.moverPeca(jogada.getPosInicial(), jogada.getPosFinal());
                        }catch(Exception e){
                            System.out.println(e);
                            System.out.println(jogada.toString());
                            System.out.println(regra.getTabuleiro().toString());
                        }
                        minMax = minMax(regra_auxiliar, alpha, beta, iteracao + 1);
                        alpha = Math.max(alpha, minMax);                        
                        jogadaCandidata = jogada;
                        if (alpha >= beta) {                              
                            if( iteracao == 1 ){
                                proximaJogada = jogada;
                            }
                            return alpha;
                            
                        }                        
                    }
                }
                if( iteracao == 1 ){
                                proximaJogada = jogadaCandidata;
                            }
                return alpha;
            } else {
                List<Peca> pecasAptas = regra.getPeçasAptasDoJogadorAtual();
                for (Peca peca : pecasAptas) {
                    List<Jogada> jogadasPossiveis = regra.jogadasPossiveis(peca);
                    for (Jogada jogada : jogadasPossiveis) {
                        regra_auxiliar = regra.copia();
                        try{
                            regra_auxiliar.moverPeca(jogada.getPosInicial(), jogada.getPosFinal());
                        }catch(Exception e){
                            System.out.println(e);
                            System.out.println(jogada.toString());
                            System.out.println(regra.getTabuleiro().toString());
                        }
                        minMax = minMax(regra_auxiliar, alpha, beta, iteracao + 1);
                        beta = Math.min(alpha, minMax);
                        if (alpha >= beta) {
                            return beta;
                        }
                    }

                }
                return beta;
            }
        }
    }

}