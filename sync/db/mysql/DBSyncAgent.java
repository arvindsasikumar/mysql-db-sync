/*
 * The MIT License
 *
 * Copyright 2017 Arvind Sasikumar.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package sync.db.mysql;

import java.sql.*;

/**
 * An object of this class serves as the agent for the synchronization process.
 * All synchronization tasks are handed over to an object of this class, which 
 * then takes the required steps to perform the actual synchronization depending 
 * on its set properties. It requires all details for establishing a connection 
 * to the server and client MySQL servers and some additional synchronization 
 * specific properties.<p>
 * An object of this class is created using the Builder pattern as opposed to 
 * the usual constructor based initialization.<p>
 * An example on using the Builder pattern to create a new DBSyncObject:<p>
 * <pre>
 * {@code
 * DBSyncAgent dbSyncAgent = new DBSyncAgent.Builder()
                            .setServerDatabaseAddress("localhost")
                            .setServerDatabaseName("test1")
                            .setServerDatabaseUsername("root")
                            .setServerDatabasePassword("root")
                            .setServerDatabasePort(3306)
                            .setServerDatabaseConnectionOptions("?useSSL=false")
                            .setClientDatabaseAddress("localhost")
                            .setClientDatabaseName("test2")
                            .setClientDatabaseUsername("root")
                            .setClientDatabasePassword("root")
                            .setClientDatabasePort(3306)
                            .setClientDatabaseConnectionOptions("?useSSL=false")
                            .setDBMap(dbMap)
                            .setSyncInterval(12)
                            .build();
 * }
 * </pre>
 * @see #DBSyncAgent(sync.db.mysql.DBSyncAgent.Builder) 
 * @author Arvind Sasikumar
 */
public class DBSyncAgent {
    
    private final String serverDatabaseAddress;
    private final String serverDatabaseName;
    private final String serverDatabaseUsername;
    private final String serverDatabasePassword;
    private final String serverDatabaseConnectionOptions;
    private final int serverDatabasePort;
    
    private final String clientDatabaseAddress;
    private final String clientDatabaseName;
    private final String clientDatabaseUsername;
    private final String clientDatabasePassword;
    private final String clientDatabaseConnectionOptions;
    private final int clientDatabasePort;
    
    private final DBMap dbMap;
    
    private int syncInterval;
    
    private Connection serverConnection;
    private Connection clientConnection;
    
    private Statement serverStatement;
    private Statement clientStatement;
    
    private DBSynchronizer dbSynchronizer;
    
    private Thread dbSynchronizerThread;
    
    /**
     * Build the DBSyncAgent class using the Builder pattern.
     * @author Arvind Sasikumar
     * @see <a href="http://stackoverflow.com/questions/328496/when-would-you-use-the-builder-pattern">Builder Pattern</a>
     */
    public static class Builder{
        
        private String serverDatabaseAddress;
        private String serverDatabaseName;
        private String serverDatabaseUsername;
        private String serverDatabasePassword;
        private String serverDatabaseConnectionOptions;
        private int serverDatabasePort;
        
        private String clientDatabaseAddress;
        private String clientDatabaseName;
        private String clientDatabaseUsername;
        private String clientDatabasePassword;
        private String clientDatabaseConnectionOptions;
        private int clientDatabasePort;
        
        private DBMap dbMap;
        
        private int syncInterval;
        
        /**
         * Set the address of the server database.
         * @param serverDatabaseAddress address of the server database, 
         * e.g. "localhost" or "59.23.54.22"
         * @return Builder object as per the Builder pattern
         */
        public Builder setServerDatabaseAddress(String serverDatabaseAddress){
            
            this.serverDatabaseAddress = serverDatabaseAddress;
            return this;
        }
        
        /**
         * Set the name of the server database.
         * @param serverDatabaseName name of the server database, e.g. "testdb"
         * @return Builder object as per the Builder pattern
         */
        public Builder setServerDatabaseName(String serverDatabaseName){
            
            this.serverDatabaseName = serverDatabaseName;
            return this;
        }
        
        /**
         * Set the username to access the server database.
         * @param serverDatabaseUsername username to access the server database
         * @return Builder object as per the Builder pattern
         */
        public Builder setServerDatabaseUsername(String serverDatabaseUsername){
            
            this.serverDatabaseUsername = serverDatabaseUsername;
            return this;
        }
        
        /**
         * Set the password to access the server database.
         * @param serverDatabasePassword password to access the server database
         * @return Builder object as per the Builder pattern
         */
        public Builder setServerDatabasePassword(String serverDatabasePassword){
            
            this.serverDatabasePassword = serverDatabasePassword;
            return this;
        }
        
        /**
         * Set the optional connection string to be used for the server 
         * connection.
         * All options in the MySQL connection string as per the Connector/J 
         * appear here. e.g. "?useSSL=false"
         * @param serverDatabaseConnectionOptions optional connection string
         * @return Builder object as per the Builder pattern
         */
        public Builder setServerDatabaseConnectionOptions(String 
                                            serverDatabaseConnectionOptions){
            
            this.serverDatabaseConnectionOptions 
                                            = serverDatabaseConnectionOptions;
            return this;
        }
        
        /**
         * Set the port for the server connection.
         * @param serverDatabasePort port for the server connection, e.g. 3306
         * @return Builder object as per the Builder pattern
         */
        public Builder setServerDatabasePort(int serverDatabasePort){
            
            this.serverDatabasePort = serverDatabasePort;
            return this;
        }
        
        /**
         * Set the address of the client database.
         * @param clientDatabaseAddress address of the client database, 
         * e.g. "localhost" or "59.23.54.22"
         * @return Builder object as per the Builder pattern
         */
        public Builder setClientDatabaseAddress(String clientDatabaseAddress){
            
            this.clientDatabaseAddress = clientDatabaseAddress;
            return this;
        }
        
        /**
         * Set the name of the client database.
         * @param clientDatabaseName name of the client database, e.g. "testdb"
         * @return Builder object as per the Builder pattern
         */
        public Builder setClientDatabaseName(String clientDatabaseName){
            
            this.clientDatabaseName = clientDatabaseName;
            return this;
        }
        
        /**
         * Set the username to access the client database.
         * @param clientDatabaseUsername username to access the client database
         * @return Builder object as per the Builder pattern
         */
        public Builder setClientDatabaseUsername(String clientDatabaseUsername){
            
            this.clientDatabaseUsername = clientDatabaseUsername;
            return this;
        }
        
        /**
         * Set the password to access the client database.
         * @param clientDatabasePassword password to access the client database
         * @return Builder object as per the Builder pattern
         */
        public Builder setClientDatabasePassword(String clientDatabasePassword){
            
            this.clientDatabasePassword = clientDatabasePassword;
            return this;
        }
        
        /**
         * Set the optional connection string to be used for the client 
         * connection.
         * All options in the MySQL connection string as per the Connector/J 
         * appear here. e.g. "?useSSL=false"
         * @param clientDatabaseConnectionOptions optional connection string
         * @return Builder object as per the Builder pattern
         */
        public Builder setClientDatabaseConnectionOptions(String 
                                            clientDatabaseConnectionOptions){
            
            this.clientDatabaseConnectionOptions 
                                            = clientDatabaseConnectionOptions;
            return this;
        }
        
        /**
         * Set the port for the client connection.
         * @param clientDatabasePort port for the client connection, e.g. 3306
         * @return Builder object as per the Builder pattern
         */
        public Builder setClientDatabasePort(int clientDatabasePort){
            
            this.clientDatabasePort = clientDatabasePort;
            return this;
        }
        
        /**
         * Set the database map.
         * @param dbMap database map
         * @return Builder object as per the Builder pattern
         */
        public Builder setDBMap(DBMap dbMap){
            
            this.dbMap = dbMap;
            return this;
        }
        
        /**
         * Set the synchronization interval.
         * This specifies the time interval between two successive 
         * synchronization attempts during a live sync. Specified in seconds.
         * @param syncInterval synchronization interval in seconds
         * @return Builder object as per the Builder pattern
         */
        public Builder setSyncInterval(int syncInterval){
            
            this.syncInterval = syncInterval;
            return this;
        }
        
        /**
         * Build the DBSyncAgent object using the Builder pattern after all 
         * properties have been set using the Builder class.
         * @return a new DBSyncAgent object with all properties set
         * @see <a href="http://stackoverflow.com/questions/328496/when-would-you-use-the-builder-pattern">Builder Pattern</a>
         */
        public DBSyncAgent build(){
            
            return new DBSyncAgent(this);
        }
    }
    
    /**
     * Create a new DBSyncAgent object using the Builder Pattern.
     * This constructor is declared as private and is hence not accessible from 
     * outside. To use this constructor, one must use the Builder pattern.
     * @param builder the builder object used to build the new DBSyncAgent 
     * object using the Builder pattern
     * @see <a href="http://stackoverflow.com/questions/328496/when-would-you-use-the-builder-pattern">Builder Pattern</a>
     */
    private DBSyncAgent(Builder builder){
        
        serverDatabaseAddress = builder.serverDatabaseAddress;
        serverDatabaseName = builder.serverDatabaseName;
        serverDatabaseUsername = builder.serverDatabaseUsername;
        serverDatabasePassword = builder.serverDatabasePassword;
        serverDatabaseConnectionOptions = builder.serverDatabaseConnectionOptions;
        serverDatabasePort = builder.serverDatabasePort;
        
        clientDatabaseAddress = builder.clientDatabaseAddress;
        clientDatabaseName = builder.clientDatabaseName;
        clientDatabaseUsername = builder.clientDatabaseUsername;
        clientDatabasePassword = builder.clientDatabasePassword;
        clientDatabaseConnectionOptions = builder.clientDatabaseConnectionOptions;
        clientDatabasePort = builder.clientDatabasePort;
        
        dbMap = builder.dbMap;
        
        syncInterval = builder.syncInterval;
    }
    
    /**
      * Set the synchronization interval.
      * This specifies the time interval between two successive 
      * synchronization attempts during a live sync. Specified in seconds.
      * @param syncInterval synchronization interval in seconds
     */
    public void setSyncInterval(int syncInterval){
        
        this.syncInterval = syncInterval;
    }
    
    /**
     * Connects to the client and server databases as per the set properties.
     */
    public void connect(){
        
        try{
            
            System.out.println("\nAttempting connection...");
            Class.forName("com.mysql.jdbc.Driver");
            
            String connectionString = "jdbc:mysql://" + serverDatabaseAddress + ":" +
                                      serverDatabasePort + "/" + serverDatabaseName +
                                      serverDatabaseConnectionOptions;
            serverConnection = DriverManager.getConnection(connectionString,
                                serverDatabaseUsername,serverDatabasePassword);
            serverStatement = serverConnection.createStatement();
            
            connectionString = "jdbc:mysql://" + clientDatabaseAddress + ":" +
                               clientDatabasePort + "/" + clientDatabaseName +
                               clientDatabaseConnectionOptions;
            clientConnection = DriverManager.getConnection(connectionString,
                                clientDatabaseUsername,clientDatabasePassword);
            clientStatement = clientConnection.createStatement();
            System.out.println("Connection successful!");
        }
        
        catch(Exception e){
            
            e.printStackTrace();
        }         
    }
    
    /**
     * Performs initial synchronization between the client and the server 
     * databases.
     * When synchronization is done for the first time or after a reasonable 
     * interval of time, call this function before calling the {@link #liveSync()}.
     */
    public void sync(){
        
        dbSynchronizer = new DBSynchronizer(serverStatement, clientStatement, dbMap);
        dbSynchronizerThread = new Thread(dbSynchronizer);
        dbSynchronizerThread.start();
    }
    
    /**
     * Synchronizes the client and the server databases periodically.
     * This synchronization is done periodically as specified using 
     * {@link #setSyncInterval(int)} or initialized using the Builder pattern.
     */
    public void liveSync(){
        
        dbSynchronizer = new DBSynchronizer(serverStatement, clientStatement, 
                                                            dbMap, syncInterval);
        dbSynchronizerThread = new Thread(dbSynchronizer);
        dbSynchronizerThread.start();
    }
    
    /**
     * Stops the synchronization process.
     * The current transaction will be finished before the stop takes places 
     * to make sure the database is in proper state.
     */
    public void stopSync(){
        
        dbSynchronizer.stopSync();
    }
    
    /**
     * Disconnects the existing client and server connections safely.
     * Call this method after calling {@link #stopSync()}.
     */
    public void disconnect(){
        
        try{
            
            System.out.println("\nDisconnecting...");
            clientStatement.close();
            serverStatement.close();
            
            clientConnection.close();
            serverConnection.close();
            System.out.println("Disconnected.");
        }
        
        catch(Exception e){
            
            e.printStackTrace();
        }
    }
    
    /**
     * Waits for the worker thread to finish its job before proceeding forward.
     * Call this method between {@link #sync()} and {@link #liveSync()}.
     */
    public void hold(){
        
        try{
            
            dbSynchronizerThread.join();
        }
        catch(Exception e){
            
            e.printStackTrace();
        }
    }
}
