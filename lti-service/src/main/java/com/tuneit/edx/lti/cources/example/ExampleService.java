package com.tuneit.edx.lti.cources.example;

import com.tuneit.edx.lti.cources.Service;
import com.tuneit.edx.lti.cources.Task;

public class ExampleService implements Service {

    @Override
    public Task[] getTasks(String studentId, String labId, int complexity) {
        Task[] tasks = new Task[complexity];
        for(int i = 0; i < complexity; ++i) {
            Task t = tasks[i];
            t.setId(studentId + labId + System.nanoTime());
        }
        return tasks;
    }

    @Override
    public Task[] checkTasks(Task... tasks) {

        for(Task t : tasks) {
            if(t.isComplete()) {
                t.setRating(  checkTask(t.getAnswer()) ? 1.0f : 0.0f  );
            }
        }

        return tasks;
    }

    private boolean checkTask(String answer) {
        return "привет матери мамонтенка".equalsIgnoreCase(answer);
    }
}
