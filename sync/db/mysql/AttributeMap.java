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

/**
 * An object of this class maps an attribute of the source table to an attribute
 * of the destination table.
 * @author Arvind Sasikumar
 */
public class AttributeMap {
    
    private final String sourceAttribute;
    private final String destinationAttribute;
    
    private final AttributeType type;
    
    /**
     * Create a new attribute map using this constructor.
     * The sourceAttribute and destinationAttribute parameters refer to the 
     * source and destination attribute names respectively that are to be mapped 
     * as found in their respective tables. The type parameter indicates whether 
     * the datatype of the attributes to be mapped is of form 
     * {@link sync.db.mysql.AttributeType#STRING} or 
     * {@link sync.db.mysql.AttributeType#NUMERICAL}.
     * @param sourceAttribute attribute name in the source table
     * @param destinationAttribute corresponding attribute name in the 
     * destination table
     * @param type specifies whether the attribute is of type 
     * {@link sync.db.mysql.AttributeType#STRING} or 
     * {@link sync.db.mysql.AttributeType#NUMERICAL}, 
     * as defined in the enum {@link sync.db.mysql.AttributeType}.
     * @see sync.db.mysql.AttributeType
     * @see sync.db.mysql.AttributeType#STRING
     * @see sync.db.mysql.AttributeType#NUMERICAL
     */
    public AttributeMap(String sourceAttribute, String destinationAttribute, 
                        AttributeType type){
        
        this.sourceAttribute = sourceAttribute;
        this.destinationAttribute = destinationAttribute;
        this.type = type;
    }
    
    /**
     * Gets the attribute name of the source table in the current attribute 
     * mapping.
     * @return attribute name of the source table in the current attribute 
     * mapping
     */
    public String getSourceAttribute(){
        
        return sourceAttribute;
    }
    
    /**
     * Gets the attribute name of the destination table in the current attribute 
     * mapping.
     * @return attribute name of the destination table in the current attribute 
     * mapping
     */
    public String getDestinationAttribute(){
        
        return destinationAttribute;
    }
    
    /**
     * Gets the attribute type of the current attribute mapping.
     * @return type of the current attribute mapping, as defined by the enum 
     * {@link sync.db.mysql.AttributeType}.
     * @see sync.db.mysql.AttributeType
     * @see sync.db.mysql.AttributeType#STRING
     * @see sync.db.mysql.AttributeType#NUMERICAL
     */
    public AttributeType getType(){
        
        return type;
    }
}
