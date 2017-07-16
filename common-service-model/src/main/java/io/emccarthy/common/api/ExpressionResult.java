package io.emccarthy.common.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

public class ExpressionResult {

    private double result;

    public ExpressionResult() {}

    public ExpressionResult(double result) {
        this.result = result;
    }

    @JsonProperty
    public double getResult() {
        return this.result;
    }

    @JsonProperty
    public void setResult(double result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return new StringBuilder()
                .append("Result: [")
                .append(this.result)
                .append("]")
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if(obj instanceof ExpressionResult){
            ExpressionResult other = (ExpressionResult) obj;
            return new EqualsBuilder()
                    .append(this.result, other.result)
                    .isEquals();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(this.result)
                .toHashCode();
    }
}
