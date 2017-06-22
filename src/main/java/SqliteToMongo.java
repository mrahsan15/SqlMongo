
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.Mongo;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author ahsan
 */
public class SqliteToMongo {
    Mongo mongoconnect = new MongoConnect().connectMongo();
    SqliteConnect sqlite = new SqliteConnect();
    Connection SqliteConnect = sqlite.SqliteConnect();
    
    
    public SqliteToMongo(){
        //Name to Create Database in Mongo
        String DBName = sqlite.DBName;
        
        //Mongo DB setup
        DB db = mongoconnect.getDB(DBName.replace(".", ""));
        DBCollection collection;
        ArrayList CollectionNames = SqliteTables();
        BasicDBObject object = null;
        ArrayList columnNames;
            
        for (int i = 0; i < CollectionNames.size(); i++){
            //Creating collection if not available with the table name fetched
            try{
                collection = db.getCollection((String) CollectionNames.get(i));
            }catch(Exception ex){
                System.out.println("Error 201: "+ex);
                collection = db.createCollection((String) CollectionNames.get(i), null);
            }
            columnNames = ColumnNames(((String) CollectionNames.get(i)));
            String queryData = "SELECT * from "+((String) CollectionNames.get(i));
//            System.out.println("Query Data: "+queryData);
            try{
                Statement ColumnStatement = SqliteConnect.createStatement();
                ResultSet ColumnSet = ColumnStatement.executeQuery(queryData);
                int counter = 0;
                while(ColumnSet.next()){
                    counter++;
                    object = new BasicDBObject();
                    for(int j= 0; j < columnNames.size(); j++){
//                        System.out.println("Column Size for "+((String) CollectionNames.get(i))+": "+columnNames.size());
                        //Creating Object with values inserted
                        String ColumnName = ((String)columnNames.get(j));
                        String ColumnValue = ColumnSet.getString((String) columnNames.get(j));
//                        System.out.println(ColumnName +" : "+ ColumnValue);
                        object.put(ColumnName, ColumnValue);
                    }
                    collection.insert(object);
                }
//                System.out.println("Counter: "+counter);
            }catch(Exception ex){
                System.out.println("Error 202: "+ex);
            }
        }
    }
    
    private ArrayList SqliteTables(){
//        String[] TableNames = null;
        ArrayList TableNames = new ArrayList();
        
        //Get the List of Table Names
        String query = "SELECT name FROM sqlite_master WHERE type='table'";
        try{
            Statement statement = SqliteConnect.createStatement();
            ResultSet rs = statement.executeQuery(query);
            while(rs.next()){
//                System.out.println(rs.getString("name"));
                TableNames.add(rs.getString("name"));
            }
        
        }catch(Exception ex){
            System.out.println("Error 204: "+ex);
        }
        return TableNames;
    }
    private ArrayList ColumnNames(String TableName){
        //ArrayList to store ColumnNames in Array
        ArrayList columnNames = new ArrayList();
        Statement statement;
        ResultSet rs;
        try{
            statement = SqliteConnect.createStatement();
            String Query = "SELECT sql FROM sqlite_master\n" +
"WHERE tbl_name = '"+TableName+"' AND type = 'table'";
//            System.out.println("Query: "+Query);
            rs = statement.executeQuery(Query);
            
            while(rs.next()){
                //Getting Raw Query
                String rawquery  = rs.getString("sql");
                //Extracting Comma Separated Values
                String rawquery1 = rawquery.substring(rawquery.indexOf("(")+1, rawquery.indexOf(")"));
                //Splitting Column Names
                String[] rawquery2 = rawquery1.split(", ");
                for(int i = 0 ; i < rawquery2.length; i++){
                    System.out.println(rawquery2[i].split(" ")[0]);
                    if(rawquery2[i].split(" ")[0].contains(",")){
                        //White Spaces Management
                        for(int j = 0; j < rawquery2[i].split(",").length; j++){
//                            System.out.println(rawquery2[i].split(",")[j]);
                            columnNames.add(rawquery2[i].split(",")[j]);
                        }
                    }else{
                        columnNames.add(rawquery2[i].split(" ")[0]);
                    }
                }
            }
        }catch(Exception ex){
            System.out.println("Error 203: "+ex);
        }
        return columnNames;
    }
}
