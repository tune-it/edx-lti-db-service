package com.tuneit.edx.lti.rest.out;

import lombok.extern.slf4j.Slf4j;
import oauth.signpost.exception.OAuthException;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.imsglobal.pox.IMSPOXRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.security.GeneralSecurityException;

/**
 * @author jek
 * @author alex
 * @author adpashnin
 */
@Slf4j
@Component
@Profile("prod")
public class EdxScoreSender implements ScoreSender {

    //Params should be given as -Dclient_key=CLIENT_KEY and -Dclient_secret=CLIENT_SECRET on startup
    @Value("${client_key}")
    private String CLIENT_KEY;
    @Value("${client_secret}")
    private String CLIENT_SECRET;

    public int push(String sourcedId, String outcomeServiceUrl, float rating) throws IOException {
        HttpPost request = null;

        try {
            request = IMSPOXRequest.buildReplaceResult(outcomeServiceUrl, CLIENT_KEY, CLIENT_SECRET, sourcedId, rating + "", null, true);
        } catch (OAuthException | GeneralSecurityException e) {
            e.printStackTrace();
        }
        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse httpResponse = client.execute(request);
        HttpEntity entity = httpResponse.getEntity();
        String responseString = EntityUtils.toString(entity, "UTF-8");
        log.info("RESPONSE ENTITY:\n{}", responseString);
        return httpResponse.getStatusLine().getStatusCode();
    }
}
