# MySQL Database Synchronizer
MySQL Database Synchronizer is a library that aids the synchronization of two MySQL databases. Selected attributes of selected tables can by synchronized between the server and the client MySQL databases in both real-time and as a one-off instance.

This library also supports a custom database name mapping, i.e. database, table and attribute names of the records that are to be synchronized can be different in the server and the client databases. Also, which tables and which attributes within these tables are to be synchronized can also be decided, i.e. the entire database or an entire table need not be synchronized as a whole. This can be useful when only a part of the database requires synchronization.

This library uses 'timestamps' occuring in both the server and the client databases to decide which records have not yet been synchronized. Hence, all tables that need synchronization must have one attribute that records the servertime at the time of record entry.

Example Code:

```java
import sync.db.mysql.*; //Import the MySQLDBSync.jar file into your project's lib folder first.

public class Test{

  public static void main(String args[]){
  
    DBMap dbMap = new DBMap(); //Creates a new DBMap object. The DBMap objects lets you create a mapping between source and destination MySQL Server table names and attribute names of these tables.
    TableMap tableMap = new TableMap(); //Creates a new TableMap object. The TableMap objects lets you create a mapping between table names and attribute names withing these tables. This will become a part of the DBMap object we created earlier.
    tableMap.addAttributeMap(new AttributeMap("name","fullname",AttributeType.STRING)); //Creates a new AttributeMap object that maps an attribute name between these tables. Here, the source table has an attribute called 'name', which is mapped to the attribute called 'fullname' in the destination table. The third parameter, AttributeType.STRING tells what type of attribute is being mapped; for e.g. varchar, char, timestamp, etc. are of type AttributeType.STRING whereas float, int, double etc. are of type AttributeType.NUMERICAL.
    tableMap.addAttributeMap(new AttributeMap("servertime","servertime",AttributeType.STRING));
  }
}
```
