package com.tuneit.edx.lti.cources;

import java.util.Calendar;
import java.util.NoSuchElementException;
import java.util.StringTokenizer;

public class Task {

    private String  id; // yearOfStudy + studentId + labId + taskId + variant
    private String  yearOfStudy;
    private String  studentId;
    private String  labId;
    private String  taskId;
    private String  variant;
    private String  question;
    private boolean isComplete;
    private String  answer;
    private float   rating;

    public String getId() {
        if (id==null ) {
            //regeneration id if something changes
            //change getYearOfStudy() to yearOfStudy if you dont want default year
            //generation to current year
            if (getYearOfStudy()==null||studentId==null||
                    labId==null||taskId==null||variant==null) {
                //TODO maybe set up fake or defaults instead of exception
                throw new RuntimeException("Task ID could not be generated. Please fill required fields in Task");
            }
            id = yearOfStudy + "-" + studentId + ":" + 
                 labId + "-" + taskId +"-" + variant; 
        }
        return id;
    }

//    public Task setId(String id) {
//        this.id = id;
//        return this;
//    }

    public String getYearOfStudy() {
        if (yearOfStudy == null) {
            int educationYear1;
            int educationYear2;
            //set current education year
            Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            if (month>=8 ) {
                educationYear1=year;
                educationYear2=year+1;
            } else {
                educationYear1=year-1;
                educationYear2=year;
            }
            setYearOfStudy(Integer.toString(educationYear1)+"/"+Integer.toString(educationYear2));
        }
        return yearOfStudy;
    }

    public Task setYearOfStudy(String yearOfStudy) {
        StringTokenizer t = new StringTokenizer(yearOfStudy, "/");
        int year1=-1;
        int year2=-1;
        try {
            year1 = Integer.parseInt(t.nextToken());
            year2 = Integer.parseInt(t.nextToken());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Education year must be if form YYYY/YYYY+1 and YYYY must be a number");
        } catch (NoSuchElementException e) {
            throw new NumberFormatException("There has to be education year in form YYYY/YYYY+1 after ':'");
        }
        if (year1 != year2-1) {
            throw new NumberFormatException("Education year must be if form YYYY/YYYY+1");
        }
        if (year1 <2000 || year1>2050) {
            throw new NumberFormatException("Education year has to be more than 2000 and less than 2050");
        }
        //reassembly
        this.yearOfStudy = year1+"/"+year2;
        id = null;
        return this;
    }

    public String getStudentId() {
        return studentId;
    }

    public Task setStudentId(String studentId) {
        this.studentId = studentId;
        id = null;
        return this;
    }

    public String getLabId() {
        return labId;
    }

    public Task setLabId(String labId) {
        this.labId = labId;
        id = null;
        return this;
    }

    public String getTaskId() {
        return taskId;
    }

    public Task setTaskId(String taskId) {
        this.taskId = taskId;
        id = null;
        return this;
    }

    public String getVariant() {
        return variant;
    }

    public Task setVariant(String variant) {
        this.variant = variant;
        id = null;
        return this;
    }
    
    public String getQuestion() {
        return question;
    }

    public Task setQuestion(String question) {
        this.question = question;
        return this;
    }
    
    public boolean isComplete() {
        return isComplete;
    }

    public Task setComplete(boolean complete) {
        isComplete = complete;
        return this;
    }

    public String getAnswer() {
        return answer;
    }

    public Task setAnswer(String answer) {
        this.answer = answer;
        return this;
    }

    public float getRating() {
        return rating;
    }

    public Task setRating(float rating) {
        this.rating = rating;
        return this;
    }

    @Override
    public String toString() {
        return "Task{" + "id=" + id + ", yearOfStudy=" + yearOfStudy + 
               ", studentId=" + studentId + ", labId=" + labId + 
               ", taskId=" + taskId + ", variant=" + variant + ", question=" + question + 
               ", isComplete=" + isComplete + ", answer=" + answer + ", rating=" + rating + '}';
    }
    

}
