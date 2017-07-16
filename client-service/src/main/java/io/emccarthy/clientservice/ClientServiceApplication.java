package io.emccarthy.clientservice;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import io.emccarthy.clientservice.function.FunctionServiceInterface;
import io.emccarthy.clientservice.function.OnlineFunctionServiceInterface;
import io.emccarthy.clientservice.resources.MultiplicationResource;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;

public class ClientServiceApplication extends Application<ClientServiceConfiguration> {

    public static void main(String[] args) throws Exception {
        new ClientServiceApplication().run(args);
    }

    @Override
    public String getName() {
        return "client-service";
    }

    @Override
    public void run(ClientServiceConfiguration configuration, Environment environment) throws Exception {
        HttpClient httpClient = HttpClientBuilder.create().build();
        FunctionServiceInterface functionServiceInterface = new OnlineFunctionServiceInterface(
                configuration.getFunctionServerHost(), configuration.getFunctionServerPort(), httpClient);

        MultiplicationResource multiplicationResource = new MultiplicationResource(functionServiceInterface);
        environment.jersey().register(multiplicationResource);
    }
}
