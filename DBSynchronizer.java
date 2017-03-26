/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sync.db.mysql;

import java.sql.*;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Arvind Sasikumar
 */
public class DBSynchronizer implements Runnable{
    
    private final Statement serverStatement;
    private final Statement clientStatement;
    
    private final DBMap dbMap;
    
    private final int syncInterval;
    
    private final SyncType syncType;
    
    private volatile boolean isRunning;
    
    public DBSynchronizer(Statement serverStatement, Statement clientStatement, DBMap dbMap){
        
        this.serverStatement = serverStatement;
        this.clientStatement = clientStatement;
        this.dbMap = dbMap;
        
        syncInterval = 0;
        
        syncType = SyncType.SYNC;
        
        isRunning = true;
    }
    
    public DBSynchronizer(Statement serverStatement, Statement clientStatement, DBMap dbMap, int syncInterval){
        
        this.serverStatement = serverStatement;
        this.clientStatement = clientStatement;
        this.dbMap = dbMap;
        
        this.syncInterval = syncInterval;
        
        syncType = SyncType.LIVE_SYNC;
        
        isRunning = true;
    }
    
    public void run(){
        
        if(syncType == SyncType.SYNC){
            
            sync();
        }
        
        else if(syncType == SyncType.LIVE_SYNC){
            
            liveSync();
        }
    }
    
    private String getLastSyncTimestamp(String destinationTable, String destinationTimestampAttribute){
        
        String lastSyncTimestamp = null;
        
        try {
            
            ResultSet rs = clientStatement.executeQuery("Select max(" + destinationTimestampAttribute +
                                                        ") from " + destinationTable);
            
            while(rs.next()){
                
                lastSyncTimestamp = rs.getString(1);
            }
            
            if(lastSyncTimestamp == null){
                
                lastSyncTimestamp = "0000-00-00 00:00:00";
            }
        } 
        
        catch (Exception e) {
            
            e.printStackTrace();
        }
        
        return lastSyncTimestamp;
    }
    
    public String generateSelectQuery(String table, ArrayList<AttributeMap> attributeMap,
                                      String timestampAttribute, String lastSyncTimestamp){
        
        StringBuilder selectQuery = new StringBuilder("select ");
        for(int i = 0; i < attributeMap.size(); i++){
            
            selectQuery.append(attributeMap.get(i).getSourceAttribute());
            if(i != attributeMap.size() - 1){
                
                selectQuery.append(", ");
            }
        }
        selectQuery.append(" from ").append(table).append(" where ")
                   .append(timestampAttribute).append(" > ")
                   .append(" '").append(lastSyncTimestamp).append("'");
        System.out.println(selectQuery.toString());
        return selectQuery.toString();
    }
    
    public String generateUpdateQuery(ResultSet rs, String table, ArrayList<AttributeMap> attributeMap) throws SQLException{
        
        StringBuilder updateQuery = new StringBuilder("insert into  ").append(table).append("(");
        for(int i = 0; i < attributeMap.size(); i++){
            
            updateQuery.append(attributeMap.get(i).getDestinationAttribute());
            if(i != attributeMap.size() - 1){
                updateQuery.append(",");
            }
        }
        updateQuery.append(") values (");
        for(int i = 0; i < attributeMap.size(); i++){
            
            if(attributeMap.get(i).getType() == AttributeType.STRING){
                
                updateQuery.append("'");
            }
            updateQuery.append(rs.getString(i+1));
            if(attributeMap.get(i).getType() == AttributeType.STRING){
                
                updateQuery.append("'");
            }
            if(i != attributeMap.size() - 1){
                updateQuery.append(",");
            }
        }
        updateQuery.append(")");
        System.out.println(updateQuery.toString());
        return updateQuery.toString();
    }
    
    private void sync(){
        
        for(TableMap tableMap : dbMap.getTableMap()){
            
            String sourceTable = tableMap.getSourceTable();
            String destinationTable = tableMap.getDestinationTable();
            ArrayList<AttributeMap> attributeMap = tableMap.getAttributeMap();
            String sourceTimestampAttribute = tableMap.getSourceTimestampAttribute();
            String destinationTimestampAttribute = tableMap.getDestinationTimestampAttribute();
            
            String lastSyncTimestamp = getLastSyncTimestamp(destinationTable, destinationTimestampAttribute);
            String selectQuery = generateSelectQuery(sourceTable, attributeMap, sourceTimestampAttribute, lastSyncTimestamp);
            
            ResultSet serverResultSet;
            
            try{
                
                serverResultSet = serverStatement.executeQuery(selectQuery);
                while(serverResultSet.next() && isRunning){                        
                        
                        String updateQuery = generateUpdateQuery(serverResultSet, destinationTable, attributeMap);
                        clientStatement.executeUpdate(updateQuery);
                }
            }
            
            catch(Exception e){
                
                e.printStackTrace();
            }
        }
    }
    
    private void liveSync(){
        
        ScheduledExecutorService exec = Executors.newSingleThreadScheduledExecutor();
        exec.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                sync();
            }
        },syncInterval, syncInterval, TimeUnit.SECONDS);
    }
    
    protected void stopSync(){
        
        isRunning = false;
    }
}
