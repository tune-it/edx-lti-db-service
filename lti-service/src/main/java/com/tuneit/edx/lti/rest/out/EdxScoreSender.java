package com.tuneit.edx.lti.rest.out;

import com.tuneit.edx.lti.rest.in.OAuthHeaders;
import com.tuneit.edx.lti.web.ScoreRestAPI;
import lombok.extern.slf4j.Slf4j;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.IOException;

/**
 * @author jek
 * @author alex
 */
@Slf4j
@Component
@Profile("prod")
public class EdxScoreSender implements ScoreSender {

    public int push(String sourcedId, String outcomeServiceUrl, float rating, OAuthHeaders headers) throws IOException {

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
    }
}
