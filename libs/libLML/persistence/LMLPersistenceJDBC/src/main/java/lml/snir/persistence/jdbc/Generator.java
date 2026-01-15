/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package lml.snir.persistence.jdbc;

import com.google.gson.Gson;

/**
 *
 * @author fanou
 */
class Generator {
    public static void main(String[] args) {
        PersistenceUnit pu = new PersistenceUnit();
        pu.setDataBase("dbName");
        pu.setDriver("mysql");
        pu.setHost("localhost");
        pu.setPasssword("secret");
        pu.setUser("fanou");
        
        
        Gson gson = new Gson();
        String str = gson.toJson(pu);
        System.out.println(str);
    }
}
