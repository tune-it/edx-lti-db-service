package com.tuneit.edx.lti.cources.db;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;


/**
 *
 * @author serge
 */
@XmlAccessorType(XmlAccessType.NONE)
public abstract class Lab { 
    
    @XmlAttribute(name="description") protected String description;    
    @XmlAttribute(name="id") protected String id;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
    
    @Override
    public String toString() {
        return "Lab{" + "name=" + description + '}';
    }
    
    public abstract List<LabTask> getLabTask();



}
