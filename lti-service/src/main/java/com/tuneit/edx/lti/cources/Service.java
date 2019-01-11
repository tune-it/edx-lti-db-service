package com.tuneit.edx.lti.cources;

/**
 * Generator of tasks
 */
public interface Service {

    /**
     * @param studentId  identifier of student
     * @param labId      identifier of lab
     * @param complexity count of questions
     * @return array of tasks
     */
    Task[] getTasks(String studentId, String labId, int complexity);

    /**
     * @param tasks list of tasks for validation
     * @return array of validated tasks
     */
    Task[] checkTasks(Task... tasks);

}
