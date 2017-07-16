package io.emccarthy.functionservice.api;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum Operator implements Evaluable {

    MULTIPLY ("*"){
        @Override
        public double evaluate(double left, double right) {
            return left * right;
        }
    };

    private String representation;

    Operator(String representation) {
        this.representation = representation;
    }

    @JsonCreator
    public static Operator fromString(String stringRepresentation) {
        for(Operator operator : Operator.values()) {
            if(operator.representation.equals(stringRepresentation)){
                return operator;
            }
        }
        return null;
    }

    @JsonValue
    public String getStringRepresentation() {
        return this.representation;
    }
}
