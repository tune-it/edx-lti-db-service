package com.tuneit.edx.lti.rest.in;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * SQL shell handler for developer purposes. Disabled in production profile.
 * @author alex
 */
@Slf4j
@Controller
@Profile("dev")
public class DevSqlShellHandler implements SqlShellHandler {

    @Override
    @GetMapping(MAIN_URL)
    public String handleInitShell() {
        return "shell";
    }

    @Override
    @ResponseBody
    @PostMapping(value = SQL_PROC_URL, produces = {MediaType.TEXT_PLAIN_VALUE})
    public String handleSqlQuery(String sqlQuery) {
        return "теперь я черуратор";
    }
}
