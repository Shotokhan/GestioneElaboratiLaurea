package test;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import control.GestioneElaborati;
import main.DataDefinition;

public class TestRichiestaAssegnazioneElaborato {

	InputStream sysInBackup;
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		DataDefinition.createTables();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
		DataDefinition.dropTables();
	}

	@Before
	public void setUp() throws Exception {
		sysInBackup = System.in;
	}

	@After
	public void tearDown() throws Exception {
		System.setIn(sysInBackup);
	}
	
	@Test
	public void test() throws IOException {
		GestioneElaborati gestioneElaborati = new GestioneElaborati();
		
		String inputString = "R\nR\nA\n";
		InputStream testInput = new ByteArrayInputStream(inputString.getBytes());
		System.setIn(testInput);
		
		
		assertEquals(1, 1);
	}

}
