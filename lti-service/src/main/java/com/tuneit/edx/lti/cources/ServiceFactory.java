package com.tuneit.edx.lti.cources;

import com.tuneit.edx.lti.cources.datastorage.DataStorageStub;

public class ServiceFactory {

    public static Service getDataSourceService() {
        return new DataStorageStub();
    }

}
