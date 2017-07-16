package io.emccarthy.functionservice.resources;

import io.emccarthy.common.api.Expression;
import io.emccarthy.common.api.ExpressionResult;
import io.emccarthy.functionservice.api.Operator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/")
@Produces(MediaType.APPLICATION_JSON)
public class OperationResource {

    private static final Log LOG = LogFactory.getLog(OperationResource.class);

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response operate(Expression expression) {
        Operator operator = Operator.fromString(expression.getOperator());
        if(operator == null) {
            String message = "Operator of type [" + expression.getOperator() + "] not supported";
            LOG.error(message);
            throw new WebApplicationException(message, Response.Status.BAD_REQUEST);
        }
        double expressionResult = operator.evaluate(expression.getLeftOperand(), expression.getRightOperand());
        return Response.status(201).entity(new ExpressionResult(expressionResult)).build();
    }

}
