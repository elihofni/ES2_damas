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
import regradejogo.Peça;
import regradejogo.Posição;
import regradejogo.Regras;
import regradejogo.Tabuleiro;
import regradejogo.Tabuleiro.Inclinacao;

/**
 *
 * @author Max
 */
public class MovimentaçãoTest {
    private Regras regras;
    private List<Posição> jogadasOraculo;
    private Peça peça;
    private List<Posição> pos;
    
    public MovimentaçãoTest() {
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
    
    private List<Posição> getPosiçoes(List<Jogada> jogadas){
        List<Posição> posicoes = new ArrayList<>();
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
        jogadasOraculo.add(new Posição(6, 1));
        jogadasOraculo.add(new Posição(2, 5));
        peça = regras.getPeça(new Posição(4, 3));
        pos = getPosiçoes(regras.jogadasPossiveis(peça));
        assertEquals(jogadasOraculo.toString(), pos.toString());
        
        /**
         * Cenário 2.
         */
        regras = new Regras("./testesCaptura/testeCaptura2.txt");
        jogadasOraculo = new ArrayList<>();
        jogadasOraculo.add(new Posição(5, 0));
        peça = regras.getPeça(new Posição(7, 2));
        pos = getPosiçoes(regras.jogadasPossiveis(peça));
        assertEquals(jogadasOraculo.toString(), pos.toString());
        
        /**
         * Cenário 3.
         */
        regras = new Regras("./testesCaptura/testeCaptura3.txt");
        jogadasOraculo = new ArrayList<>();
        jogadasOraculo.add(new Posição(1, 5));
        jogadasOraculo.add(new Posição(5, 5));
        peça = regras.getPeça(new Posição(3, 3));
        pos = getPosiçoes(regras.jogadasPossiveis(peça));
        assertEquals(jogadasOraculo.toString(), pos.toString());
        
        /**
         * Cenário 4.
         */
        regras = new Regras("./testesCaptura/testeCaptura4.txt");
        jogadasOraculo = new ArrayList<>();
        jogadasOraculo.add(new Posição(4, 4));
        peça = regras.getPeça(new Posição(3, 3));
        pos = getPosiçoes(regras.jogadasPossiveis(peça));
        assertEquals(jogadasOraculo.toString(), pos.toString());
        
        /**
         * Cenário 5.
         */
        regras = new Regras("./testesCaptura/testeCaptura5.txt");
        jogadasOraculo = new ArrayList<>();
        jogadasOraculo.add(new Posição(1, 1));
        peça = regras.getPeça(new Posição(3, 3));
        pos = getPosiçoes(regras.jogadasPossiveis(peça));
        assertEquals(jogadasOraculo.toString(), pos.toString());
        
        /**
         * Cenário 6.
         */
        regras = new Regras("./testesCaptura/testeCaptura6.txt");
        jogadasOraculo = new ArrayList<>();
        jogadasOraculo.add(new Posição(5, 0));
        peça = regras.getPeça(new Posição(7, 2));
        pos = getPosiçoes(regras.jogadasPossiveis(peça));
        assertEquals(jogadasOraculo.toString(), pos.toString());
    }
    
    @Test
    public void jogadasPossiveisDama() throws IOException{
        regras = new Regras("./testesMovimentacaoDama/testeDama1.txt");
        
        peça = regras.getPeça(new Posição(3, 3));
        pos = getPosiçoes(regras.jogadasPossiveisDama(peça));
        
        //System.out.println(pos.toString());
        
        //System.out.println(regras.getTabuleiro().getDiagonal(new Posição(3, 3), -1, 1).toString());
        
        System.out.println(getPosiçoes(regras.jogadasPossiveisDama(peça)));
    }
}
