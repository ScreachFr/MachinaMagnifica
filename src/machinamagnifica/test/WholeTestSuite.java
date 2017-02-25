package machinamagnifica.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite.SuiteClasses;
import org.junit.runners.Suite;

@RunWith(value=Suite.class)
@SuiteClasses(value={
		machinamagnifica.test.PlateauDeSableTest.class,
		machinamagnifica.test.MemoryTest.class
})
public class WholeTestSuite {}
