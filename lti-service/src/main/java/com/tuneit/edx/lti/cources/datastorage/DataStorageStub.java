package com.tuneit.edx.lti.cources.datastorage;

import com.tuneit.edx.lti.cources.Service;
import com.tuneit.edx.lti.cources.Task;

public class DataStorageStub implements Service {

    @Override
    public Task[] getTasks(String labId, int complexity) {
        int count = complexity;
        if(complexity <= 0) {
            count = 2;
        }
        Task[] tasks = new Task[count];
        for(int i = 0; i < count; ++i)
            tasks[i] = new Task() {

                @Override
                public String getQuestion() {
                    return "DataStorage course generated question here with id #" + getId();
                }
            };

        return tasks;
    }
}
