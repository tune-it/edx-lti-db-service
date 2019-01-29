package com.tuneit.edx.lti.courses.db;

import com.tuneit.edx.lti.courses.Task;
import com.tuneit.edx.lti.courses.db.schema.Column;
import com.tuneit.edx.lti.courses.db.schema.Schema;
import com.tuneit.edx.lti.courses.db.schema.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;

/**
 *
 * @author serge
 */
@XmlAccessorType(XmlAccessType.NONE)
public class Lab02 extends Lab {
    
    @XmlElements({
        @XmlElement(name = "task01", type = Task01.class),
        @XmlElement(name = "task02", type = Task02.class),
        @XmlElement(name = "task03", type = Task03.class),
        @XmlElement(name = "task04", type = Task04.class),

    })
    private List<LabTask> labTask = new ArrayList<>();

    @Override
    public List<LabTask> getLabTask() {
        return labTask;
    }

    public void setLabTask(List<LabTask> labTask) {
        this.labTask = labTask;
    }

    @Override
    public String toString() {
        return "Lab02{" + super.toString()+", labTask=" + labTask + '}';
    }

}


@XmlAccessorType(XmlAccessType.NONE)
class Task01 extends LabTask {
    @XmlElement(name = "forbiden-table")
    protected List<String> forbidenTables = new ArrayList<>();

    public List<String> getForbidenTables() {
        return forbidenTables;
    }

    public void setForbidenTables(List<String> forbidenTables) {
        this.forbidenTables = forbidenTables;
    }

    @Override
    public String toString() {
        return "Task01{" + super.toString()+ ", forbidenTables=" + forbidenTables + '}';
    }

    @Override
    public LabTaskQA generate(Schema s, Task t) {
        List<Table> tables = s.getTables();
        Random r = getRandom(t);
        r.nextBoolean();
        String tableName = null;
        int break_count = tables.size()+2;
        while(tableName == null) {
            tableName = tables.get(r.nextInt(tables.size())).getTableName();
            boolean forbidenTable = false;
            for (String ft : getForbidenTables()) {
                if (tableName.equalsIgnoreCase(ft)) {
                    forbidenTable = true;
                    break;
                }
            }
            if (forbidenTable) {
                tableName = null;
                break_count--;
            }
            if (break_count<=0) {
                throw new IllegalStateException("Could not generate Task as all found tables are in forbiden list");
            }
        }

        return new LabTaskQA(t.getId(), getProlog()+tableName+getEpilog(),
                             "SELECT * FROM "+tableName+";");
    }
}

@XmlAccessorType(XmlAccessType.NONE)
class Task02 extends LabTask {
    @Override
    public LabTaskQA generate(Schema s, Task t) {
        StringBuilder qb = new StringBuilder();
        StringBuilder ab = new StringBuilder();
        List<Table> tables = s.getTables();
        Random r = getRandom(t);
        r.nextBoolean();
        int tableIdx = r.nextInt(tables.size()); 
        Table table = tables.get(tableIdx);
        List<Column> columns = table.getColumns();
        int colCnt = columns.size();
        int colNum = r.nextInt(colCnt-1)+1;
        qb.append(getProlog());
        ab.append("SELECT ");
        ArrayList<Integer> used_columns = new ArrayList<Integer>();
        for(int j=0;j<colNum;j++) {
            int colIdx = r.nextInt(colCnt);
            if (used_columns.contains(colIdx)) {
                j--;
                continue;
            }
            used_columns.add(colIdx);
            if (j!=0) { 
                qb.append(", ");
                ab.append(", ");
            }
            qb.append(columns.get(colIdx).getColumnName());
            ab.append(columns.get(colIdx).getColumnName());
        }
        qb.append(getEpilog()).append(table.getTableName()).append('.');
        ab.append(" FROM ").append(table.getTableName()).append(';');
        
        return new LabTaskQA(t.getId(), qb.toString(), ab.toString());
    }
}

@XmlAccessorType(XmlAccessType.NONE)
class Task03 extends LabTask {
    @Override
    public LabTaskQA generate(Schema s, Task t) {
        return new LabTaskQA(t.getId(), "dummyQuestion", "dummyAnswer");
    }
}

@XmlAccessorType(XmlAccessType.NONE)
class Task04 extends LabTask {
    @Override
    public LabTaskQA generate(Schema s, Task t) {
        return new LabTaskQA(t.getId(), "dummyQuestion", "dummyAnswer");
    }
}