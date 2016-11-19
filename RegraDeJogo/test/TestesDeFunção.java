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
import regradejogo.Peça;
import regradejogo.Posição;
import regradejogo.Regras;

/**
 *
 * @author Max
 */
public class TestesDeFunção {
    
    public TestesDeFunção() {
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
    
    public List<Posição> getPosicoesPecas(List<Peça> pecas, Regras regra){
        List<Posição> posicoes = new ArrayList<>();
        
        for(Peça peça : pecas){
            posicoes.add(regra.getTabuleiro().getPosição(peça));
        }
        
        return posicoes; 
    }
    
    @Test
    public void testGetPeçasTabuleiro(){
        /**
         * Caso 1
         */
        Regras regras = new Regras();
        List<Posição> oraculo = new ArrayList<>();
        oraculo.add(new Posição(0, 1));
        oraculo.add(new Posição(0, 3));
        oraculo.add(new Posição(0, 5));
        oraculo.add(new Posição(0, 7));
        oraculo.add(new Posição(1, 0));
        oraculo.add(new Posição(1, 2));
        oraculo.add(new Posição(1, 4));
        oraculo.add(new Posição(1, 6));
        oraculo.add(new Posição(2, 1));
        oraculo.add(new Posição(2, 3));
        oraculo.add(new Posição(2, 5));
        oraculo.add(new Posição(2, 7));
        oraculo.add(new Posição(5, 0));
        oraculo.add(new Posição(5, 2));
        oraculo.add(new Posição(5, 4));
        oraculo.add(new Posição(5, 6));
        oraculo.add(new Posição(6, 1));
        oraculo.add(new Posição(6, 3));
        oraculo.add(new Posição(6, 5));
        oraculo.add(new Posição(6, 7));
        oraculo.add(new Posição(7, 0));
        oraculo.add(new Posição(7, 2));
        oraculo.add(new Posição(7, 4));
        oraculo.add(new Posição(7, 6));
        
        List<Posição> resultado = getPosicoesPecas(regras.getTabuleiro().getPeças(), regras);
        assertEquals(oraculo.toString(), resultado.toString());
    }
    
    @Test
    public void peçasAptas(){
        /**
         * Caso 1.
         */
        Regras regras = new Regras();
        
        List<Posição> oraculo = new ArrayList<>();
        oraculo.add(new Posição(2, 1));
        oraculo.add(new Posição(2, 3));
        oraculo.add(new Posição(2, 5));
        oraculo.add(new Posição(2, 7));
        oraculo.add(new Posição(5, 0));
        oraculo.add(new Posição(5, 2));
        oraculo.add(new Posição(5, 4));
        oraculo.add(new Posição(5, 6));
        
        List<Posição> resultado = getPosicoesPecas(regras.getPeçasAptas(), regras);
        
        assertEquals(oraculo.toString(), resultado.toString());
    }
    
}
