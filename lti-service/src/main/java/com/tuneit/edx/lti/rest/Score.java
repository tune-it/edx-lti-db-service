package com.tuneit.edx.lti.rest;

/**
 *
 * @author jek
 */
public class Score {

    public static final String xml = "<?xml version = \"1.0\" encoding = \"UTF-8\"?>\n"
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
            + "                      <sourcedId>feb-123-456-2929::28883</sourcedId>\n"
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
}
