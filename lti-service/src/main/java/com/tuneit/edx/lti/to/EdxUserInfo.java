package com.tuneit.edx.lti.to;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Обертка для быстрого парсинга edx-user-info
 * в Java-объект
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EdxUserInfo {

    String username = "";
    String version = "";
    String enrollmentStatusHash = "";
    HeaderUrls header_urls = new HeaderUrls();

    @Override
    public String toString() {
        return "EdxUserInfo {" +
                "username = '" + username + '\'' +
                ", version = '" + version + '\'' +
                ", enrollmentStatusHash = '" + enrollmentStatusHash + '\'' +
                ", header_urls = " + header_urls.toString() +
                '}';
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class HeaderUrls {

        String learner_profile = "";
        String resume_block = "";
        String logout = "";
        String account_settings = "";

        @Override
        public String toString() {
            return "HeaderUrls {" +
                    "learner_profile = '" + learner_profile + '\'' +
                    ", resume_block = '" + resume_block + '\'' +
                    ", logout = '" + logout + '\'' +
                    ", account_settings = '" + account_settings + '\'' +
                    '}';
        }
    }
}
