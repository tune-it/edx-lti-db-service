package com.tuneit.edx.lti.rest.out;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

/**
 * Score sender for developer purposes. Disabled in production profile.
 * @author alex
 */
@Slf4j
@Component
@Profile("dev")
public class DevScoreSender implements ScoreSender {

    /**
     * Pushes score into EdX LMS.
     * @param sourcedId sourceId
     * @param outcomeServiceUrl URL of the outcome service
     * @param rating Rating [0-1]
     * @return Push HTTP request status
     */
    @Override
    public int push(String sourcedId, String outcomeServiceUrl, float rating) {

        log.info(getXmlContent(sourcedId, String.valueOf(rating)));

        return HttpStatus.OK.value();
    }
}
