package lml.snir.parkinglogickit.metier.rest.serveur;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import lml.snir.parkinglogickit.metierfactory.MetierFactory;
import lml.snir.parkinglogickit.metier.entity.Driver;
import lml.snir.parkinglogickit.metier.entity.LoginRequest;
import lml.snir.parkinglogickit.metier.transactionel.DriverService;
import lml.snir.rest.server.RestException;

/**
 * Endpoint REST gérant l'authentification des utilisateurs.
 * Expose un POST sur /AuthService/login qui reçoit un LoginRequest,
 * vérifie les identifiants côté serveur via le DriverService,
 * et retourne uniquement le username et le rôle (Admin ou Driver)
 * sans jamais exposer le mot de passe dans la réponse.
 * @author Ethan
 */

@Path("/AuthService")
@Consumes("application/json")
@Produces("application/json")
public class AuthRestServeurImpl {

    private final DriverService DriverSrv;

    public AuthRestServeurImpl() throws Exception {
        this.DriverSrv = MetierFactory.getDriverService();
    }

    @POST
    @Path("/login")
    public LoginResponse login(LoginRequest request) throws Exception {
        try {
            Driver driver = this.DriverSrv.getByUsername(request.getUsername());

            if (driver == null || !driver.getPassword().equals(request.getPassword())) {
                throw new RestException(401, "Identifiants incorrects");
            }

            String role = driver.getClass().getSimpleName().equals("Admin") ? "Admin" : "Driver";

            return new LoginResponse(driver.getUsername(), role);

        } catch (RestException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RestException(500, ex.getMessage());
        }
    }

    // Classe interne pour la réponse — pas besoin d'un fichier séparé
    public static class LoginResponse {
        public String username;
        public String role;

        public LoginResponse(String username, String role) {
            this.username = username;
            this.role = role;
        }
    }
}