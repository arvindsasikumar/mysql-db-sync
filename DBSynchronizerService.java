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
import java.io.FileNotFoundException;
import sync.db.mysql.*;
import java.util.Scanner;

/**
 * Console version.
 * @author Arvind Sasikumar
 */
public class DBSynchronizerService {
    
    public static void main(String[] args){
        
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Enter server database address: ");
        String serverDatabaseAddress = scanner.next();
        System.out.println("Enter server database name: ");
        String serverDatabaseName = scanner.next();
        System.out.println("Enter server database username: ");
        String serverDatabaseUsername = scanner.next();
        System.out.println("Enter server database password: ");
        String serverDatabasePassword = scanner.next();
        System.out.println("Enter server database port: ");
        int serverDatabasePort = scanner.nextInt();
        System.out.println("Enter server database connection options: ");
        String serverDatabaseConnectionOptions = scanner.next();
        
        System.out.println("Enter client database address: ");
        String clientDatabaseAddress = scanner.next();
        System.out.println("Enter client database name: ");
        String clientDatabaseName = scanner.next();
        System.out.println("Enter client database username: ");
        String clientDatabaseUsername = scanner.next();
        System.out.println("Enter client database password: ");
        String clientDatabasePassword = scanner.next();
        System.out.println("Enter client database port: ");
        int clientDatabasePort = scanner.nextInt();
        System.out.println("Enter client database connection options: ");
        String clientDatabaseConnectionOptions = scanner.next();
        scanner.nextLine();
        System.out.println("Enter full path to the dmml file to create the database map: ");
        String dmmlFilePath = scanner.nextLine();
        
        DBMap dbMap = new DBMap();
        try{
            
            dbMap = DBMap.getDBMapFromFile(dmmlFilePath);
        }
        catch(FileNotFoundException e){
            
            System.out.println("DMML File not found!");
            System.exit(0);
        }
        catch(InvalidDBMapFileException e){
            
            System.out.println("Invalid DBMap File!");
            System.exit(0);
        }
        catch(Exception e){
            
            e.printStackTrace();
            System.exit(0);
        }
        
        DBSyncAgent dbSyncAgent = new DBSyncAgent.Builder()
            .setServerDatabaseAddress(serverDatabaseAddress)
            .setServerDatabaseName(serverDatabaseName)
            .setServerDatabaseUsername(serverDatabaseUsername)
            .setServerDatabasePassword(serverDatabasePassword)
            .setServerDatabasePort(serverDatabasePort)
            .setServerDatabaseConnectionOptions(serverDatabaseConnectionOptions)
            .setClientDatabaseAddress(clientDatabaseAddress)
            .setClientDatabaseName(clientDatabaseName)
            .setClientDatabaseUsername(clientDatabaseUsername)
            .setClientDatabasePassword(clientDatabasePassword)
            .setClientDatabasePort(clientDatabasePort)
            .setClientDatabaseConnectionOptions(clientDatabaseConnectionOptions)
            .setDBMap(dbMap)
            .build();
        
        dbSyncAgent.setSyncInterval(30);
        
        dbSyncAgent.connect();
        dbSyncAgent.sync();
        dbSyncAgent.hold();
        dbSyncAgent.liveSync();
        
        String s;
        s = scanner.next();
        
        dbSyncAgent.stopSync();
        dbSyncAgent.disconnect();
    }
}
