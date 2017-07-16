package io.emccarthy.clientservice.function;

import io.emccarthy.clientservice.api.Expression;
import io.emccarthy.clientservice.api.ExpressionResult;
import io.emccarthy.clientservice.function.exception.FunctionServiceConnectionException;
import io.emccarthy.clientservice.function.exception.FunctionServiceResponseException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OnlineFunctionServiceInterfaceTest {

    private static final String HOST_NAME = "localhost";
    private static final int HOST_PORT = 12345;

    @Mock
    private HttpClient mockHttpClient;

    @Mock
    private HttpResponse mockHttpResponse;

    @Mock
    private StatusLine mockStatusLine;

    private FunctionServiceInterface functionServiceInterface;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        HttpEntity mockHttpEntity = mock(HttpEntity.class);
        when(this.mockStatusLine.getStatusCode()).thenReturn(200);
        when(mockHttpEntity.getContent()).thenReturn(Thread.currentThread().getContextClassLoader().getResourceAsStream("references/expressionResponse.json"));
        when(this.mockHttpResponse.getEntity()).thenReturn(mockHttpEntity);
        when(this.mockHttpResponse.getStatusLine()).thenReturn(mockStatusLine);
        this.functionServiceInterface = new OnlineFunctionServiceInterface(HOST_NAME, HOST_PORT, this.mockHttpClient);
    }

    @Test
    public void testValidRequest() throws Exception {
        when(this.mockHttpClient.execute(any(HttpPost.class))).then(invocation -> this.mockHttpResponse);
        ExpressionResult expressionResult = this.functionServiceInterface.performExpression(new Expression(2.0d, 2.0d, "*"));
        assertEquals(new ExpressionResult(4.0d), expressionResult);
    }

    @Test(expected = FunctionServiceConnectionException.class)
    public void testConnectionExceptionOnFunctionServer() throws Exception {
        when(this.mockHttpClient.execute(any(HttpPost.class))).thenThrow(IOException.class);
        this.functionServiceInterface.performExpression(new Expression(2.0d, 2.0d, "*"));
    }

    @Test(expected = FunctionServiceResponseException.class)
    public void testResponseErrorStatusReturnedFromFunctionServer() throws Exception {
        when(this.mockStatusLine.getStatusCode()).thenReturn(403);
        when(this.mockHttpClient.execute(any(HttpPost.class))).then(invocation -> this.mockHttpResponse);
        this.functionServiceInterface.performExpression(new Expression(4.0d, 2.0d, "\\"));
    }

}