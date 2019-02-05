package com.tuneit.edx.lti.unit;

import com.tuneit.courses.db.SelectProcessor;
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
        StringBuilder result = new StringBuilder();
        try {
            Schema schema = SchemaLoader.getSchema(0);
            SelectProcessor selectProc = new SelectProcessor();
            selectProc.execute_select(schema, sql, 10, result);
        }catch (Exception e) {
            result.append(e.getMessage());
        }

        if(result.length() == 0) {
            result.append("----------\n").append("No rows");
        }

        return result.toString();
    }

}
