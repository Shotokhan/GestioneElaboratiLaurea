package test;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import main.DataDefinition;

public class TestRichiestaAssegnazioneElaborato {

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
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void test() {
		fail("Non ancora implementato");
	}

}
