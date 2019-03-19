package com.tuneit.edx.lti.rest.out;

import com.tuneit.edx.lti.rest.in.OAuthHeaders;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("dev")
public class DevScoreSender implements ScoreSender {

    @Override
    public int push(String sourcedId, String outcomeServiceUrl, float rating, OAuthHeaders headers) {

        log.info(getXmlContent(sourcedId, String.valueOf(rating)));

        return HttpStatus.OK.value();
    }
}
