package com.tuneit.edx.lti.cources;

/**
 * Generator of tasks
 */
public interface Service {

    /**
     * @param labId      identifier of lab
     * @param complexity count of questions
     * @return array of tasks
     */
    Task[] getTasks(String labId, int complexity);

}
