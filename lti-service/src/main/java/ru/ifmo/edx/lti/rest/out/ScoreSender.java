package ru.ifmo.edx.lti.rest.out;

import java.io.IOException;

public interface ScoreSender {

    String SOURCED_ID_PATTERN = "####";

    String SCORE_PATTERN = "----";

    String XML_PATTERN =
            "<?xml version = \"1.0\" encoding = \"UTF-8\"?>\n" +
            "<imsx_POXEnvelopeRequest xmlns = \"http://www.imsglobal.org/services/ltiv1p1/xsd/imsoms_v1p0\">\n" +
            "  <imsx_POXHeader>\n" +
            "    <imsx_POXRequestHeaderInfo>\n" +
            "      <imsx_version>V1.0</imsx_version>\n" +
            "      <imsx_messageIdentifier>999999123</imsx_messageIdentifier>\n" +
            "    </imsx_POXRequestHeaderInfo>\n" +
            "  </imsx_POXHeader>\n" +
            "  <imsx_POXBody>\n" +
            "    <replaceResultRequest>\n" +
            "      <resultRecord>\n" +
            "        <sourcedGUID>\n" +
            "          <sourcedId>" + SOURCED_ID_PATTERN + "</sourcedId>\n" +
            "        </sourcedGUID>\n" +
            "        <result>\n" +
            "          <resultScore>\n" +
            "            <language>en</language>\n" +
            "            <textString>" + SCORE_PATTERN + "</textString>\n" +
            "          </resultScore>\n" +
            "        </result>\n" +
            "      </resultRecord>\n" +
            "    </replaceResultRequest>\n" +
            "  </imsx_POXBody>\n" +
            "</imsx_POXEnvelopeRequest>";

    default String getXmlContent(String sourcedId, String score) {
        return XML_PATTERN
                .replace(SOURCED_ID_PATTERN, sourcedId)
                .replace(SCORE_PATTERN, score);
    }

    int push(String sourcedId, String outcomeServiceUrl, float rating) throws IOException;

}
