/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package sync.db.mysql;

import java.util.ArrayList;

/**
 *
 * @author Arvind Sasikumar
 */
public class TableMap {
    
    private final ArrayList<AttributeMap> attributeMapList;
    
    private final String sourceTable;
    private final String destinationTable;
    
    private final String sourceTimestampAttribute;
    private final String destinationTimestampAttribute;
    
    public TableMap(String sourceTable, String destinationTable, 
                    String sourceTimestampAttribute, String destinationTimeStampAttribute){
        
        this.sourceTable = sourceTable;
        this.destinationTable = destinationTable;
        
        this.sourceTimestampAttribute = sourceTimestampAttribute;
        this.destinationTimestampAttribute = destinationTimeStampAttribute;
        
        attributeMapList = new ArrayList<>();
    }
    
    public void addAttributeMap(AttributeMap attributeMap){
        
        attributeMapList.add(attributeMap);
    }
    
    public String getSourceTable(){
        
        return sourceTable;
    }
    
    public String getDestinationTable(){
        
        return destinationTable;
    }
    
    public String getSourceTimestampAttribute(){
        
        return sourceTimestampAttribute;
    }
    
    public String getDestinationTimestampAttribute(){
        
        return destinationTimestampAttribute;
    }
    
    public ArrayList<AttributeMap> getAttributeMap(){
        
        return attributeMapList;
    }
}
