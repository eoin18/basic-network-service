package io.emccarthy.functionservice.resources;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.ByteStreams;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.junit.ResourceTestRule;
import io.emccarthy.common.api.Expression;
import io.emccarthy.common.api.ExpressionResult;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.InputStream;

import static org.junit.Assert.assertEquals;

public class OperationResourceTest {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    @Rule
    public ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new OperationResource())
            .build();

    @Test
    public void testValidRequest() throws Exception {
        Expression expression = new Expression(3.0d, 100.0d, "*");
        Response response = resources.target("/").request().post(Entity.entity(expression, MediaType.APPLICATION_JSON_TYPE));
        ExpressionResult expressionResult = MAPPER.readValue(ByteStreams.toByteArray((InputStream) response.getEntity()), ExpressionResult.class);
        assertEquals(new ExpressionResult(300.0d), expressionResult);
    }

    @Test
    public void testInvalidOperator() throws Exception {
        Expression expression = new Expression(3.0d, 100.0d, "oh no!");
        Response response = resources.target("/").request().post(Entity.entity(expression, MediaType.APPLICATION_JSON_TYPE));
        assertEquals(400, response.getStatus());
    }

}