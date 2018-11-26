package com.tuneit.edx.lti.rest;

import org.imsglobal.aspect.Lti;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Controller
public class Lab {

    @GetMapping("/debug/edx/home")
    public String doGet(@RequestParam(name = "custom_x", required = false) Integer x,
                        @RequestParam(name = "user_id", required = false) String userId,
                        @RequestHeader HttpHeaders headers,
                        @RequestBody(required = false) String body,
                        Map<String, Object> model) {
        if(body == null || body.isEmpty()) {
            body = "Да прибудет дом сей в чистоте и порядке!";
        }
        return doPost("1", x, userId, headers, body, model);
    }

    @Lti
    @PostMapping("/api/rest/lti/{labId}")
    public String doPost(@PathVariable("labId") String labId,
                         @RequestParam(name = "custom_x", required = false) Integer x,
                         @RequestParam(name = "user_id", required = false) String userId,
                         @RequestHeader HttpHeaders headers,
                         @RequestBody String body,
                         Map<String, Object> model) {

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

        model.put("username", (userId == null ? "Guest" : userId));
        model.put("custom_param", (x == null ? "nothing" : x));

        return "index";
    }

}
