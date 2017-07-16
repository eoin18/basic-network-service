package io.emccarthy.clientservice;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import org.hibernate.validator.constraints.NotEmpty;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

public class ClientServiceConfiguration extends Configuration {

    @NotEmpty
    private String functionServerHost;

    @Min(1)
    @Max(65535)
    private int functionServerPort;

    @JsonProperty
    public void setFunctionServerHost(String functionServerHost) {
        this.functionServerHost = functionServerHost;
    }

    @JsonProperty
    public void setFunctionServerPort(int functionServerPort) {
        this.functionServerPort = functionServerPort;
    }

    @JsonProperty
    public String getFunctionServerHost() {
        return this.functionServerHost;
    }

    @JsonProperty
    public int getFunctionServerPort() {
        return functionServerPort;
    }
}
