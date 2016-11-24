package regradejogo;

import domain.Jogador;
import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        Regras regras = new Regras();
        
        Jogador jogador = new Jogador(regras);
        
        System.out.println("Jogador atual: " + regras.getJogadorAtual());
        System.out.println(regras.getTabuleiro().toString());
        
        jogador.realizarJogada(5, 0, 4, 1);
        
        System.out.println("Jogador atual: " + regras.getJogadorAtual());
        System.out.println(regras.getTabuleiro().toString());
        
        jogador.realizarJogada(4, 1, 3, 2);
        
        //regras.moverPeça(new Posição(4, 3), new Posição(6, 1));
        
        //System.out.println(regras.getTabuleiro().toString());
        
        //regras.moverPeça(new Posição(6, 1), new Posição(7, 0));
        //Peca peça = regras.getPeca(new Posicao(4, 2));
        //System.out.println(regras.jogadasPossiveis(peça).toString());
        
        //System.out.println(regras.getTabuleiro().toString());
        
        //Peça peça = regras.getPeça(new Posição(5, 2));
        //System.out.println(regras.jogadasPossiveis(peça).toString());
        
        //List<Jogada> jogadas = regras.jogadasPossiveis(peca);
        //System.out.println(jogadas.toString());
    }
    
}
