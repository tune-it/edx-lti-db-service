package com.tuneit.edx.lti.cources;

import com.tuneit.edx.lti.cources.datastorage.DataStorageService;
import com.tuneit.edx.lti.cources.example.ExampleService;

public class ServiceFactory {

    public static Service getDataSourceService() {
        return new DataStorageService();
    }

    public static Service getExampleService() {
        return new ExampleService();
    }

}
