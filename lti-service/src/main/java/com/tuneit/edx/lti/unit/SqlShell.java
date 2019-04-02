package com.tuneit.edx.lti.unit;

import com.tuneit.courses.db.SelectProcessor;
import com.tuneit.courses.db.SelectResult;
import com.tuneit.courses.db.schema.Schema;
import com.tuneit.courses.db.schema.SchemaLoader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Profile("prod")
public class SqlShell {

    public String exec(String sql) {
        SelectResult result;
        try {
            Schema schema = SchemaLoader.getSchema(0);
            SelectProcessor selectProc = new SelectProcessor();
            result = selectProc.executeQuery(schema, sql, 10, true);
        }catch (Exception e) {
            return e.getMessage();
        }

        if (result.getRowCount() == 0) {
            return "----------\nNo rows";
        }

        return result.getHtmlRows().toString();
    }

}
