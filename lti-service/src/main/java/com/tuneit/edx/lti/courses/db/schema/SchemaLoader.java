package com.tuneit.edx.lti.courses.db.schema;

import java.util.ArrayList;
import java.util.ListIterator;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author serge
 */
public class SchemaLoader {
    private static final ArrayList<Schema> schemas = new ArrayList<>();
    
    static {
        //System.out.println("Loading underlying JDBC driver.");
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            Logger.getLogger(SchemaLoader.class.getName()).log(Level.SEVERE, null, e);
            System.err.println("Cannot continue without Postgresql JDBC driver.");
            //TODO - replace to coorect shutdown SpringBoot Application
            System.exit(-1);
        }
        //System.out.println("Done.");
        
        schemas.add(Schema.load("airbooking.xml"));
        //TODO add other schemas
    }
    
    // TODO - should refactor this -- need more dependency
    public static Schema getSchema(String yearOfStudy, String studentId) {
        if (yearOfStudy==null || studentId==null)
            throw new IllegalArgumentException("Cant get Schema for student and year of study. Null args.");
        int seed = (yearOfStudy+"-"+studentId).toUpperCase().hashCode();
        int schemaNo = (new Random(seed)).nextInt(schemas.size());
        return SchemaLoader.getSchema(schemaNo);
    }
    
    //
    // below are test methods, do not use in the production code!
    //
    public static Schema getSchema(int index) {
        return schemas.get(index);
    }
    
    public static int getSchemasCount() {
        return schemas.size();
    }
    
    public ListIterator<Schema> schemas () {
        return schemas.listIterator();
    }

}

// old style seed int getSchema generation. hashCode is much more simply
// keep it hear for posibility to use in future
//        long seed = 2928;
//        try {
//            MessageDigest md = MessageDigest.getInstance("MD5");
//            md.update(yearOfStudy.toUpperCase().getBytes());
//            md.update(studentId.toUpperCase().getBytes());
//            String md5 = DatatypeConverter.printHexBinary(md.digest()).toUpperCase();
//            seed = Long.parseUnsignedLong(md5.substring(0, 16), 16);
//            
//        } catch (NoSuchAlgorithmException|NumberFormatException ex) {
//            Logger.getLogger(SchemaLoader.class.getName()).log(Level.SEVERE, null, ex);
//        }