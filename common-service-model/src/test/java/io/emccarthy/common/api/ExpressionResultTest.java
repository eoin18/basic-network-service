package io.emccarthy.common.api;

public class ExpressionResultTest extends AbstractRepresentationTest<ExpressionResult>{

    @Override
    protected ExpressionResult newInstanceOne() {
        return new ExpressionResult(666.0d);
    }

    @Override
    protected Class<ExpressionResult> getType() {
        return ExpressionResult.class;
    }

    @Override
    protected ExpressionResult newInstanceTwo() {
        return new ExpressionResult(0d);
    }

    @Override
    protected String getFixtureJSON() {
        return "fixtures/expressionResult.json";
    }


}