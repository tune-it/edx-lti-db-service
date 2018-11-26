package com.tuneit.edx.lti.rest;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;

import org.imsglobal.aspect.Lti;

@RestController
public class Lab {

    @Lti
    @PostMapping("/api/rest/lti/{labId}")
    public String doPost(@PathVariable("labId") String labId) {
        return "Task for lab work #" + labId;
    }

}
