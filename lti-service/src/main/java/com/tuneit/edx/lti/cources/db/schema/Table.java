package com.tuneit.edx.lti.cources.db.schema;

import java.util.List;
import javax.xml.bind.annotation.*;

/**
 *
 * @author serge
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Table {
    
    @XmlAttribute(name="sql-name") private String sqlName;
    @XmlAttribute(name="name") private String name;
    
    @XmlElement(name="column")
    private List<Column> columns;

    @Override
    public String toString() {
        return "Table{" + "sqlName=" + sqlName + ", name=" + name + ", columns=" + columns + '}';
    }

    public String getTableName() {
        return sqlName;
    }

    public void setTableName(String sqlName) {
        this.sqlName = sqlName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }
    



}
