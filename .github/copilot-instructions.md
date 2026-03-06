# ParkingLogicKit — AI assistant guidance

The repository is a multi‑module Maven project for a parking management system. An AI agent should be aware of the high‑level architecture and the recurring patterns used by the human developers before attempting to add or refactor code.

## 🏗️ Architecture overview

1. **Top level aggregator** (`pom.xml` in root) defines modules:
   - `common` – shared domain model: JPA entities and service interfaces.
   - `metier` – business layer, transaction implementations plus REST client/​server subpackages.
   - `physique` – persistence & hardware; `DataService` interfaces with JPA implementations.
   - `serveurweb` – Jakarta‑EE/JSF web UI (PrimeFaces) and JAX‑RS registration.

   A separate `libs/` tree contains reusable libraries (JavaFX helpers, REST client/server, drivers) used by this and other projects; you rarely need to edit it unless you are adding global utilities.

2. **Service + data factories** (`MetierFactory`, `PhysiqueDataFactory`) decide at runtime whether to use a local implementation or a remote REST client. The choice is controlled by `rest.properties`.

3. **REST layer**: endpoints live in `metier/rest/serveur`. `serveurweb/RestLaunch` registers them with path `@ApplicationPath("rest")`; the base URL when deployed is `http://<host>:8080/ParkingLogicKitServeur/rest/…`. Only `DriverService` is enabled by default; the other registrations are commented out.

4. **Angular front‑end**: under `clientlourd/parkinglogickit`. This code lives outside Maven and is run with the Angular CLI; it consumes the REST API exposed by the back end.

5. **Persistence**: JPA/EclipseLink configuration is in `*/META-INF/persistence.xml` (see `physique` for credentials). Tests run with `schema-generation.database.action=create`, so a local MySQL instance must be available.


## 🔧 Key developer workflows

- **Build everything:**
  ```powershell
  cd <repo root>
  ./mvnw clean install
  ```
  (use `mvnw.cmd` on Windows if you prefer).  You can target a single module with `-pl metier` etc.

- **Run unit tests:** `./mvnw test` (Junit‑4). Individual modules contain their own tests in `src/test/java`.

- **Package web application:**
  ```bash
  cd serveurweb
  ./mvnw clean package
  # war appears in target/*.war
  ```
  Deploy the WAR to a Jakarta‑EE 10 container such as WildFly; the project was originally built for WildFly (see comments in `RestLaunch`).

- **Switch between local and remote business logic:** edit `rest.properties` in the working directory and set `local=true|false`.
  - `true`: factories return `*ServiceImpl` classes defined in `metier/transactionel` and `physique/*JPAImpl`.
  - `false`: factories return classes under `metier/rest/client` which call a running REST server.

- **Angular front‑end:**
  ```bash
  cd clientlourd/parkinglogickit
  npm install          # first time only
  ng serve             # start dev server at http://localhost:4200
  ng build             # production bundle in dist/
  ng test              # runs Vitest unit tests
  ng e2e               # run end‑to‑end tests (none configured by default)
  ```
  The front‑end expects the back‑end API to be available on `localhost:8080` unless you change the environment configuration.

- **Database**: default persistence unit connects to `jdbc:mysql://localhost:3306/parkinglogickit`. Credentials are hard‑coded in `physique/src/main/resources/META-INF/persistence.xml`. Adjust this file or the URL when setting up a new environment.

- **Scripts**: the `scripts/` directory contains PowerShell and shell helpers used by the team for pushing/updating remote repos; they are not required for normal development.


## ✅ Project conventions & patterns

- **Naming**: packages use `lml.snir.parkinglogickit.*`. Class names are English; comments are often in French.

- **Services**: define an interface (in `common` or `metier/transactionel`), then provide
  - a local impl (`XServiceImpl`),
  - a REST client impl (`XServiceClientRESTImpl`),
  - optionally a REST server impl (`XServiceRestServeurImpl`) annotated with JAX‑RS `@Path`.
  Factories statically cache singletons and read `rest.properties`.  When adding a new operation, update all three layers and the factory accordingly.

- **Data layer**: interfaces like `AccessDataService` with `AccessDataServiceJPAImpl`. Always obtain instances via `PhysiqueDataFactory.get...Service()`; this makes it easy to plug in mocks or alternate storage later.

- **Entities**: defined with Jakarta‑persistence annotations. `equals`/`hashCode` are generated and usually compare both the `id` field and one or two unique properties; there are `// TODO` comments warning about uninitialised ids.  Keep the style consistent with existing classes.

- **REST registration**: edit `serveurweb/RestLaunch#getClasses()` to add a class to the set; uncomment as needed.  The WADL servlet and JSON mapping are enabled there.

- **Factory configuration**: `ConfigReader` (from `lml.snir.tools`) is used to read `rest.properties`; it expects a file path relative to the working directory.

- **Angular conventions**: standard CLI structure. When generating new components or services use `ng generate`. The front‑end code calls the REST API; look at existing service files in `src/app` for examples.


## 🧠 Integration points & external dependencies

- **Database**: MySQL running on localhost; schema recreation is triggered by JPA on each run (see persistence.xml).
- **REST services**: backend uses Jakarta‑EE 10 / Resteasy; front‑end and remote clients consume these endpoints.
- **MQTT**: MetierFactory contains commented‑out MQTT consumer code; Paho client dependency is included.
- **Angular CLI / Node.js**: keep Node >= the version used by Angular CLI 21.0.2.
- **WildFly / Jakarta container**: deploy the WAR produced by `serveurweb`.


## 📍 Where to look for examples

- `metier/transactionel/*.java` – business logic implementations.
- `metier/rest/client/*ClientRESTImpl.java` – how clients serialize/deserialize DTOs with Gson.
- `physique/data/*JPAImpl.java` – typical JPA queries using `EntityManager`.
- `serveurweb/client/beans/*` – JSF backing beans for the web UI.
- `common/src/main/java/lml/snir/parkinglogickit/metier/entity` – all domain objects.


> **Note for AI agents:** the codebase is old‑school Java (no Spring), relies heavily on manual factories and static singletons, and mixes French/English. Avoid making sweeping architectural changes unless the user explicitly asks; focus on extending existing patterns.  Always refer to `rest.properties` and `persistence.xml` when touching configuration.

---

Please review these instructions and let me know if any area feels incomplete or unclear; I can iterate further.