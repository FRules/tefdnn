package de.nitschmann.tefdnn.test.unittest;

import de.nitschmann.tefdnn.application.TrainingEnvironment;
import de.nitschmann.tefdnn.persistence.Database;
import de.nitschmann.tefdnn.presentation.Console;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class ConsoleTestInit {

    private String initString;
    private TrainingEnvironment expectedResult;
    private Database db;
    private Console c;

    public ConsoleTestInit(String initString, TrainingEnvironment expectedResult) {
        this.initString = initString;
        this.expectedResult = expectedResult;
    }

    @Before
    public void initDatabase() {
        db = new Database("jdbc:hsqldb:file:db/database; shutdown=true", "SA", "" );
        db.dropTables();
        db.initDatabase();
        c = new Console(db);
    }

    @After
    public void closeDatabase() {
        db.close();
    }

    @Parameterized.Parameters
    public static Collection initStrings() {
        return Arrays.asList(new Object[][] {
                {"init     -n:Name       -cIN:5   -cHN: 5 -cON:   5 -cHL:   3   -cHNAE: 3", new TrainingEnvironment()},
                {"init     -n:Name        -cIND:5   -cHN: 5 -cON:   5 -cHL:   3   -cHNAE: 3", null},
                {"init    -cHN:  5 -cON:   5 -cIN:5   -cHL:3   -cHNAE:   3", new TrainingEnvironment()},
                {"init    cHN:  5 -cON:   5 -cIN:5   -cHL:3   -cHNAE:   3", null},
                {"init -nF: NameFeedforwardNetwork   -nAE: NameAutoencoderNetzwerkBla     ", null},
                {"init -nS: notexistingenv", null},
                {"init -nFF: notexistingff -nAE: notexistingae", null}

        });
    }

    @Test
    public void testInitialization() {
        System.out.println("String: " + initString);
        initString = initString.toLowerCase();
        if (expectedResult == null) {
            Assert.assertNull(c.init(initString));
        } else {
            Assert.assertNotNull(c.init(initString));
        }
    }

}
