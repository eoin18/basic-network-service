package io.emccarthy.clientservice.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class Expression {

    private double leftOperand;
    private double rightOperand;
    private String operator;

    public Expression() {}

    public Expression(double leftOperand, double rightOperand, String operator) {
        this.leftOperand = leftOperand;
        this.rightOperand = rightOperand;
        this.operator = operator;
    }

    @JsonProperty
    public double getLeftOperand() {
        return leftOperand;
    }

    @JsonProperty
    public double getRightOperand() {
        return rightOperand;
    }

    @JsonProperty
    public String getOperator() {
        return operator;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append(leftOperand)
                .append(" ").append(operator).append(" ")
                .append(rightOperand)
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof Expression) {
            Expression other = (Expression) obj;
            return new EqualsBuilder()
                    .append(this.leftOperand, other.leftOperand)
                    .append(this.rightOperand, other.rightOperand)
                    .append(this.operator, other.operator)
                    .isEquals();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.leftOperand)
                .append(this.rightOperand)
                .append(this.operator)
                .toHashCode();
    }
}
