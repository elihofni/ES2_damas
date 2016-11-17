package regradejogo;

import java.io.IOException;
import java.util.List;

public class Main {

    public static void main(String[] args) throws IOException {
        Regras regras = new Regras("teste2.txt");
        
        System.out.println(regras.getTabuleiro().toString());
        
        //regras.moverPeça(new Posição(4, 2), new Posição(7, 1));
        Peça peça = regras.getPeça(new Posição(4, 2));
        System.out.println(regras.jogadasPossiveis(peça).toString());
        
        //System.out.println(regras.getTabuleiro().toString());
        
        //List<Jogada> jogadas = regras.jogadasPossiveis(peca);
        //System.out.println(jogadas.toString());
    }
    
}