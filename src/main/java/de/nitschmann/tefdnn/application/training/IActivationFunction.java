package de.nitschmann.tefdnn.application.training;

public interface IActivationFunction {
    double activate(double value);
    double derivative(double value);
}
