# MySQL Database Synchronizer
MySQL Database Synchronizer is a library that aids the synchronization of two MySQL databases. Selected attributes of selected tables can by synchronized between the server and the client MySQL databases in both real-time and as a one-off instance.

This library also supports a custom database name mapping, i.e. database, table and attribute names of the records that are to be synchronized can be different in the server and the client databases. Also, which tables and which attributes within these tables are to be synchronized can also be decided, i.e. the entire database or an entire table need not be synchronized as a whole. This can be useful when only a part of the database requires synchronization.

This library uses 'timestamps' occuring in both the server and the client databases to decide which records have not yet been synchronized. Hence, all tables that need synchronization must have one attribute that records the servertime at the time of record entry.

Example Code:

```java
import sync.db.mysql.*; //Import the MySQLDBSync.jar file into your project's lib folder first.
import java.util.Scanner;

public class Test{

  public static void main(String args[]){
  
    DBMap dbMap = new DBMap(); //Creates a new DBMap object. The DBMap objects lets you create a mapping between source and destination MySQL Server table names and attribute names of these tables.
    TableMap tableMap = new TableMap("info","new_info","servertime","servertime"); //Creates a new TableMap object. The TableMap objects lets you create a mapping between table names and attribute names withing these tables. This will become a part of the DBMap object we created earlier. The first parameter specifies the table name of the source table while the second parameter specifies the corresponding table name of the destination table. The third parameter specifies the name of the timestamp attribute of the source table which the synchronization will be based on and the last parameters specifies the corresponding timestamp attribute's name.
    tableMap.addAttributeMap(new AttributeMap("name","fullname",AttributeType.STRING)); //Creates a new AttributeMap object that maps an attribute name between these tables. Here, the source table has an attribute called 'name', which is mapped to the attribute called 'fullname' in the destination table. The third parameter, AttributeType.STRING tells what type of attribute is being mapped; for e.g. varchar, char, timestamp, etc. are of type AttributeType.STRING whereas float, int, double etc. are of type AttributeType.NUMERICAL.
    tableMap.addAttributeMap(new AttributeMap("servertime","servertime",AttributeType.STRING)); //The synchronizing timestamp attribute of the source and destination tables as specified in the TableMap object creation above is important and need to be separately mapped.
    dbMap.addTableMap(tableMap); //Add this newly created table map into the DBMap object.
    
    tableMap = new TableMap("orders","orders","servertime","clienttime"); //Resuse the same TableMap object created above to create a fresh TableMap object and add AttributeMaps to it. Finally, add this table map to the initially created DBMap.
    tableMap.addAttributeMap("details","details",AttributeType.STRING);
    tableMap.addAttributeMap("id","orderid",AttributeType.NUMERICAL);
    tableMap.addAttributeMap("servertime","clienttime",AttributeType.STRING);
    dbMap.addTableMap(tableMap); //We are now donw with the mapping procedure.
    
    //Before we can start the synchronization, we need to establish a connection between the server and the client MySQL databases. This, and the actual syncronization is handled by the class DBSyncAgent. An object of this class is built using a standard Builder Pattern.
    
    DBSyncAgent dbSyncAgent = new DBSyncAgent.Builder()
                              .setServerDatabaseAddress("56.23.53.87") //Set the MySQL server database address here. This can also be a domain name, e.g. xyz.com where the server database is hosted. Also, this value can be localhost.
                              .setServerDatabaseName("test1") //Set the MySQL server database name from which to synchronize the client database.
                              .setServerDatabaseUsername("user123") //Set the MySQL server database username for connection.
                              .setServerDatabasePassword("1234") //Set the MySQL server's corresponding password.
                              .setServerDatabasePort(3306) //Set the port on which the server database listens, usually 3306.
                              .setServerDatabaseConnectionOptions("?useSSL=false") //All additional connection options to be passed here. If none, set this string to "".
                              .set.setClientDatabaseAddress("localhost") //Now do the same for the MySQL client database.
                              .setClientDatabaseName("test2")
                              .setClientDatabaseUsername("user567")
                              .setClientDatabasePassword("9878")
                              .setClientDatabasePort(3306)
                              .setClientDatabaseConnectionOptions("?useSSL=false")
                              .setDBMap(dbMap) //Pass the DBMap object created earlier
                              .setSyncInterval(20) //Specifies the time interval in which successive synchronization takes places, in seconds. This parameter is the only one that is not necessary to be set here. Also, this is useful only for live synchronization and otherwise can be omitted for one-off synchronization.
                              .build(); //Builds a new dbSyncAgent using the properties provided using the Builder Pattern.
                              
    dbSyncAgent.connect(); //Establish connection to server and client databases.
    dbSyncAgent.sync(); //Call this when synchronizing for first time, even if live synchronization is required. If the latter is the case, call the method for live synchronization immediately after this.
    dbSyncAgent.hold(); //Wait for the sync() method to do finish its job.
    dbSyncAgent.liveSync(); //Activates live sync. Make sure syncInterval() method has been used or the sync interval property was set while building the DBSyncObject. So not use this method if the client server has been idle for a while or is starting for the first time, especially if the server receives a lot of entires within this period. In this case, call the sync() function method, followed by this method.
    
    System.out.println("Enter a command:");
    Scanner scanner = new Scanner(System.in);
    String s = scanner.next(); //The synchronization happens in a new Thread. This main thread is free to receive commands. Use your standard input to enter the command 'end' to end the synchronization. This will end all synchronization activities safely.
    
    if(s.equals("end")){
      dbAgent.stopSync(); //Call this method to stop the synchronization safely.
      dbAgent.disconnect(); //Call this method after the stopSync() method has been called. Do not call this method without calling the stopSync() method.
    }
  }
}
```
Starting from v2.0, now you can set up database mapping using a custom markup language called the Database Mapping Markup Language (DMML). Once you have a dmml file, to get a DBMap object, simply call:

```java
DBMap dbMap = DBMap.getDBMapFromFile("dmml_file_path");
```

DMML file is very easy to generate. For help on generating a .dmml file, check out the DMMLGuide and sample.dmml.
