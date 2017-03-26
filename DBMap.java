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
public class DBMap {
    
    private final ArrayList<TableMap> tableMapList;
    
    public DBMap(){
        
        tableMapList = new ArrayList<>();
    }
    
    public void addTableMap(TableMap tableMap){
        
        tableMapList.add(tableMap);
    }
    
    public ArrayList<TableMap> getTableMap(){
        
        return tableMapList;
    }
}
