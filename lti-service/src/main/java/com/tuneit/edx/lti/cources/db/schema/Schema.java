package com.tuneit.edx.lti.cources.db.schema;

import com.tuneit.edx.lti.cources.db.Lab;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author serge
 */

@XmlRootElement(name="schema")
@XmlAccessorType(XmlAccessType.FIELD)
public class Schema {

    @XmlElement(name="connection")
    private SchemaConnection connection;
    
    @XmlElementWrapper(name="tables")
    @XmlElement(name="table")
    private List<Table> tables;
    
    @XmlElementWrapper(name="labs-config")
    @XmlElement(name="lab")
    private List<Lab> task;

    
    public static Schema load(String schemaName) {
        Schema sch = null;
        try {
            JAXBContext jc = JAXBContext.newInstance(Schema.class);
            InputStream is = Schema.class.getResourceAsStream(schemaName);
            if (is==null)
                throw new JAXBException("Could not get XML schema in application resourses");
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            sch = (Schema) unmarshaller.unmarshal(is);
            
            //Marshaller marshaller = jc.createMarshaller();
            //marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            //marshaller.marshal(sch, System.out);
        } catch (JAXBException ex) {
            Logger.getLogger(Schema.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalArgumentException("Schema "+schemaName+" could not be loaded", ex);
        }
        return sch;
    }
    
    public Connection getConnection() throws SQLException {
        if (connection == null) {
            throw new SQLException("Schema connection is not properly setup");
        }
        return connection.getConnection();
    }

    /**
     * @return the tables
     */
    public List<Table> getTables() {
        return tables;
    }

    /**
     * @param tables the tables to set
     */
    public void setTables(List<Table> tables) {
        this.tables = tables;
    }
    
    

}
