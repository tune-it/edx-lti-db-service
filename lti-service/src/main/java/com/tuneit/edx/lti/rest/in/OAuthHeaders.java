package com.tuneit.edx.lti.rest.in;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Getter
@Setter
public class OAuthHeaders {

    private Pair<String, String> nonce = new MutablePair<>("oauth_nonce", "");
    private Pair<String, String> timestamp = new MutablePair<>("oauth_timestamp", "");
    private Pair<String, String> consumerKey = new MutablePair<>("oauth_consumer_key", "");
    private Pair<String, String> signatureMethod = new MutablePair<>("oauth_signature_method", "");
    private Pair<String, String> version = new MutablePair<>("oauth_version", "");
    private Pair<String, String> signature = new MutablePair<>("oauth_signature", "");

    public Pair[] getAll() {
        return new Pair[] {nonce, timestamp, consumerKey, signatureMethod, version, signature};
    }

    /**
     * Generate value for OAuth header, use body for calculating oauth_body_hash
     * @param body
     * @return
     */
    public String value(String body) {
        StringBuilder builder = new StringBuilder();

        builder.append("OAuth realm=\"\",");
        for(Pair p : getAll()) {
            builder
                .append((String) p.getKey())
                .append("=\"")
                .append((String)p.getValue())
                .append("\",");
        }
        builder.append("oauth_body_hash=\"").append(getSHA1(body)).append("\"");

        return builder.toString();
    }

    public String name() {
        return "Authorization";
    }

    private String getSHA1(String string) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("SHA-1");
            return new String(md.digest(string.getBytes()));
        }
        catch(NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }
}
