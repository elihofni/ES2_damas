import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import regradejogo.Jogada;
import regradejogo.Peca;
import regradejogo.Posicao;
import regradejogo.Regras;
import regradejogo.Tabuleiro;
import regradejogo.Tabuleiro.Inclinacao;

/**
 *
 * @author Max
 */
public class MovimentacaoTest {
    private Regras regras;
    private List<Posicao> jogadasOraculo;
    private Peca peca;
    private List<Posicao> pos;
    
    public MovimentacaoTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    private List<Posicao> getPosicoes(List<Jogada> jogadas){
        List<Posicao> posicoes = new ArrayList<>();
        for(Jogada jogada : jogadas){
            posicoes.add(jogada.getPosFinal());
        }
        
        return posicoes;
    }
    
    /**
     * Testa as jogadas possíveis envolvendo caputras.
     * @throws IOException 
     */
    @Test
    public void jogadasPossiveisTest() throws IOException{
        /**
         * Cenário 1.
         */
        regras = new Regras("./testesCaptura/testeCaptura1.txt");
        jogadasOraculo = new ArrayList<>();
        jogadasOraculo.add(new Posicao(6, 1));
        jogadasOraculo.add(new Posicao(2, 5));
        peca = regras.getPeca(new Posicao(4, 3));
        pos = getPosicoes(regras.jogadasPossiveis(peca));
        assertEquals(jogadasOraculo.toString(), pos.toString());
        
        /**
         * Cenário 2.
         */
        regras = new Regras("./testesCaptura/testeCaptura2.txt");
        jogadasOraculo = new ArrayList<>();
        jogadasOraculo.add(new Posicao(5, 0));
        peca = regras.getPeca(new Posicao(7, 2));
        pos = getPosicoes(regras.jogadasPossiveis(peca));
        assertEquals(jogadasOraculo.toString(), pos.toString());
        
        /**
         * Cenário 3.
         */
        regras = new Regras("./testesCaptura/testeCaptura3.txt");
        jogadasOraculo = new ArrayList<>();
        jogadasOraculo.add(new Posicao(1, 5));
        jogadasOraculo.add(new Posicao(5, 5));
        peca = regras.getPeca(new Posicao(3, 3));
        pos = getPosicoes(regras.jogadasPossiveis(peca));
        assertEquals(jogadasOraculo.toString(), pos.toString());
        
        /**
         * Cenário 4.
         */
        regras = new Regras("./testesCaptura/testeCaptura4.txt");
        jogadasOraculo = new ArrayList<>();
        jogadasOraculo.add(new Posicao(4, 4));
        peca = regras.getPeca(new Posicao(3, 3));
        pos = getPosicoes(regras.jogadasPossiveis(peca));
        assertEquals(jogadasOraculo.toString(), pos.toString());
        
        /**
         * Cenário 5.
         */
        regras = new Regras("./testesCaptura/testeCaptura5.txt");
        jogadasOraculo = new ArrayList<>();
        jogadasOraculo.add(new Posicao(1, 1));
        peca = regras.getPeca(new Posicao(3, 3));
        pos = getPosicoes(regras.jogadasPossiveis(peca));
        assertEquals(jogadasOraculo.toString(), pos.toString());
        
        /**
         * Cenário 6.
         */
        regras = new Regras("./testesCaptura/testeCaptura6.txt");
        jogadasOraculo = new ArrayList<>();
        jogadasOraculo.add(new Posicao(5, 0));
        peca = regras.getPeca(new Posicao(7, 2));
        pos = getPosicoes(regras.jogadasPossiveis(peca));
        assertEquals(jogadasOraculo.toString(), pos.toString());
    }
    
    @Test
    public void jogadasPossiveisDama() throws IOException{
        regras = new Regras("./testesMovimentacaoDama/testeDama1.txt");
        
        peca = regras.getPeca(new Posicao(3, 3));
        pos = getPosicoes(regras.jogadasPossiveisDama(peca));
        
        //System.out.println(pos.toString());
        
        //System.out.println(regras.getTabuleiro().getDiagonal(new Posição(3, 3), -1, 1).toString());
        
        System.out.println(getPosicoes(regras.jogadasPossiveisDama(peca)));
    }
}
