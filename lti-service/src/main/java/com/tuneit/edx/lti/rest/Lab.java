package com.tuneit.edx.lti.rest;

import org.imsglobal.aspect.Lti;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.*;

@RestController
public class Lab {

    @Lti
    @PostMapping("/api/rest/lti/{labId}")
    public String doPost(@PathVariable("labId") String labId,
                         @RequestParam(name = "custom_x", required = false) Integer x,
                         @RequestHeader HttpHeaders headers,
                         @RequestBody String body) {

        System.out.println("####################################### REQUEST BODY");
        System.out.println(body);
        System.out.println("####################################### REQUEST HEADERS");

        for(String key : headers.keySet()) {
            if(headers.getValuesAsList(key).isEmpty()) {
                continue;
            }
            headers.getValuesAsList(key).forEach(value ->
                System.out.println(key + " ===> " + value)
            );
        }

        return "Task for lab work #" + labId + (x == null ? ";" : ("; x = " + x));
    }

}
