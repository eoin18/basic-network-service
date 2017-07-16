package io.emccarthy.clientservice.function;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.base.Preconditions;
import io.dropwizard.jackson.Jackson;
import io.emccarthy.clientservice.api.Expression;
import io.emccarthy.clientservice.api.ExpressionResult;
import io.emccarthy.clientservice.function.exception.FunctionServiceConnectionException;
import io.emccarthy.clientservice.function.exception.FunctionServiceResponseException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
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
        String url = this.hostName + (this.port == 0 ? "" : ":" + String.valueOf(this.port));
        HttpPost post = new HttpPost(url);
        post.setHeader(new BasicHeader("Accept", MediaType.APPLICATION_JSON));
        StringEntity entity;
        try {
            entity = new StringEntity(OBJECT_MAPPER.writeValueAsString(expression));
        } catch (UnsupportedEncodingException | JsonProcessingException e) {
            String message = "Unable to serialize request for function server";
            throw handleResponseException(message, e);
        }
        entity.setContentEncoding(new BasicHeader("Content-type", MediaType.APPLICATION_JSON));
        post.setEntity(entity);
        HttpResponse response;
        try {
            response = this.httpClient.execute(post);
        } catch (IOException e) {
            String message = "Unable to connect to function server at url: [" + url + "] Please check function server is connected.";
            LOG.error(message, e);
            throw new FunctionServiceConnectionException(message, e);
        }
        if(response.getStatusLine().getStatusCode() != 200) {
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
