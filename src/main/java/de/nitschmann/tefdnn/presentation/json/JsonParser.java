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

    private void generateLoadString(JsonConfig jsonConfig) {
        StringBuilder sb = new StringBuilder();
        sb.append("init -n: ");
        sb.append(jsonConfig.getInitialization().getName());
        sb.append(" -cin: ");
        sb.append(jsonConfig.getInitialization().getCountOfInputNeurons());
        sb.append(" -chn: ");
        sb.append(jsonConfig.getInitialization().getCountOfHiddenNeurons());
        sb.append(" -con: ");
        sb.append(jsonConfig.getInitialization().getCountOfOutputNeurons());
        sb.append(" -chl: ");
        sb.append(jsonConfig.getInitialization().getCountOfHiddenLayers());
        this.loadString = sb.toString();
    }

    private void generateConfigString(JsonConfig jsonConfig) {
        StringBuilder sb = new StringBuilder();
        sb.append("conf -nff");
        sb.append(" -tt: ");
        sb.append(jsonConfig.getConfiguration().getTrainingType());
        sb.append(" -lr: ");
        sb.append(jsonConfig.getConfiguration().getLearningRate());
        sb.append(" -mom: ");
        sb.append(jsonConfig.getConfiguration().getMomentum());
        sb.append(" -af: ");
        sb.append(jsonConfig.getConfiguration().getActivationFunction());
        sb.append(" -me: ");
        sb.append(jsonConfig.getConfiguration().getMaximumNumberOfEpochs());
        sb.append(" -tl: ");
        sb.append(jsonConfig.getConfiguration().getTargetLoss());
        this.configString = sb.toString();
    }

    private void generateTrainStrings(JsonConfig jsonConfig) {
        this.trainStrings = new String[jsonConfig.getTrainingData().size()];
        for(int i = 0; i < jsonConfig.getTrainingData().size(); i++) {
            StringBuilder sb = new StringBuilder();
            sb.append("train -ptd: ");
            sb.append(jsonConfig.getTrainingData().get(i).getPathToDirectory());
            sb.append(" -tn: ");
            sb.append(jsonConfig.getTrainingData().get(i).getTargetNeuron());
            sb.append(" -n: ");
            sb.append(jsonConfig.getTrainingData().get(i).getName());
            this.trainStrings[i] = sb.toString();
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
