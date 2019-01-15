
package com.tuneit.edx.lti.cources.db;

/**
 * Dummy Class to persisting store already generated task question and answer 
 * and avoid high load data corruption.
 * 
 * @author serge
 */

public class LabTaskQA {
    
    protected String id;
    protected String question;
    protected String correctAnswer;

    public LabTaskQA(String id, String question, String correctAnswer) {
        if (id==null||question==null||correctAnswer==null) {
            throw new IllegalArgumentException("Could not instantiate LabTaskQA with null values in constructor");
        }
        this.id = id;
        this.question = question;
        this.correctAnswer = correctAnswer;
    }

    public String getId() {
        return id;
    }

    public String getQuestion() {
        return question;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

}
