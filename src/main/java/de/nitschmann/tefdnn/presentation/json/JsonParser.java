package de.nitschmann.tefdnn.presentation.json;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.nitschmann.tefdnn.presentation.json.mapper.JsonConfig;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class JsonParser {

    private boolean startTrainingImmediately;
    private boolean openGuiAfterTrainingIsCompleted;
    private String loadString;
    private String configString;
    private String[] trainStrings;

    public void parse(String filename) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        byte[] jsonData = Files.readAllBytes(Paths.get(filename));

        JsonConfig config = objectMapper.readValue(jsonData, JsonConfig.class);

        generateLoadString(config);
        generateConfigString(config);
        generateTrainStrings(config);

        this.startTrainingImmediately = config.getStartTrainingImmediately();
        this.openGuiAfterTrainingIsCompleted = config.getOpenGuiAfterTrainingIsCompleted();
    }

    public JsonConfig getJsonConfig(String filename) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();

        byte[] jsonData = Files.readAllBytes(Paths.get(filename));

        return objectMapper.readValue(jsonData, JsonConfig.class);
    }

    private void generateLoadString(JsonConfig jsonConfig) {
        this.loadString = "init -n: " +
                jsonConfig.getInitialization().getName() +
                " -cin: " +
                jsonConfig.getInitialization().getCountOfInputNeurons() +
                " -chn: " +
                jsonConfig.getInitialization().getCountOfHiddenNeurons() +
                " -con: " +
                jsonConfig.getInitialization().getCountOfOutputNeurons() +
                " -chl: " +
                jsonConfig.getInitialization().getCountOfHiddenLayers();
    }

    private void generateConfigString(JsonConfig jsonConfig) {
        this.configString = " -tt: " +
                jsonConfig.getConfiguration().getTrainingType() +
                " -lr: " +
                jsonConfig.getConfiguration().getLearningRate() +
                " -mom: " +
                jsonConfig.getConfiguration().getMomentum() +
                " -af: " +
                jsonConfig.getConfiguration().getActivationFunction() +
                " -me: " +
                jsonConfig.getConfiguration().getMaximumNumberOfEpochs() +
                " -tl: " +
                jsonConfig.getConfiguration().getTargetLoss();
    }

    private void generateTrainStrings(JsonConfig jsonConfig) {
        this.trainStrings = new String[jsonConfig.getTrainingData().size()];
        for(int i = 0; i < jsonConfig.getTrainingData().size(); i++) {
            String sb = "train -ptd: " +
                    jsonConfig.getTrainingData().get(i).getPathToDirectory() +
                    " -tn: " +
                    jsonConfig.getTrainingData().get(i).getTargetNeuron() +
                    " -n: " +
                    jsonConfig.getTrainingData().get(i).getName();
            this.trainStrings[i] = sb;
        }
    }

    public boolean getOpenGuiAfterTrainingIsCompleted() {
        return this.openGuiAfterTrainingIsCompleted;
    }

    public boolean getStartTrainingImmediately() {
        return this.startTrainingImmediately;
    }

    public String getLoadString() {
        return this.loadString;
    }

    public String getConfigString() {
        return this.configString;
    }

    public String[] getTrainStrings() {
        return this.trainStrings;
    }


}
