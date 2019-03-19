package com.tuneit.edx.lti.rest.in;

import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

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
}
