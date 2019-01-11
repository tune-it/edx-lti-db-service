package com.tuneit.edx.lti.cources.datastorage;

import com.tuneit.edx.lti.cources.Service;
import com.tuneit.edx.lti.cources.Task;

/**
 * STUB need implements
 */
public class DataStorageService implements Service {

    @Override
    public Task[] getTasks(String studentId, String labId, int complexity) {
        Task[] tasks = new Task[complexity];

        // CREATE ans INITIALIZATION tasks here

        return tasks;
    }

    @Override
    public Task[] checkTasks(Task... tasks) {

        // VALIDATION tasks` answers and set rating

        return tasks;
    }
}
