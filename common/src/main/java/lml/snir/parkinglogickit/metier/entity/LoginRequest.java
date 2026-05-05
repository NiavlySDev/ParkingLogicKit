package lml.snir.parkinglogickit.metier.entity;
/**
 * Classe représentant le corps de la requête de connexion.
 * Contient les identifiants saisis par l'utilisateur (username et password)
 * qui seront envoyés au serveur pour vérification.
 * Le mot de passe ne transite que dans ce sens (client → serveur)
 * et ne sera jamais retourné dans une réponse.
 * @author Ethan
 */

public class LoginRequest {
    private String username;
    private String password;

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
}