package io.emccarthy.clientservice.resources;

import io.dropwizard.testing.junit.ResourceTestRule;
import io.emccarthy.clientservice.function.FunctionServiceInterface;
import io.emccarthy.clientservice.function.exception.FunctionServiceConnectionException;
import io.emccarthy.clientservice.function.exception.FunctionServiceResponseException;
import io.emccarthy.common.api.Expression;
import io.emccarthy.common.api.ExpressionResult;
import org.junit.Rule;
import org.junit.Test;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.WebApplicationException;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MultiplicationResourceTest {

    private static final FunctionServiceInterface MOCK_FUNCTION_SERVICE_INTERFACE = mock(FunctionServiceInterface.class);

    @Rule
    public ResourceTestRule resources = ResourceTestRule.builder()
            .addResource(new MultiplicationResource(MOCK_FUNCTION_SERVICE_INTERFACE))
            .build();

    @Test
    public void testValidExpressionInput() throws Exception {
        when(MOCK_FUNCTION_SERVICE_INTERFACE.performExpression(any(Expression.class))).then(invocation -> {
            Expression argument = invocation.getArgument(0);
            return new ExpressionResult(argument.getLeftOperand() * argument.getRightOperand());
        });
        ExpressionResult expressionResult = resources.target("/multiply").queryParam("left", 2).queryParam("right", 4).request().get(ExpressionResult.class);
        assertEquals(new ExpressionResult(8), expressionResult);
    }

    @Test(expected = WebApplicationException.class)
    public void testWhenFunctionServiceIsDown() throws Exception {
        when(MOCK_FUNCTION_SERVICE_INTERFACE.performExpression(any(Expression.class))).thenThrow(FunctionServiceConnectionException.class);
        resources.target("/multiply").queryParam("left", 2).queryParam("right", 4).request().get(ExpressionResult.class);
    }

    @Test(expected = WebApplicationException.class)
    public void testWhenFunctionServiceReturnsResponseError() throws Exception {
        when(MOCK_FUNCTION_SERVICE_INTERFACE.performExpression(any(Expression.class))).thenThrow(FunctionServiceResponseException.class);
        resources.target("/multiply").queryParam("left", 2).queryParam("right", 4).request().get(ExpressionResult.class);
    }

    @Test(expected = BadRequestException.class)
    public void testWhenOperandsAreNotNumbers() throws Exception {
        resources.target("/multiply").queryParam("left", "test").queryParam("right", "test").request().get(ExpressionResult.class);
    }

}