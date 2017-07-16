package io.emccarthy.functionservice;

import io.dropwizard.Application;
import io.dropwizard.setup.Environment;
import io.emccarthy.functionservice.resources.OperationResource;

public class FunctionServiceApplication extends Application<FunctionServiceConfiguration> {

    public static void main(String[] args) throws Exception {
        new FunctionServiceApplication().run(args);
    }

    @Override
    public void run(FunctionServiceConfiguration configuration, Environment environment) throws Exception {
        environment.jersey().register(new OperationResource());
    }
}
