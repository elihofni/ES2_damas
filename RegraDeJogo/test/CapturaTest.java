/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

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

/**
 *
 * @author Max
 */
public class CapturaTest {
    
    private Regras regras;
    private Peça peça;
    private List<Posição> resultado;
    private List<Posição> oraculo;
    
    public CapturaTest() {
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
    
    @Test
    public void capturaSequencia() throws IOException{
        /**
         * Cenário 1.
         */
        regras = new Regras("./testesCaptura/testeCaptura7.txt");
        //oraculo = new ArrayList<>();
        //oraculo.add(new Posição(5, 0));
        peça = regras.getPeça(new Posição(7, 2));
        //resultado = getPosiçoes(regras.jogadasPossiveis(peça));
        //assertEquals(oraculo.toString(), resultado.toString());
        
        System.out.println(getPosiçoes(regras.capturasSeguidas(new ArrayList<>(), regras.jogadasPossiveis(peça).get(0), peça.getTime(), new ArrayList<>())).toString());
    }
}
