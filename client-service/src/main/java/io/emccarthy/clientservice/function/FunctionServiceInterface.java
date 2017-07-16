package io.emccarthy.clientservice.function;

import io.emccarthy.clientservice.api.Expression;
import io.emccarthy.clientservice.api.ExpressionResult;
import io.emccarthy.clientservice.function.exception.FunctionServiceConnectionException;
import io.emccarthy.clientservice.function.exception.FunctionServiceResponseException;

public interface FunctionServiceInterface {

    ExpressionResult performExpression(Expression expression) throws FunctionServiceConnectionException, FunctionServiceResponseException;

}
