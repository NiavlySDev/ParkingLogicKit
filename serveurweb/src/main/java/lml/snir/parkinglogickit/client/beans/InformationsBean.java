package lml.snir.parkinglogickit.client.beans;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;
import java.io.Serializable;
import java.io.StringReader;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Informations générales affichées dans la page Informations.
 */
@Named
@ApplicationScoped
public class InformationsBean implements Serializable {

    private static final String[] BRANCHES = {"sylvain", "phily", "virgile", "ethan"};
    private static final int LIMITE_CHANGELOG = 12;
    private static final String COMMITS_URL
            = "https://api.github.com/repos/NiavlySDev/ParkingLogicKit/commits?sha=%s&per_page=100";
    private static final Duration CACHE_DURATION = Duration.ofMinutes(10);
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter
            .ofPattern("d MMMM yyyy 'à' HH:mm", Locale.FRANCE)
            .withZone(ZoneId.of("Europe/Paris"));

    private final HttpClient httpClient = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(8))
            .build();

    private Instant cacheDate;
    private Instant derniereMiseAJour;
    private List<CommitInfo> dernieresMisesAJour = new ArrayList<>();

    public synchronized String getDerniereMiseAJourProjet() {
        rafraichirSiNecessaire();
        if (derniereMiseAJour == null) {
            return "Indisponible";
        }
        return DATE_FORMATTER.format(derniereMiseAJour);
    }

    public synchronized List<CommitInfo> getDernieresMisesAJour() {
        rafraichirSiNecessaire();
        return dernieresMisesAJour;
    }

    private void rafraichirSiNecessaire() {
        if (cacheDate != null && Instant.now().minus(CACHE_DURATION).isBefore(cacheDate)) {
            return;
        }

        Map<String, CommitInfo> commitsParSha = new LinkedHashMap<>();
        for (String branche : BRANCHES) {
            try {
                for (CommitInfo commit : trouverCommitsNonMerge(branche)) {
                    commitsParSha.putIfAbsent(commit.getSha(), commit);
                }
            } catch (Exception e) {
                System.err.println("InformationsBean GitHub branch " + branche
                        + " refresh error: " + e.getMessage());
            }
        }

        List<CommitInfo> commits = new ArrayList<>(commitsParSha.values());
        commits.sort(Comparator.comparing(CommitInfo::getDate).reversed());
        if (commits.size() > LIMITE_CHANGELOG) {
            commits = new ArrayList<>(commits.subList(0, LIMITE_CHANGELOG));
        }

        dernieresMisesAJour = commits;
        derniereMiseAJour = commits.isEmpty() ? null : commits.get(0).getDate();
        cacheDate = Instant.now();
    }

    private List<CommitInfo> trouverCommitsNonMerge(String branche) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(String.format(COMMITS_URL, branche)))
                .timeout(Duration.ofSeconds(10))
                .header("Accept", "application/vnd.github+json")
                .header("User-Agent", "ParkingLogicKit-ServeurWeb")
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() < 200 || response.statusCode() >= 300) {
            throw new IllegalStateException("GitHub API HTTP " + response.statusCode());
        }

        List<CommitInfo> result = new ArrayList<>();
        try (JsonReader reader = Json.createReader(new StringReader(response.body()))) {
            JsonArray commits = reader.readArray();
            for (int i = 0; i < commits.size(); i++) {
                JsonObject commit = commits.getJsonObject(i);
                if (commit.getJsonArray("parents").size() > 1) {
                    continue;
                }
                result.add(creerCommitInfo(commit));
            }
        }
        return result;
    }

    private CommitInfo creerCommitInfo(JsonObject commit) {
        JsonObject commitData = commit.getJsonObject("commit");
        String sha = commit.getString("sha");
        String auteur = commitData.getJsonObject("author").getString("name", "Inconnu");
        Instant date = Instant.parse(commitData.getJsonObject("committer").getString("date"));
        String message = commitData.getString("message", "Mise à jour");
        String titre = message.split("\\R", 2)[0];
        return new CommitInfo(sha, auteur, titre, date, DATE_FORMATTER.format(date));
    }

    public static class CommitInfo implements Serializable {

        private final String sha;
        private final String auteur;
        private final String titre;
        private final Instant date;
        private final String dateFormatee;

        public CommitInfo(String sha, String auteur, String titre, Instant date, String dateFormatee) {
            this.sha = sha;
            this.auteur = auteur;
            this.titre = titre;
            this.date = date;
            this.dateFormatee = dateFormatee;
        }

        public String getSha() {
            return sha;
        }

        public String getAuteur() {
            return auteur;
        }

        public String getTitre() {
            return titre;
        }

        public Instant getDate() {
            return date;
        }

        public String getDateFormatee() {
            return dateFormatee;
        }
    }
}
