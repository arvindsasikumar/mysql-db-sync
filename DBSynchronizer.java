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
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.lang.Exception;
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
   
    private final ScheduledExecutorService exec;
    
    public DBSynchronizer(Statement serverStatement, Statement clientStatement, DBMap dbMap){
        
        this.serverStatement = serverStatement;
        this.clientStatement = clientStatement;
        this.dbMap = dbMap;
        
        syncInterval = 0;
        
        syncType = SyncType.SYNC;
        
        isRunning = true;
        
        exec = null;
    }
    
    public DBSynchronizer(Statement serverStatement, Statement clientStatement, DBMap dbMap, int syncInterval){
        
        this.serverStatement = serverStatement;
        this.clientStatement = clientStatement;
        this.dbMap = dbMap;
        
        this.syncInterval = syncInterval;
        
        syncType = SyncType.LIVE_SYNC;
        
        isRunning = true;
        
        exec = Executors.newSingleThreadScheduledExecutor();
    }
    
    public void run(){
        
        if(syncType == SyncType.SYNC){
            
            sync();
        }
        
        else if(syncType == SyncType.LIVE_SYNC){
            
            liveSync();
        }
    }
    
    /**
     * Gets the last synchronized timestamp value.
     * @param destinationTable destination table name
     * @param destinationTimestampAttribute destination timestamp attribute name
     * @return last synchronization timestamp, '0000-00-00 00:00:00' if first time
     */
    private String getLastSyncTimestamp(String destinationTable, 
                                        String destinationTimestampAttribute){
        
        String lastSyncTimestamp = null;
        
        try {
            
            ResultSet rs = clientStatement.executeQuery("Select max(" + 
                                                destinationTimestampAttribute + 
                                                ") from " + destinationTable);
            
            while(rs.next()){
                
                lastSyncTimestamp = rs.getString(1);
            }
            
            if(lastSyncTimestamp == null){
                
                lastSyncTimestamp = "0000-00-00 00:00:00";
            }
            
            rs.close();
        } 
        
        catch (Exception e) {
            
            e.printStackTrace();
        }
        
        return lastSyncTimestamp;
    }
    
    private String generateSelectQuery(String table, ArrayList<AttributeMap> attributeMap,
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
        return selectQuery.toString();
    }
    
    private String generateUpdateQuery(ResultSet rs, String table, ArrayList<AttributeMap> attributeMap) throws SQLException{
        
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
                
                serverResultSet.close();
            }
            
            catch(Exception e){
                
                e.printStackTrace();
            }
        }
    }
    
    private void liveSync() {
        
        exec.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                sync();
            }
        },syncInterval, syncInterval, TimeUnit.SECONDS);
    }
    
    protected void stopSync(){
        
        isRunning = false;
        exec.shutdown();
    }
}
