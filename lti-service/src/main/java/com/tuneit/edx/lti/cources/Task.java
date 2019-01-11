package com.tuneit.edx.lti.cources;

public interface Task {

    String getQuestion();
    void setQuestion(String arg);

    long getId();

    boolean isComplete();
    void setComplete(boolean completeFlag);

    String getAnswer();
    void setAnswer(String arg);

    float getRating();
    void setRating(float rating);

}
