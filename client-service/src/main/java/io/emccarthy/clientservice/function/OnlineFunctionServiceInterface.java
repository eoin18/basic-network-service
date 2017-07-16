package io.emccarthy.clientservice.function;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Preconditions;
import io.dropwizard.jackson.Jackson;
import io.emccarthy.clientservice.function.exception.FunctionServiceConnectionException;
import io.emccarthy.clientservice.function.exception.FunctionServiceResponseException;
import io.emccarthy.common.api.Expression;
import io.emccarthy.common.api.ExpressionResult;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;

import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class OnlineFunctionServiceInterface implements FunctionServiceInterface {

    private static final Log LOG = LogFactory.getLog(OnlineFunctionServiceInterface.class);
    private static final ObjectMapper OBJECT_MAPPER = Jackson.newObjectMapper().configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

    private final String hostName;
    private final int port;
    private final HttpClient httpClient;

    public OnlineFunctionServiceInterface(final String hostName, final int port, final HttpClient httpClient) {
        this.hostName = Preconditions.checkNotNull(hostName, "Host name is required");
        this.port = port;
        this.httpClient = httpClient;
    }

    @Override
    public ExpressionResult performExpression(Expression expression) throws FunctionServiceConnectionException, FunctionServiceResponseException {
        String url = "http://" + this.hostName + (this.port == 0 ? "" : ":" + String.valueOf(this.port));
        HttpPost post = new HttpPost(url);
        post.setHeader(new BasicHeader("Accept", MediaType.APPLICATION_JSON));
        StringEntity entity;
        try {
            String expressionAsString = OBJECT_MAPPER.writeValueAsString(expression);
            entity = new StringEntity(expressionAsString, ContentType.APPLICATION_JSON);
        } catch (JsonProcessingException e) {
            String message = "Unable to serialize request for function server";
            throw handleResponseException(message, e);
        }
        post.setEntity(entity);
        HttpResponse response;
        try {
            response = this.httpClient.execute(post);
        } catch (IOException e) {
            String message = "Unable to connect to function server at url: [" + url + "] Please check function server is connected.";
            LOG.error(message, e);
            throw new FunctionServiceConnectionException(message, e);
        }
        if(response.getStatusLine().getStatusCode() != 201) {
            String message = "HTTP response error from Function server with status code: [" + response + "] for request: [" + expression + "]";
            throw new FunctionServiceResponseException(message);
        }
        try {
            return OBJECT_MAPPER.readValue(response.getEntity().getContent(), ExpressionResult.class);
        } catch(IOException e) {
            String message = "Unable to deserialize http response from function server";
            throw handleResponseException(message, e);
        }
    }

    private FunctionServiceResponseException handleResponseException(String message, IOException e) throws FunctionServiceResponseException {
        LOG.error(message, e);
        return new FunctionServiceResponseException(message, e);
    }
}
