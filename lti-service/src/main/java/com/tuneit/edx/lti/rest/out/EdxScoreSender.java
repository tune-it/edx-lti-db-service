package com.tuneit.edx.lti.rest.out;

import com.tuneit.edx.lti.web.ScoreRestAPI;
import lombok.extern.slf4j.Slf4j;
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

    public int push(String sourcedId, String outcomeServiceUrl, float rating) throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://localhost:18010")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build();

        String score = String.format("%.2f", rating);

        ScoreRestAPI service = retrofit.create(ScoreRestAPI.class);

        log.debug("%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% REQUEST");
        log.debug(getXmlContent(sourcedId, score));
        Call<String> response = service.post(outcomeServiceUrl, getXmlContent(sourcedId, score));
        Response<String> execute = response.execute();

        log.debug("################################# RESPONSE");
        log.debug(execute.message());
        log.debug(execute.body());
        return execute.code();
    }
}
