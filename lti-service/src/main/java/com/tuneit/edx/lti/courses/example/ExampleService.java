package com.tuneit.edx.lti.courses.example;

import com.tuneit.edx.lti.courses.Service;
import com.tuneit.edx.lti.courses.Task;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
@Profile("dev")
public class ExampleService implements Service {

    @Override
    public Task[] getTasks(String studentId, String labId, String variant, int complexity) {

        String[] questions = new String[] {
                "Выдать содержимое всех столбцов таблицы Н_ПЛАНЫ",
                "Вывести список аудиторий университета",
                "Вывести только уникальные отчества сотрудников и студентов",
                "Вычислить возраст человека в милисекундах",
                "Вывести код названия оценок студентов в процессе обучения",
                "Вычислить продолжительность обучения учеников в часах",
                "Вычислить продолжительность обучения учеников в днях",
                "Вывести сокращенные наименования компонентов учебного процесса",
                "Выдать содержимое всех столбцов таблицы Н_ТИПЫ_ПЛАНОВ",
                "Вычислить возраст человека в годах",
                "Вычислить возраст человека в годах"
        };

        Task[] tasks = new Task[2];
        for(int i = 0; i < 2; ++i) {
            tasks[i] = new Task();
            Task t = tasks[i];
            t.setStudentId(studentId).setLabId(labId).setVariant(variant);
            t.setQuestion(
                questions[new Random().nextInt(10)]
            );
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
        return answer.startsWith("select");
    }
}
