package ru.ifmo.edx.lti.rest.in;

import ru.ifmo.edx.lti.unit.SqlShell;
import lombok.extern.slf4j.Slf4j;
import org.imsglobal.aspect.Lti;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Handler for SQL shell LTI requests
 * @author alex
 */
@Slf4j
@Controller
@CrossOrigin
@Profile("prod")
public class EdxSqlShellHandler implements SqlShellHandler {

    @Autowired
    private SqlShell shell;

    @Lti
    @Override
    @PostMapping(MAIN_URL)
    public String handleInitShell() {
        
        // may be add some objects to model of page

        return "shell";
    }

    @Override
    @ResponseBody
    @PostMapping(value = SQL_PROC_URL, produces = {MediaType.TEXT_PLAIN_VALUE})
    public String handleSqlQuery(@RequestParam(value = "query") String sqlQuery) {
        return shell.exec(sqlQuery);
    }
}
