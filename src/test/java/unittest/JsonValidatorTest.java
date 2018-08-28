package unittest;

import de.nitschmann.tefdnn.application.NeuralNetwork;
import de.nitschmann.tefdnn.persistence.Database;
import de.nitschmann.tefdnn.presentation.Console;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;

@RunWith(Parameterized.class)
public class JsonValidatorTest {

    private String initString;
    private NeuralNetwork expectedResult;
    private Database db;
    private Console c;

    public JsonValidatorTest(String initString, NeuralNetwork expectedResult) {
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
        String str1 = "init -json: " + Paths.get("test/invalidJson/initialization/initializationObjectMissing.json").toAbsolutePath().toString();
        String str2 = "init -json: " + Paths.get("test/invalidJson/initialization/networkNameMissing.json").toAbsolutePath().toString();
        String str3 = "init -json: " + Paths.get("test/invalidJson/initialization/neuronCountBelow0.json").toAbsolutePath().toString();
        String str4 = "init -json: " + Paths.get("test/invalidJson/initialization/neuronCountMissing.json").toAbsolutePath().toString();
        String str5 = "init -json: " + Paths.get("test/invalidJson/configuration/activationFunctionMissing.json").toAbsolutePath().toString();
        String str6 = "init -json: " + Paths.get("test/invalidJson/configuration/activationFunctionNotBetween1and3.json").toAbsolutePath().toString();
        String str7 = "init -json: " + Paths.get("test/invalidJson/configuration/configurationObjectMissing.json").toAbsolutePath().toString();
        String str8 = "init -json: " + Paths.get("test/invalidJson/configuration/epochsBelow0.json").toAbsolutePath().toString();
        String str9 = "init -json: " + Paths.get("test/invalidJson/configuration/epochsMissing.json").toAbsolutePath().toString();
        String str10 = "init -json: " + Paths.get("test/invalidJson/configuration/learningRateBelow0.json").toAbsolutePath().toString();
        String str11 = "init -json: " + Paths.get("test/invalidJson/configuration/learningRateMissing.json").toAbsolutePath().toString();
        String str12 = "init -json: " + Paths.get("test/invalidJson/configuration/momentumBelow0.json").toAbsolutePath().toString();
        String str13 = "init -json: " + Paths.get("test/invalidJson/configuration/momentumMissing.json").toAbsolutePath().toString();
        String str14 = "init -json: " + Paths.get("test/invalidJson/configuration/targetLossBelow0.json").toAbsolutePath().toString();
        String str15 = "init -json: " + Paths.get("test/invalidJson/configuration/targetLossMissing.json").toAbsolutePath().toString();
        String str16 = "init -json: " + Paths.get("test/invalidJson/configuration/trainingTypeMissing.json").toAbsolutePath().toString();
        String str17 = "init -json: " + Paths.get("test/invalidJson/configuration/trainingTypeNot1.json").toAbsolutePath().toString();
        String str18 = "init -json: " + Paths.get("test/invalidJson/training/nameMissing.json").toAbsolutePath().toString();
        String str19 = "init -json: " + Paths.get("test/invalidJson/training/pathToDirectoryMissing.json").toAbsolutePath().toString();
        String str20 = "init -json: " + Paths.get("test/invalidJson/training/targetNeuronBelow0.json").toAbsolutePath().toString();
        String str21 = "init -json: " + Paths.get("test/invalidJson/training/targetNeuronMissing.json").toAbsolutePath().toString();
        String str22 = "init -json: " + Paths.get("test/invalidJson/training/trainingDataObjectMissing.json").toAbsolutePath().toString();

        return Arrays.asList(new Object[][] {
                {str1, null},
                {str2, null},
                {str3, null},
                {str4, null},
                {str5, null},
                {str6, null},
                {str7, null},
                {str8, null},
                {str9, null},
                {str10, null},
                {str11, null},
                {str12, null},
                {str13, null},
                {str14, null},
                {str15, null},
                {str16, null},
                {str17, null},
                {str18, null},
                {str19, null},
                {str20, null},
                {str21, null},
                {str22, null},
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
