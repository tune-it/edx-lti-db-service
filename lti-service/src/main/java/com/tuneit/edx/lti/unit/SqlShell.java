package com.tuneit.edx.lti.unit;

import com.tuneit.courses.db.checking.SelectProcessor;
import com.tuneit.courses.db.checking.SelectResult;
import com.tuneit.courses.db.generate.Schema;
import com.tuneit.courses.db.generate.SchemaLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

/**
 * SQL shell processor
 * @author alex
 */
@Slf4j
@Component
@Profile("prod")
public class SqlShell {

    @Autowired
    SchemaLoader schemaLoader;

    @Autowired
    SelectProcessor selectProcessor;

    /**
     * Execute SQL query
     * @param sql Query
     * @return Request processing result
     */
    public String exec(String sql) {
        SelectResult result;
        try {
            result = selectProcessor.executeQuery(sql, null, 10, true);
        } catch (Exception e) {
            return e.getMessage();
        }

        if (result.getRowCount() == 0) {
            return "----------\nNo rows";
        }

        return result.getHtmlRows().toString();
    }

}
