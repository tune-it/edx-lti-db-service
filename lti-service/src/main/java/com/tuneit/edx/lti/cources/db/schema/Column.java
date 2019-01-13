package com.tuneit.edx.lti.cources.db.schema;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author serge
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Column {
    
    @XmlAttribute(name="sqlName") private String sqlName;
    @XmlAttribute(name="name") private String name;

    @Override
    public String toString() {
        return "Column{" + "sqlName=" + sqlName + ", name=" + name + '}';
    }

    public String getColumnName() {
        return sqlName;
    }

    public void setColumnName(String sqlName) {
        this.sqlName = sqlName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    
    
}
