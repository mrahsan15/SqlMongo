import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;

/**
 *
 * @author ahsan
 */
public class SqliteConnect {
    private Connection connection;
    public String DBName;
    public SqliteConnect(){
        
    }
    
    
    public Connection SqliteConnect(){
        try {
            Class.forName("org.sqlite.JDBC");
            File file;
            JFileChooser filechooser = new JFileChooser("Select Sqlite Database");
            filechooser.showOpenDialog(filechooser);
            file = filechooser.getSelectedFile();
            DBName = file.getName();
            connection = DriverManager.getConnection("jdbc:sqlite:"+file.getAbsolutePath());
        }catch(Exception ex){
            System.out.println(ex);
        }
        return connection;
    }
}
