# Lambda

ITF20319 - Software Engineering og testing - Prosjekt (Gruppe12)

Dette repositoryet inneholder kildekoden for gruppeprosjektet "Lambda" i kurset ITF20319. README-en her gir en oversikt over prosjektet, hvordan du bygger og kjører det, og annen nyttig informasjon for utviklere og sensorer.



Innhold
- Om prosjektet
- Oppgave tekst
- Teknologistack
- Forutsetninger
- Komme i gang (kjøre, teste)
- Prosjektstruktur
- kontakt

Om prosjektet
----------------
Lambda er et Java-basert prosjekt (Maven) utviklet som en del av kurset for å lære programvareutvikling, testing og samarbeid i et team. Dette repositoryet inneholder kildekode, tester, og nødvendige konfigurasjoner.

Oppgave tekst - Kollektivtrafikk og ruteplanlegging
----------------
I dagen samfunn ser vi økende behov for mer effektiv og brukervennlig
kollektivtransport. Mange opplever utfordringer med å finne de raskeste
rutene, planlegge bytter mellom ulike transportmidler og få oppdatert
informasjon om forsinkelser. En oppstartsbedrift ønsker å lage en plattform
som gjør kollektivtransport mer tilgjengelig og forståelig for «hvermannsen» -
også de som ikke er vant til å bruke avanserte apper. 
Oppstartsbedriften, der dere er den interne IT-kompetansen, ønsker et
forslag til hvordan et slikt system kan bygges opp, hva det bør inneholde og
hvordan det kan gi verdi til sluttbrukere.

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
- Javalin
- (Evt. andre biblioteker finnes i pom.xml)

Forutsetninger
----------------
Før du bygger eller kjører prosjektet lokalt, sørg for at følgende er installert:
- Java JDK 11+ (sjekk med java -version)
- Git
- Du må være tilkoblet HIOF eduroam eller HIOF VPN for pga. databasen

Komme i gang
----------------
Klon repositoryet:

  git clone https://github.com/Gruppe12/Lambda.git
  cd Lambda

Kjør applikasjonen :
-----------------
(Du må være tilkoblet HIOF eduroam eller HIOF VPN for pga. databasen)
kjør Application klassen i src/main/java/no.lambda
click på url-en som kommer opp i console

Kjør tester:
--------------
kjøre testene 1 og 1 i src/tests/java

Prosjektstruktur
----------------
Dette prosjektet følger standard Maven-struktur:

- src/main/java/     -> Applikasjonskode
- src/main/resources/-> Ressurser (konfigurasjoner, properties, GraphQL spørringer)
- src/test/java/     -> Tester
- pom.xml            -> Maven-konfigurasjon og avhengigheter

Kodingstil
- Følg Java-kodekonvensjoner (navngivning, pakker, etc.).
- Skriv enhetstester for ny funksjonalitet.
- Sørg for at mvn test kjører grønt før du lager en PR.

Kontakt:
- kristoki@hiof.no
- tadasv@hiof.no
- danielpk@hiof.no
- khalily@hiof.no

- Repository: https://github.com/Gruppe12/Lambda
