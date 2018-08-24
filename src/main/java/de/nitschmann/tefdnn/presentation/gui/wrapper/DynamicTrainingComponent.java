package de.nitschmann.tefdnn.presentation.gui.wrapper;

import java.awt.*;

public class DynamicTrainingComponent {
    private TrainingComponentType componentType;
    private Component component;

    public DynamicTrainingComponent(Component component, TrainingComponentType componentType) {
        this.component = component;
        this.componentType = componentType;
    }

    public Component getComponent() {
        return this.component;
    }

    public TrainingComponentType getComponentType() {
        return this.componentType;
    }
}
