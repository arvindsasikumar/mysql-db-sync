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
 * An object of this class maps the source database to the destination database.
 * @author Arvind Sasikumar
 */
public class DBMap {
    
    private final ArrayList<TableMap> tableMapList;
    
    /**
     * Create a new database map using this constructor.
     * DBMap contains an {@link java.util.ArrayList} of 
     * {@link sync.db.mysql.TableMap} objects, each of which maps one table 
     * from the source database to another table in the destination database. 
     * This ArrayList of TableMap is initialized in this constructor. TableMaps 
     * are added to this ArrayList using 
     * @link #addTableMap(sync.db.mysql.TableMap). 
     */
    public DBMap(){
        
        tableMapList = new ArrayList<>();
    }
    
    /**
     * Adds a mapping between one table of the source database to one 
     * table of the destination database.
     * This mapping is specified in a {@link sync.db.mysql.TableMap} object for 
     * each individual table mapping.
     * @param tableMap a TableMap object mapping a table of the source database 
     * to a table of the destination database.
     */
    public void addTableMap(TableMap tableMap){
        
        tableMapList.add(tableMap);
    }
    
    /**
     * Gets a set of all table mapping between the source and the destination 
     * databases in an {@link java.util.ArrayList} form, of type 
     * {@link sync.db.mysql.TableMap}.
     * @return set of all table mapping between the source and the destination 
     * databases
     */
    public ArrayList<TableMap> getTableMap(){
        
        return tableMapList;
    }
}
