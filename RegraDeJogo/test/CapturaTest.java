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
public class CapturaTest {
    
    private Regras regras;
    private Peça peça;
    private List<Peça> capturas;
    private List<Peça> oraculo;
    
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
        regras = new Regras();
        oraculo = new ArrayList<>();
    }
    
    @After
    public void tearDown() {
    }

    // TODO add test methods here.
    // The methods must be annotated with annotation @Test. For example:
    //
    // @Test
    // public void hello() {}
    
    @Test
    public void captura(){
        
    }
}
