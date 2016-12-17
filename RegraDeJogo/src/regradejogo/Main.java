package regradejogo;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        Regras regras = new Regras("teste5.txt");

        //jogarHumanoVsBot(regras);
        //jogarHumanoVsHumano(regras);
        Jogador jogador = new Humano(regras, Regras.JOGADOR_UM);
        System.out.println(jogador.getJogadasPossiveis(new Posicao(0, 1)).toString());
        //System.out.println(regras.jogadasPossiveisDama(regras.getTabuleiro().getPeca(new Posicao(2, 6))).toString());

        //System.out.println(regras.getTabuleiro().toString());
        //regras.moverPeca(new Posicao(5, 2), new Posicao(4, 3));
        //regras.moverPeca(new Posicao(2, 1), new Posicao(3, 2));
        //System.out.println(regras.getTabuleiro().toString());
        //System.out.println(regras.getPecasAptasParaCapturaDoJogadorAtual().toString());
        //System.out.println(regras.getJogadorAtual());
    }

    public static void jogarHumanoVsHumano(Regras regras) {
        Humano humanoPl1 = new Humano(regras, Regras.JOGADOR_UM);
        Humano humanoPl2 = new Humano(regras, Regras.JOGADOR_DOIS);

        while (!regras.isJogoFinalizado()) {
            System.out.println(regras.getTabuleiro().toString());
            if (regras.getJogadorAtual() == humanoPl1.getTime()) {
                humanoJoga(humanoPl1, regras);
            } else {
                humanoJoga(humanoPl2, regras);
            }
        }
    }

    private static void humanoJoga(Humano humanoPl, Regras regras) {
        Scanner in = new Scanner(System.in);
        System.out.println("vez do jogador " +humanoPl.getTime()+": digite a posição da peça separada por ; ");
        String posPeca = in.next();
        String[] strPos = posPeca.split(";");
        int xPeca = Integer.parseInt(strPos[0]);
        int yPeca = Integer.parseInt(strPos[1]);
        Peca peca = regras.getPeca(new Posicao(xPeca, yPeca));
        System.out.println("Sugestão de jogadas");
        System.out.println(humanoPl.getPosPossiveis(regras.getTabuleiro().getPosicao(peca)).toString());
        System.out.println("digite a posição de destino da peça separada por ; ");
        String posDestino = in.next();
        String[] strPosDestino = posDestino.split(";");
        int xDestino = Integer.parseInt(strPosDestino[0]);
        int yDestino = Integer.parseInt(strPosDestino[1]);
        try {
            humanoPl.realizarJogada(xPeca, yPeca, xDestino, yDestino);
        } catch (Exception e) {
            System.out.println(e);
        }
        
        //System.out.println(regras.getPecassssss().toString());
    }

    public static void jogarHumanoVsBot(Regras regras) {

        Bot botPl2 = new Bot(regras, Bot.Dificuldade.MEDIO, Regras.JOGADOR_DOIS);
        Humano humanoPl1 = new Humano(regras, Regras.JOGADOR_UM);

        while (!regras.isJogoFinalizado()) {
            System.out.println(regras.getTabuleiro().toString());
            if (regras.getJogadorAtual() == humanoPl1.getTime()) {
                humanoJoga(humanoPl1, regras);
            } else {
                botPl2.Jogar();
            }
        }

    }

}
