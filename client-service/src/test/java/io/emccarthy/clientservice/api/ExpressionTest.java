package io.emccarthy.clientservice.api;

public class ExpressionTest extends AbstractRepresentationTest<Expression> {

    @Override
    protected Expression newInstanceOne() {
        return new Expression(123.0d, 321.0d, "*");
    }

    @Override
    protected Expression newInstanceTwo() {
        return new Expression(4320.1d, -987.0d, "/");
    }

    @Override
    protected Class<Expression> getType() {
        return Expression.class;
    }

    @Override
    protected String getFixtureJSON() {
        return "fixtures/expression.json";
    }

}