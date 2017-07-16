package io.emccarthy.clientservice.resources;

import com.google.common.base.Preconditions;
import io.emccarthy.clientservice.api.Expression;
import io.emccarthy.clientservice.api.ExpressionResult;
import io.emccarthy.clientservice.function.FunctionServiceInterface;
import io.emccarthy.clientservice.function.exception.FunctionServiceConnectionException;
import io.emccarthy.clientservice.function.exception.FunctionServiceResponseException;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/multiply")
@Produces(MediaType.APPLICATION_JSON)
public class MultiplicationResource {

    private final FunctionServiceInterface functionServiceInterface;

    public MultiplicationResource(final FunctionServiceInterface functionServiceInterface) {
        this.functionServiceInterface = Preconditions.checkNotNull(functionServiceInterface);
    }

    @GET
    public ExpressionResult multiply(@QueryParam("left") String left , @QueryParam("right") String right) {
        double leftOperand = parseOperand(left);
        double rightOperand = parseOperand(right);
        try {
            return this.functionServiceInterface.performExpression(new Expression(leftOperand, rightOperand, "*"));
        } catch (FunctionServiceResponseException e) {
            throw new WebApplicationException(e, Response.Status.INTERNAL_SERVER_ERROR);
        } catch (FunctionServiceConnectionException e) {
            throw new WebApplicationException(e, Response.Status.SERVICE_UNAVAILABLE);
        }
    }

    private double parseOperand(String operand) {
        try {
            return Double.valueOf(operand);
        } catch (NumberFormatException e) {
            throw new BadRequestException(String.format("Operand with value %s must be a number.", operand));
        }
    }
}
