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

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

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
    
    /**
     * Creates a database map from a valid dmml file using the dmml file's path.
     * Converts the file path to a file object and internally calls the 
     * getDBMapFromFile(File) method.
     * @param filepath full path of the dmml file as a string
     * @return database map
     * @throws FileNotFoundException if file is not found
     * @throws InvalidDBMapFileException if the dmml file is invalid
     */
    public static DBMap getDBMapFromFile (String filepath) throws FileNotFoundException, 
            InvalidDBMapFileException{
        
        File file = new File(filepath);
        return getDBMapFromFile(file);
    }
    
    /**
     * Creates a database map from a valid dmml file using a File object containing 
     * the dmml file.
     * @param file a file object containing the dmml file
     * @return database map
     * @throws FileNotFoundException if file not found
     * @throws InvalidDBMapFileException if the dmml file is invalid
     */
    public static DBMap getDBMapFromFile(File file) throws FileNotFoundException, 
            InvalidDBMapFileException{
        
        DBMap dbMap = new DBMap();
        Scanner scanner = new Scanner(file);
        String read;
        read = scanner.next();
        if(!read.equals("<databasemap>")){
            throw new InvalidDBMapFileException();
        }
        while(scanner.hasNext()){
            read = scanner.next();
            if(read.equals("</databasemap>")){
                break;
            }
            if(read.equals("<tablemap>")){
                String sourceTable, destinationTable, sourceTimestampAttribute, 
                        destinationTimestampAttribute;
                if(scanner.hasNext()){
                    read = scanner.next();
                    if(read.equals("<source>")){
                        if(scanner.hasNext()){
                            read = scanner.next();
                            sourceTable = read;
                        }
                        else{
                            throw new InvalidDBMapFileException();
                        }
                        if(scanner.hasNext()){
                            read = scanner.next();
                            if(!read.equals("</source>")){
                                throw new InvalidDBMapFileException();
                            }
                        }
                        else{
                            throw new InvalidDBMapFileException();
                        }
                    }
                    else{
                        throw new InvalidDBMapFileException();
                    }
                }
                else{
                    throw new InvalidDBMapFileException();
                }
                if(scanner.hasNext()){
                    read = scanner.next();
                    if(read.equals("<dest>")){
                        if(scanner.hasNext()){
                            read = scanner.next();
                            destinationTable = read;
                        }
                        else{
                            throw new InvalidDBMapFileException();
                        }
                        if(scanner.hasNext()){
                            read = scanner.next();
                            if(!read.equals("</dest>")){
                                throw new InvalidDBMapFileException();
                            }
                        }
                        else{
                            throw new InvalidDBMapFileException();
                        }
                    }
                    else{
                        throw new InvalidDBMapFileException();
                    }
                }
                else{
                    throw new InvalidDBMapFileException();
                }
                if(scanner.hasNext()){
                    read = scanner.next();
                    if(read.equals("<sourcetimestamp>")){
                        if(scanner.hasNext()){
                            read = scanner.next();
                            sourceTimestampAttribute = read;
                        }
                        else{
                            throw new InvalidDBMapFileException();
                        }
                        if(scanner.hasNext()){
                            read = scanner.next();
                            if(!read.equals("</sourcetimestamp>")){
                                throw new InvalidDBMapFileException();
                            }
                        }
                        else{
                            throw new InvalidDBMapFileException();
                        }
                    }
                    else{
                        throw new InvalidDBMapFileException();
                    }
                }
                else{
                    throw new InvalidDBMapFileException();
                }
                if(scanner.hasNext()){
                    read = scanner.next();
                    if(read.equals("<desttimestamp>")){
                        if(scanner.hasNext()){
                            read = scanner.next();
                            destinationTimestampAttribute = read;
                        }
                        else{
                            throw new InvalidDBMapFileException();
                        }
                        if(scanner.hasNext()){
                            read = scanner.next();
                            if(!read.equals("</desttimestamp>")){
                                throw new InvalidDBMapFileException();
                            }
                        }
                        else{
                            throw new InvalidDBMapFileException();
                        }
                    }
                    else{
                        throw new InvalidDBMapFileException();
                    }
                }
                else{
                    throw new InvalidDBMapFileException();
                }
                TableMap tableMap = new TableMap(sourceTable, destinationTable, 
                        sourceTimestampAttribute, destinationTimestampAttribute);
                while(scanner.hasNext()){
                    read = scanner.next();
                    if(read.equals("</tablemap>")){
                        dbMap.addTableMap(tableMap);
                        break;
                    }
                    if(read.equals("<attrmap>")){
                        String sourceAttribute, destinationAttribute;
                        AttributeType attributeType;
                        if(scanner.hasNext()){
                            read = scanner.next();
                            if(read.equals("<source>")){
                                if(scanner.hasNext()){
                                    read = scanner.next();
                                    sourceAttribute = read;
                                }
                                else{
                                    throw new InvalidDBMapFileException();
                                }
                                if(scanner.hasNext()){
                                    read = scanner.next();
                                    if(!read.equals("</source>")){
                                        throw new InvalidDBMapFileException();
                                    }
                                }
                                else{
                                    throw new InvalidDBMapFileException();
                                }
                            }
                            else{
                                throw new InvalidDBMapFileException();
                            }
                        }
                        else{
                            throw new InvalidDBMapFileException();
                        }
                        if(scanner.hasNext()){
                            read = scanner.next();
                            if(read.equals("<dest>")){
                                if(scanner.hasNext()){
                                    read = scanner.next();
                                    destinationAttribute = read;
                                }
                                else{
                                    throw new InvalidDBMapFileException();
                                }
                                if(scanner.hasNext()){
                                    read = scanner.next();
                                    if(!read.equals("</dest>")){
                                        throw new InvalidDBMapFileException();
                                    }
                                }
                                else{
                                    throw new InvalidDBMapFileException();
                                }
                            }
                            else{
                                throw new InvalidDBMapFileException();
                            }
                        }
                        else{
                            throw new InvalidDBMapFileException();
                        }
                        if(scanner.hasNext()){
                            read = scanner.next();
                            if(read.equals("<type>")){
                                if(scanner.hasNext()){
                                    read = scanner.next();
                                    if(read.equals("NUMERICAL")){
                                        attributeType = AttributeType.NUMERICAL;
                                    }
                                    else if(read.equals("STRING")){
                                        attributeType = AttributeType.STRING;
                                    }
                                    else{
                                        throw new InvalidDBMapFileException();
                                    }
                                }
                                else{
                                    throw new InvalidDBMapFileException();
                                }
                                if(scanner.hasNext()){
                                    read = scanner.next();
                                    if(!read.equals("</type>")){
                                        throw new InvalidDBMapFileException();
                                    }
                                }
                                else{
                                    throw new InvalidDBMapFileException();
                                }
                            }
                            else{
                                throw new InvalidDBMapFileException();
                            }
                        }
                        else{
                            throw new InvalidDBMapFileException();
                        }
                        tableMap.addAttributeMap(new AttributeMap(sourceAttribute, 
                                destinationAttribute, attributeType));
                    }
                    else{
                        throw new InvalidDBMapFileException();
                    }
                    if(scanner.hasNext()){
                        read = scanner.next();
                        if(!read.equals("</attrmap>")){
                            throw new InvalidDBMapFileException();
                        }
                    }
                    else{
                        throw new InvalidDBMapFileException();
                    }
                }
            }
            else{
                throw new InvalidDBMapFileException();
            }
        }
        return dbMap;
    }
}
