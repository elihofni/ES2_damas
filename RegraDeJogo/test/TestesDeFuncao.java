/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.util.ArrayList;
import java.util.List;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import regradejogo.Peca;
import regradejogo.Posicao;
import regradejogo.Regras;

/**
 *
 * @author Max
 */
public class TestesDeFuncao {
    
    public TestesDeFuncao() {
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
    
    public List<Posicao> getPosicoesPecas(List<Peca> pecas, Regras regra){
        List<Posicao> posicoes = new ArrayList<>();
        
        for(Peca peça : pecas){
            posicoes.add(regra.getTabuleiro().getPosicao(peça));
        }
        
        return posicoes; 
    }
    
    @Test
    public void testGetPeçasTabuleiro(){
        /**
         * Caso 1
         */
        Regras regras = new Regras();
        List<Posicao> oraculo = new ArrayList<>();
        oraculo.add(new Posicao(0, 1));
        oraculo.add(new Posicao(0, 3));
        oraculo.add(new Posicao(0, 5));
        oraculo.add(new Posicao(0, 7));
        oraculo.add(new Posicao(1, 0));
        oraculo.add(new Posicao(1, 2));
        oraculo.add(new Posicao(1, 4));
        oraculo.add(new Posicao(1, 6));
        oraculo.add(new Posicao(2, 1));
        oraculo.add(new Posicao(2, 3));
        oraculo.add(new Posicao(2, 5));
        oraculo.add(new Posicao(2, 7));
        oraculo.add(new Posicao(5, 0));
        oraculo.add(new Posicao(5, 2));
        oraculo.add(new Posicao(5, 4));
        oraculo.add(new Posicao(5, 6));
        oraculo.add(new Posicao(6, 1));
        oraculo.add(new Posicao(6, 3));
        oraculo.add(new Posicao(6, 5));
        oraculo.add(new Posicao(6, 7));
        oraculo.add(new Posicao(7, 0));
        oraculo.add(new Posicao(7, 2));
        oraculo.add(new Posicao(7, 4));
        oraculo.add(new Posicao(7, 6));
        
        List<Posicao> resultado = getPosicoesPecas(regras.getTabuleiro().getPecas(), regras);
        assertEquals(oraculo.toString(), resultado.toString());
    }
    
    @Test
    public void peçasAptas(){
        /**
         * Caso 1.
         */
        Regras regras = new Regras();
        
        List<Posicao> oraculo = new ArrayList<>();
        oraculo.add(new Posicao(2, 1));
        oraculo.add(new Posicao(2, 3));
        oraculo.add(new Posicao(2, 5));
        oraculo.add(new Posicao(2, 7));
        oraculo.add(new Posicao(5, 0));
        oraculo.add(new Posicao(5, 2));
        oraculo.add(new Posicao(5, 4));
        oraculo.add(new Posicao(5, 6));
        
        List<Posicao> resultado = getPosicoesPecas(regras.getPecasAptas(), regras);
        
        assertEquals(oraculo.toString(), resultado.toString());
    }
    
}
