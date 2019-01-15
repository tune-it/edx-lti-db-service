package com.tuneit.edx.lti.cources.db;

import com.tuneit.edx.lti.cources.Task;
import com.tuneit.edx.lti.cources.db.schema.Schema;
import com.tuneit.edx.lti.cources.db.schema.SchemaLoader;
import com.tuneit.edx.lti.cources.db.schema.Table;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.DatatypeConverter;



/**
 *
 * @author serge
 */
public class SelectProcessor {


    
    public static void main(String[] args) throws Exception {
        SelectProcessor st = new SelectProcessor();
        Schema s = SchemaLoader.getSchema(0);
        for (Lab t : s.getLabs()) {
            System.out.println(t);
        }
        DBTaskGenerationService ds = new DBTaskGenerationService();
        Task tasks[] = ds.getTasks("serge@cs.ifmo.ru", "lab02", "00", 0);
        tasks[0].setAnswer("select * from ticket_flights;").setComplete(true);
        tasks[1].setAnswer("select timezone, airport_code, city, airport_name from airports;").setComplete(true);
        ds.checkTasks(tasks);
        for (Task t : tasks) {
            System.out.println(t);
        }
        //for (Table t : s.getTables()) {
        //    System.out.println(t);
        //}
        //StringBuilder sb = new StringBuilder();
        //System.out.println("query md5 = "+st.execute_select(s,"select * from airports", -1, sb));
        //System.out.println(sb);
    }
    
    //private static ResultsHashStore results = new ResultsHashStore();;
    private static String session_timeout_command = 
            "SET statement_timeout = 10000;";
    
    
    public String execute_select(Schema schema, String sql) {
            return execute_select(schema,sql,100,null);
    }
    
    private static final String TABLE_CLASSES = "dummy-table";
    private static final String ROW_CLASSES = "dummy-row";
    private static final String CELL_CLASSES = "dummy-cell";
    private static final String HEADER_CLASSES = "dummy-header";
    /**
     * 
     * @param schema - schema name to generate queries
     * @param sql - sql to execute
     * @param row_limit Limits row in output, 0 - zero rows, -1 unlimited
     * @param ho - html_output pass new StringBuilder to fill up with html elements
     * @return result's md5 hash in string representation
     */
    public String execute_select(Schema schema, String sql, int row_limit, StringBuilder ho ) {
        
        //TODO error sign is null;
        //String query_md5 = "Error at executing query";
        String query_md5 = null;
        boolean do_html_output = false;
        if (ho !=null) {
            do_html_output = true;
            ho.append("<table id=sqlresult class=\"").append(TABLE_CLASSES).append("\">\n");
        }
        
        Connection conn = null;
        Statement stmt = null;
        ResultSet rset = null;
        SQLException query_exception = null;
        try 
        {
            // we are count only first five rows in select
            int row_counter = row_limit;
            // TODO probably should change in future on quicker alg or on procedure in DB
            MessageDigest md = MessageDigest.getInstance("MD5");

            //System.out.println("Creating connection.");
            conn = schema.getConnection();
            //System.out.println("Creating statement.");
            stmt = conn.createStatement();
            stmt.execute(session_timeout_command);
            //System.out.println("Executing statement.");
            rset = stmt.executeQuery(sql);
            //System.out.println("Results:");
            ResultSetMetaData rsmd = rset.getMetaData();
            int numcols = rsmd.getColumnCount();
            //headers
            if (do_html_output) {
                ho.append("<tr class=\"").append(ROW_CLASSES).append("\">");
                for(int i=1;i<=numcols;i++) {
                    String fieldLabel = rsmd.getColumnLabel(i);
                    ho.append("<th class=\"").append(HEADER_CLASSES).append("\">")
                      .append(fieldLabel).append("</th>");
                }
                ho.append("</tr>\n");
            }
            while(rset.next() && row_counter-- != 0 ) {
                if (do_html_output) 
                    ho.append("<tr class=\"").append(ROW_CLASSES).append("\">");
                for(int i=1;i<=numcols;i++) {
                    String field = rset.getString(i);
                    md.update(field.getBytes());
                    if (do_html_output) 
                        ho.append("<td class=\"").append(CELL_CLASSES).append("\">")
                          .append(field).append("</td>");
                    //System.out.print("\t" + field);
                }
                if (do_html_output)
                    ho.append("</tr>\n");
                //System.out.println("");
            }
            byte[] digest = md.digest();
            query_md5 = DatatypeConverter.printHexBinary(digest).toUpperCase();
            
        } catch(SQLException e) {
            query_exception = e;
            //Logger.getLogger(SelectProcessor.class.getName()).log(Level.SEVERE, null, e);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(SelectProcessor.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try { if (rset != null) rset.close(); } catch(Exception e) { }
            try { if (stmt != null) stmt.close(); } catch(Exception e) { }
            try { if (conn != null) conn.close(); } catch(Exception e) { }
        }
        if (do_html_output) {
            if (query_exception != null) {
                ho.delete(0, ho.length());
                ho.append("<H1>").append(query_exception).append("</H1>");
            } else {
                ho.append("</table>\n");
            }
        }
        return query_md5;
    }
    


}
