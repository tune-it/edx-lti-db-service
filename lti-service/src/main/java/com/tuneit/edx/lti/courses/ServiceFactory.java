package com.tuneit.edx.lti.courses;

import com.tuneit.edx.lti.courses.db.DBTaskGenerationService;
import com.tuneit.edx.lti.courses.example.ExampleService;

public class ServiceFactory {

    public static Service getDataSourceService() {
        return new DBTaskGenerationService();
    }

    public static Service getExampleService() {
        return new ExampleService();
    }

}
