# Lambda

ITF20319 - Software Engineering og testing - Prosjekt (Gruppe12)

Dette repositoryet inneholder kildekoden for gruppeprosjektet "Lambda" i kurset ITF20319. README-en her gir en oversikt over prosjektet, hvordan du bygger og kjører det, og annen nyttig informasjon for utviklere og sensorer.

Innhold
- Om prosjektet
- Teknologistack
- Forutsetninger
- Komme i gang (bygge, kjøre, teste)
- Prosjektstruktur
- Bidra
- Kodekvalitet og testing
- Vanlige problemer
- Lisens og kontakt

Om prosjektet
----------------
Lambda er et Java-basert prosjekt (Maven) utviklet som en del av kurset for å lære programvareutvikling, testing og samarbeid i et team. Dette repositoryet inneholder kildekode, tester, og nødvendige konfigurasjoner.

Teknologistack
----------------
- Java (versjon anbefales: 11 eller nyere)
- JavaScript
- HTML
- CSS
- GraphQL
- Maven (byggeverktøy)
- JUnit (enhetstesting)
- OKhttp3
- (Evt. andre biblioteker finnes i pom.xml)

Forutsetninger
----------------
Før du bygger eller kjører prosjektet lokalt, sørg for at følgende er installert:
- Java JDK 11+ (sjekk med java -version)
- Maven (sjekk med mvn -v)
- Git

Komme i gang
----------------
Klon repositoryet:

  git clone https://github.com/Gruppe12/Lambda.git
  cd Lambda

Bygge prosjektet:

  mvn clean package

Kjør tester:

  mvn test

Kjør applikasjonen (hvis prosjektet produserer en kjørbar JAR):

  java -jar target/<artifact-name>.jar

Merk: Erstatt <artifact-name>.jar med det faktiske artefaktet som genereres av Maven. Alternativt, hvis prosjektet er et Spring Boot-prosjekt, kan du bruke:

  mvn spring-boot:run

Prosjektstruktur
----------------
Dette prosjektet følger standard Maven-struktur:

- src/main/java/     -> Applikasjonskode
- src/main/resources/-> Ressurser (konfigurasjoner, properties, GraphQL spørringer)
- src/test/java/     -> Tester
- pom.xml            -> Maven-konfigurasjon
- .mvn/              -> Maven wrapper (inkludert slik at bygg fungerer uten global mvn-installasjon)

Hvis du trenger mer detaljert arkitekturoversikt (klasser, pakker, ansvar), legg inn en issue eller be om en arkitektur-README med diagrammer.

Bidra
----------------
Vi oppfordrer til samarbeid:
- Lag en issue for nye funksjoner eller bugs.
- Opprett en feature-branch: git checkout -b feature/short-description
- Lag en PR med beskrivende tittel og referanse til relevante issues.

Kodingstil
- Følg Java-kodekonvensjoner (navngivning, pakker, etc.).
- Skriv enhetstester for ny funksjonalitet.
- Sørg for at mvn test kjører grønt før du lager en PR.

Kodekvalitet og testing
----------------
- Enhetstester kjøres med mvn test.
- For CI: Vi anbefaler å sette opp GitHub Actions for automatisk bygg og test.

Vanlige problemer
----------------
- Feil versjon av Java: Sjekk java -version og oppdater om nødvendig.
- Maven cache-problemer: Prøv mvn -U clean package for å oppdatere avhengigheter.


Kontakt:
- Gruppe12
- Repository: https://github.com/Gruppe12/Lambda
