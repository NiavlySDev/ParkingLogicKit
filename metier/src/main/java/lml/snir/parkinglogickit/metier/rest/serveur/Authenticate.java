package lml.snir.parkinglogickit.metier.rest.serveur;

import jakarta.ws.rs.core.MultivaluedMap;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Viralu
 */

public class Authenticate {

    static void authenticate(MultivaluedMap<String, String> tokens) throws Exception {
        boolean ok;
        try {
            String login = tokens.getFirst("login");
            String pass = tokens.getFirst("pass");

            ok = login.equals("PLK") & pass.equals("PASSPLK");
        } catch (Exception ex) {
            ok = false;
        }

        if (!ok) {
            throw new Exception("Not authenticated");
        }

    }
}