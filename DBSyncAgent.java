/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sync.db.mysql;

import java.sql.*;

/**
 * 
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
        
        public Builder setServerDatabaseAddress(String serverDatabaseAddress){
            
            this.serverDatabaseAddress = serverDatabaseAddress;
            return this;
        }
        
        public Builder setServerDatabaseName(String serverDatabaseName){
            
            this.serverDatabaseName = serverDatabaseName;
            return this;
        }
        
        public Builder setServerDatabaseUsername(String serverDatabaseUsername){
            
            this.serverDatabaseUsername = serverDatabaseUsername;
            return this;
        }
        
        public Builder setServerDatabasePassword(String serverDatabasePassword){
            
            this.serverDatabasePassword = serverDatabasePassword;
            return this;
        }
        
        public Builder setServerDatabaseConnectionOptions(String serverDatabaseConnectionOptions){
            
            this.serverDatabaseConnectionOptions = serverDatabaseConnectionOptions;
            return this;
        }
        
        public Builder setServerDatabasePort(int serverDatabasePort){
            
            this.serverDatabasePort = serverDatabasePort;
            return this;
        }
        
        public Builder setClientDatabaseAddress(String clientDatabaseAddress){
            
            this.clientDatabaseAddress = clientDatabaseAddress;
            return this;
        }
        
        public Builder setClientDatabaseName(String clientDatabaseName){
            
            this.clientDatabaseName = clientDatabaseName;
            return this;
        }
        
        public Builder setClientDatabaseUsername(String clientDatabaseUsername){
            
            this.clientDatabaseUsername = clientDatabaseUsername;
            return this;
        }
        
        public Builder setClientDatabasePassword(String clientDatabasePassword){
            
            this.clientDatabasePassword = clientDatabasePassword;
            return this;
        }
        
        public Builder setClientDatabaseConnectionOptions(String clientDatabaseConnectionOptions){
            
            this.clientDatabaseConnectionOptions = clientDatabaseConnectionOptions;
            return this;
        }
        
        public Builder setClientDatabasePort(int clientDatabasePort){
            
            this.clientDatabasePort = clientDatabasePort;
            return this;
        }
        
        public Builder setDBMap(DBMap dbMap){
            
            this.dbMap = dbMap;
            return this;
        }
        
        public Builder setSyncInterval(int syncInterval){
            
            this.syncInterval = syncInterval;
            return this;
        }
        
        public DBSyncAgent build(){
            
            return new DBSyncAgent(this);
        }
    }
    
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
    
    public void setSyncInterval(int syncInterval){
        
        this.syncInterval = syncInterval;
    }
    
    public void connect(){
        
        try{
            
            Class.forName("com.mysql.jdbc.Driver");
            
            String connectionString = "jdbc:mysql://" + serverDatabaseAddress + ":" +
                                      serverDatabasePort + "/" + serverDatabaseName +
                                      serverDatabaseConnectionOptions;
            serverConnection = DriverManager.getConnection(connectionString,serverDatabaseUsername,serverDatabasePassword);
            serverStatement = serverConnection.createStatement();
            
            connectionString = "jdbc:mysql://" + clientDatabaseAddress + ":" +
                               clientDatabasePort + "/" + clientDatabaseName +
                               clientDatabaseConnectionOptions;
            clientConnection = DriverManager.getConnection(connectionString,clientDatabaseUsername,clientDatabasePassword);
            clientStatement = clientConnection.createStatement();
        }
        
        catch(Exception e){
            
            e.printStackTrace();
        }         
    }
    
    public void sync(){
        
        dbSynchronizer = new DBSynchronizer(serverStatement, clientStatement, dbMap);
        Thread dbSynchronizerThread = new Thread(dbSynchronizer);
        dbSynchronizerThread.start();
    }
    
    public void liveSync(){
        
        dbSynchronizer = new DBSynchronizer(serverStatement, clientStatement, dbMap, syncInterval);
        Thread dbSynchronizerThread = new Thread(dbSynchronizer);
        dbSynchronizerThread.start();
    }
    
    public void stopSync(){
        
        dbSynchronizer.stopSync();
    }
}
