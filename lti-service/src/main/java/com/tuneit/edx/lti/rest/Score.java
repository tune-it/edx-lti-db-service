package com.tuneit.edx.lti.rest;

import com.tuneit.edx.lti.web.ScoreRestAPI;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;

import java.io.IOException;

/**
 * @author jek
 */
public class Score {

    public static final String XML_PATTERN =
              "<?xml version = \"1.0\" encoding = \"UTF-8\"?>\n"
            + "            <imsx_POXEnvelopeRequest xmlns = \"some_link (may be not required)\">\n"
            + "              <imsx_POXHeader>\n"
            + "                <imsx_POXRequestHeaderInfo>\n"
            + "                  <imsx_version>V1.0</imsx_version>\n"
            + "                  <imsx_messageIdentifier>528243ba5241b</imsx_messageIdentifier>\n"
            + "                </imsx_POXRequestHeaderInfo>\n"
            + "              </imsx_POXHeader>\n"
            + "              <imsx_POXBody>\n"
            + "                <replaceResultRequest>\n"
            + "                  <resultRecord>\n"
            + "                    <sourcedGUID>\n"
            + "                      <sourcedId>####</sourcedId>\n"
            + "                    </sourcedGUID>\n"
            + "                    <result>\n"
            + "                      <resultScore>\n"
            + "                        <language>en-us</language>\n"
            + "                        <textString>0.4</textString>\n"
            + "                      </resultScore>\n"
            + "                    </result>\n"
            + "                  </resultRecord>\n"
            + "                </replaceResultRequest>\n"
            + "              </imsx_POXBody>\n"
            + "            </imsx_POXEnvelopeRequest>";

    public static String getXmlContent(String sourcedId) {
        return XML_PATTERN.replace("####", sourcedId);
    }

    public static int push(String sourcedId, String outcomeServiceUrl) throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
            .baseUrl("http://localhost:18010")
            .addConverterFactory(ScalarsConverterFactory.create())
            .build();

        ScoreRestAPI service = retrofit.create(ScoreRestAPI.class);
        Call<Void> response = service.post(outcomeServiceUrl, getXmlContent(sourcedId));
        Response<Void> execute = response.execute();
        System.out.println("*&^34*&^34*&^34*&^34 =====>> " + execute.message());
        return execute.code();
    }
}
