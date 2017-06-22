
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import java.util.List;

/**
 *
 * @author ahsan
 */
public class MongoConnect {
    private Mongo mongo = null;
    
    public MongoConnect(){
        
    }
    
    public Mongo connectMongo(){
        try{
            //Connect Mongo Server
            mongo   = new Mongo("localhost",27017);
        }catch(Exception ex){
            System.out.println(ex);
        }
        return mongo;
    }
    
    
    
}