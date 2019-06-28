package ru.ifmo.edx.lti.rest.in;

public interface SqlShellHandler {

    String MAIN_URL = "/api/rest/lti/shell/sql";

    String SQL_PROC_URL = "/api/rest/lti/shell/sql/query";

    String handleInitShell();

    String handleSqlQuery(String sqlQuery);

}
