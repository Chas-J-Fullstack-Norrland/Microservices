# Introduktion
Systemet är uppbyggt enligt en mikrotjänstarkitektur där varje tjänst ansvarar för ett avgränsat verksamhetsområde. Tjänsterna körs som separata applikationer och kommunicerar över HTTP via ett API Gateway.

API Gateway fungerar som systemets enda ingångspunkt och ansvarar för att dirigera inkommande förfrågningar till rätt mikrotjänst. Gatewayn kan även hantera gemensamma funktioner såsom autentisering, auktorisering och routning.

För att möjliggöra dynamisk upptäckt av tjänster används en Service Registry. När en mikrotjänst startar registrerar den sig automatiskt i registret, vilket gör att andra komponenter kan hitta tjänsten utan att känna till dess fysiska adress.

Systemets konfiguration hanteras centralt genom en Config Server. Vid uppstart hämtar varje mikrotjänst sin konfiguration från Config Server, vilket förenklar administration och gör det möjligt att ändra konfiguration utan att behöva duplicera inställningar mellan tjänster.

Varje mikrotjänst har ett eget ansvar för affärslogik och datalagring. Detta innebär att tjänsterna är löst kopplade och kan utvecklas, testas och distribueras oberoende av varandra.

                        +----------------------+
                        |      Klient          |
                        | (Webb / Swagger UI)  |
                        +----------+-----------+
                                   |
                                   |
                          HTTP Requests
                                   |
                                   v
                     +-------------------------+
                     |      API Gateway        |
                     |  Routing & Säkerhet     |
                     +-----------+-------------+
                                 |
               +-----------------+-----------------+
               |                                   |
               |                                   |
               v                                   v
     +---------------------+            +---------------------+
     |    Microservice A   |            |    Microservice B   |
     |  Affärslogik/API    |            |  Affärslogik/API    |
     +----------+----------+            +----------+----------+
                |                                    |
                |                                    |
                v                                    v
        +---------------+                    +---------------+
        |   Databas A   |                    |   Databas B   |
        +---------------+                    +---------------+

                 ^                                   ^
                 |                                   |
                 +---------------+-------------------+
                                 |
                         +---------------+
                         | Service       |
                         | Registry      |
                         +---------------+

                                 ^
                                 |
                         +---------------+
                         | Config Server |
                         +---------------+

# API Gateway

Gatewayn fungerar som den centrala ingångspunkten till systemet. Den ansvarar för att:

dirigera inkommande HTTP-förfrågningar till rätt mikrotjänst,
hantera autentisering och auktorisering,
exponera gemensamma API:er,
minska kopplingen mellan klient och interna tjänster.

Klienten kommunicerar endast med gatewayn och har ingen direkt kontakt med de bakomliggande mikrotjänsterna.

## Service Registry

Service Registry används för tjänsteupptäckt (Service Discovery). När en mikrotjänst startar registrerar den sig automatiskt. API Gateway och övriga tjänster kan därefter hitta varandra utan att IP-adresser eller portar behöver konfigureras manuellt.

# Config Server

Config Server tillhandahåller centraliserad konfiguration för samtliga mikrotjänster. Vid uppstart hämtar respektive tjänst sina inställningar från Config Server, vilket gör konfigurationen enhetlig och enklare att underhålla.

# Mikrotjänster

Varje mikrotjänst ansvarar för ett specifikt verksamhetsområde och innehåller:

egen affärslogik,
egna REST-endpoints,
egen databas,
egen konfiguration.

Denna uppdelning gör att tjänster kan utvecklas och distribueras oberoende av varandra.

# Säkerhet

Systemet använder en central säkerhetslösning där API Gateway fungerar som första kontrollpunkt för inkommande trafik.

Publika resurser, exempelvis API-dokumentation och Swagger UI, kan exponeras utan autentisering medan övriga API-endpoints kan skyddas med JWT-baserad autentisering.

Genom att centralisera säkerheten i gatewayn minskar behovet av duplicerad säkerhetskonfiguration i varje enskild mikrotjänst.

Kommunikation mellan tjänster

Kommunikationen sker via HTTP och REST.

Flödet ser ut enligt följande:

Klienten skickar en HTTP-förfrågan till API Gateway.
Gatewayn identifierar vilken mikrotjänst som ska hantera förfrågan.
Förfrågan vidarebefordras till aktuell mikrotjänst.
Mikrotjänsten behandlar begäran och kommunicerar vid behov med sin databas.
Svaret skickas tillbaka via API Gateway till klienten.
Starta systemet lokalt

Följande steg används för att starta systemet i en lokal utvecklingsmiljö.

# 1. Klona projektet

Klona projektets repository till den lokala datorn.

git clone <repository-url>
cd <projekt>

# 2. Kontrollera förutsättningar

Följande programvara bör vara installerad:

Java
Gradle
Docker
Docker Compose

# 3. Starta infrastrukturen

OBS: /gradlew clean build kan krävas vid första uppstart

Starta samtliga containrar.

docker compose up --build

Vid första uppstart kan nedladdning av beroenden ta några minuter.

# 4. Kontrollera att tjänsterna har startat

Verifiera att samtliga containrar körs.

docker ps

Kontrollera även att samtliga mikrotjänster registrerat sig i Service Registry.

# 5. Öppna API Gateway

När systemet är färdigstartat används API Gateway som ingångspunkt för samtliga anrop.

Swagger/OpenAPI kan därefter användas för att testa de exponerade API:erna.

# Stänga systemet

Samtliga tjänster stoppas med:

docker compose down

Om databaser och lagrade volymer även ska tas bort används:

docker compose down -v
