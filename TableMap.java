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

import java.util.ArrayList;

/**
 * An object of the class maps the source table to the destination table.
 * @author Arvind Sasikumar
 */
public class TableMap {
    
    private final ArrayList<AttributeMap> attributeMapList;
    
    private final String sourceTable;
    private final String destinationTable;
    
    private final String sourceTimestampAttribute;
    private final String destinationTimestampAttribute;
    
    /**
     * Create a new table map using this constructor.
     * The source table name is mapped to the specified destination table name. 
     * The attributes of the table that are to be mapped are specified by using 
     * the {@link #addAttributeMap()} method. The constructor also requires 
     * to know which attributes in both the source and the destination tables 
     * serve as timestamp values that are used for the synchronization. The 
     * names of these attributes in the source and the destination tables are 
     * specified by the parameters sourceTimestampAttribute and 
     * destinationTimestampAttribute respectively.
     * @param sourceTable name of the table in the source table
     * @param destinationTable name of the table in the destination table
     * @param sourceTimestampAttribute name of the timestamp attribute in the 
     * source table to use used for synchronization
     * @param destinationTimeStampAttribute name of the timestamp attribute in 
     * the destination table to use used for synchronization
     * @see sync.db.mysql.AttributeMap
     */
    public TableMap(String sourceTable, String destinationTable, 
                    String sourceTimestampAttribute, 
                    String destinationTimeStampAttribute){
        
        this.sourceTable = sourceTable;
        this.destinationTable = destinationTable;
        
        this.sourceTimestampAttribute = sourceTimestampAttribute;
        this.destinationTimestampAttribute = destinationTimeStampAttribute;
        
        attributeMapList = new ArrayList<>();
    }
    
    /**
     * Adds a new attribute mapping between attribute names of corresponding 
     * attributes in the source and the destination tables.
     * @param attributeMap attribute map of corresponding attributes between the 
     * source and the destination tables
     */
    public void addAttributeMap(AttributeMap attributeMap){
        
        attributeMapList.add(attributeMap);
    }
    
    /**
     * Gets the source table name.
     * @return name of the source table
     */
    public String getSourceTable(){
        
        return sourceTable;
    }
    
    /**
     * Gets the destination table name.
     * @return name of the destination table
     */
    public String getDestinationTable(){
        
        return destinationTable;
    }
    
    /**
     * Gets the attribute name of the source table that serves as the timestamp 
     * attribute for synchronization.
     * @return attribute name of the source table that serves as the timestamp 
     * attribute for synchronization
     */
    public String getSourceTimestampAttribute(){
        
        return sourceTimestampAttribute;
    }
    
    /**
     * Gets the attribute name of the destination table that serves as the 
     * timestamp attribute for synchronization.
     * @return attribute name of the destination table that serves as the 
     * timestamp attribute for synchronization
     */
    public String getDestinationTimestampAttribute(){
        
        return destinationTimestampAttribute;
    }
    
    /**
     * Gets the entire attribute mapping between the source and the 
     * destination tables.
     * Attribute mapping of each attribute in the source and the destination 
     * tables is done inside an {@link sync.db.mysql.AttributeMap} 
     * object and put into the corresponding TableMap object one by one, 
     * building an {@link java.util.ArrayList} of AttributeMap objects in the 
     * process. This method returns this ArrayList.
     * @return the entire attribute mapping between the source and the 
     * destination tables
     * @see java.util.ArrayList
     * @see sync.db.mysql.AttributeMap
     */
    public ArrayList<AttributeMap> getAttributeMap(){
        
        return attributeMapList;
    }
}