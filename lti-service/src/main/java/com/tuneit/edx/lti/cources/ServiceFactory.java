package com.tuneit.edx.lti.cources;

import com.tuneit.edx.lti.cources.db.DBTaskGenerationService;
import com.tuneit.edx.lti.cources.example.ExampleService;

public class ServiceFactory {

    public static Service getDataSourceService() {
        return new DBTaskGenerationService();
    }

    public static Service getExampleService() {
        return new ExampleService();
    }

}
