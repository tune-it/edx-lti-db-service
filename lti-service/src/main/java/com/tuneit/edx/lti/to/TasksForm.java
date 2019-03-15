package com.tuneit.edx.lti.to;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TasksForm {

    private String textQuery;

    // temporary workaround
    private String textQuery2;
    private String textQuery3;
    private String textQuery4;
    private String textQuery5;
    private String textQuery6;
    private String textQuery7;
    private String textQuery8;
    private String textQuery9;
    private String textQuery10;
    private String textQuery11;
    private String textQuery12;

    public String[] asArray() {
        return new String[]{textQuery, textQuery2, textQuery3, textQuery4, textQuery5, textQuery6,
                textQuery7, textQuery8, textQuery9, textQuery10, textQuery11, textQuery12};
    }
}
