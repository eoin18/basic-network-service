package io.emccarthy.clientservice.function;

import io.emccarthy.clientservice.function.exception.FunctionServiceConnectionException;
import io.emccarthy.clientservice.function.exception.FunctionServiceResponseException;
import io.emccarthy.common.api.Expression;
import io.emccarthy.common.api.ExpressionResult;

public interface FunctionServiceInterface {

    ExpressionResult performExpression(Expression expression) throws FunctionServiceConnectionException, FunctionServiceResponseException;

}
