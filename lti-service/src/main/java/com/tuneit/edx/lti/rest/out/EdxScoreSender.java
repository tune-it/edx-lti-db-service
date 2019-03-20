package com.tuneit.edx.lti.rest.out;

import com.tuneit.edx.lti.rest.in.OAuthHeaders;
import com.tuneit.edx.lti.web.ScoreRestAPI;
import lombok.extern.slf4j.Slf4j;
import oauth.signpost.exception.OAuthException;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.imsglobal.pox.IMSPOXRequest;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Arrays;

/**
 * @author jek
 * @author alex
 */
@Slf4j
@Component
@Profile("prod")
public class EdxScoreSender implements ScoreSender {

    public int push(String sourcedId, String outcomeServiceUrl, float rating, OAuthHeaders headers) throws IOException {
        HttpPost request = null;

        log.debug("\n\n################################\bBEGIN PUSH");
        log.debug("PARAMS: sourcedId={}, outcomeServiceUrl={}, rating={}", sourcedId, outcomeServiceUrl, rating);
        log.debug("OAuthHeaders:");
        Arrays.stream(headers.getAll()).forEach(pair -> log.debug("{}: {}", pair.getKey(), pair.getValue()));
        try {
            request = IMSPOXRequest.buildReplaceResult(outcomeServiceUrl, "__CONSUMER KEY__", "__CONSUMER SECRETE__", sourcedId, rating + "", null, true);
            HttpEntity entity = request.getEntity();
            String requestString = EntityUtils.toString(entity, "UTF-8");
            log.debug("REQUEST STRING {}", requestString);
            log.debug("REQUEST HEADERS:");
            Arrays.stream(request.getAllHeaders()).forEach(header -> log.debug("{}: {}", header.getName(), header.getValue()));
        } catch (OAuthException | GeneralSecurityException e) {
            e.printStackTrace();
        }
/*
        String score = String.format("%.2f", rating);
        String body = getXmlContent(sourcedId, score);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new Interceptor() {
              @Override
              public okhttp3.Response intercept(Interceptor.Chain chain) throws IOException {
                  Request original = chain.request();

                  Request.Builder builder = original.newBuilder();

                  builder.header(headers.name(), headers.value(body));

                  Request request = builder
                      .method(original.method(), original.body())
                      .build();

                  return chain.proceed(request);
              }
        });

        OkHttpClient client = httpClient.build();

        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://localhost:18010")
            .addConverterFactory(ScalarsConverterFactory.create())
            .client(client)
            .build();

        ScoreRestAPI service = retrofit.create(ScoreRestAPI.class);
        Call<String> response = service.post(outcomeServiceUrl, body);
        Response<String> execute = response.execute();

        log.debug("################################# RESPONSE");

        log.debug(execute.message());
        log.debug(execute.body());

        return execute.code();
        */
        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse httpResponse = client.execute(request);
        HttpEntity entity = httpResponse.getEntity();
        String responseString = EntityUtils.toString(entity, "UTF-8");
        log.debug("Response code: {}", httpResponse.getStatusLine().getStatusCode());
        log.debug("Response headers:");
        Arrays.stream(httpResponse.getAllHeaders()).forEach(header -> log.debug("{}: {}", header.getName(), header.getValue()));
        log.debug("RESPONSE ENTITY: {}", responseString);
        log.debug("################################# RESPONSE");
        return httpResponse.getStatusLine().getStatusCode();
    }
}
