/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sync.db.mysql;

/**
 * 
 * @author Arvind Sasikumar
 */
public class DBSyncAgent {
    
    private final String serverDatabaseAddress;
    private final String serverDatabaseName;
    private final String serverDatabaseUsername;
    private final String serverDatabasePassword;
    
    private final String clientDatabaseAddress;
    private final String clientDatabaseName;
    private final String clientDatabaseUsername;
    private final String clientDatabasePassword;
    
    private final int syncInterval;
    
    public class Builder{
        
        private String serverDatabaseAddress;
        private String serverDatabaseName;
        private String serverDatabaseUsername;
        private String serverDatabasePassword;
        
        private String clientDatabaseAddress;
        private String clientDatabaseName;
        private String clientDatabaseUsername;
        private String clientDatabasePassword;
        
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
        
        clientDatabaseAddress = builder.clientDatabaseAddress;
        clientDatabaseName = builder.clientDatabaseName;
        clientDatabaseUsername = builder.clientDatabaseUsername;
        clientDatabasePassword = builder.clientDatabasePassword;
        
        syncInterval = builder.syncInterval;
    }
    
    public void sync(){
        
    }
    
    public void liveSync(){
    
    }
}
