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
public class AttributeMap {
    
    private final String sourceAttribute;
    private final String destinationAttribute;
    
    private final AttributeType type;
    
    public AttributeMap(String sourceAttribute, String destinationAttribute, AttributeType type){
        
        this.sourceAttribute = sourceAttribute;
        this.destinationAttribute = destinationAttribute;
        this.type = type;
    }
    
    public String getSourceAttribute(){
        
        return sourceAttribute;
    }
    
    public String getDestinationAttribute(){
        
        return destinationAttribute;
    }
    
    public AttributeType getType(){
        
        return type;
    }
}
