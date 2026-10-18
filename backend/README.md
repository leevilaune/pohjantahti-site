# Pohjantähti API

Spring Boot backend for the Pohjantähti clan website. It pulls the three clans from the official Clash of Clans API on a schedule, stores them in a database (Spring Data JPA) and serves the cached data to the React frontend. The API token stays on the server and site traffic never hits the official API's rate limits.

- Java 17+, Spring Boot 3.5, Spring Web, Spring Data JPA
- H2 file database by default (`./data/`), easy to swap for PostgreSQL/MySQL
- Tracks the clans `#2GY2L0UQP`, `#2RJ0VGRCL` and `#2JJGRUPQL` (see `application.yml`)

## Run it

1. Create an API key at <https://developer.clashofclans.com>. **A key only works from the IP addresses you list when creating it**, so make one for your development machine and another for the Raspberry Pi's public IP.
2. Start the app:

   ```bash
   export COC_API_TOKEN=your-token-here
   mvn spring-boot:run
   ```

   The server listens on <http://localhost:8080>. The first refresh runs 5 seconds after startup and then every 10 minutes (`coc.refresh-interval`).

3. Run the tests: `mvn test`

Starting from an IDE (IntelliJ, VS Code) or with `sudo` does not pass your shell's `export`s on. Either set the variable in the run configuration, or create a git-ignored `backend/.env` file next to `pom.xml`:

```
COC_API_TOKEN=your-token-here
ADMIN_KEY=choose-a-secret
```

The app reads it from the directory it is started in.

The app starts without a token too; it just logs a warning and serves an empty list until data has been fetched.

## Endpoints

| Method | Path                         | Description                                              |
| ------ | ---------------------------- | -------------------------------------------------------- |
| GET    | `/api/overview`              | totals across all clans plus a summary of each           |
| GET    | `/api/clans`                 | list of tracked clans                                    |
| GET    | `/api/clans/{tag}`           | one clan with its member list                            |
| GET    | `/api/clans/{tag}/members`   | members only, ordered by clan rank                       |
| POST   | `/api/admin/refresh`         | refresh now; needs header `X-Admin-Key` (see below)      |

Tags can be written with or without `#`: `/api/clans/2GY2L0UQP`. Only tracked clans are served, so the backend cannot be used to look up arbitrary clans. Member `role` is the raw API value: `leader`, `coLeader`, `admin`, `member`.

Errors use RFC 9457 problem details: 400 for malformed tags, 404 for clans that are not tracked or not fetched yet, 502 if the Clash of Clans API failed (e.g. 403 means the token is wrong or the IP is not allowed for the key).

Manual refresh:

```bash
curl -X POST -H "X-Admin-Key: $ADMIN_KEY" http://localhost:8080/api/admin/refresh
```

The endpoint is disabled unless `ADMIN_KEY` is set.

## Configuration

Set in `src/main/resources/application.yml` or via environment variables.

| Setting                    | Env var         | Default                         |
| -------------------------- | --------------- | ------------------------------- |
| `coc.token`                | `COC_API_TOKEN` | empty (refresh disabled)        |
| `coc.clan-tags`            |                 | the three Pohjantähti clans     |
| `coc.refresh-interval`     |                 | `PT10M` (ISO-8601 duration)     |
| `app.admin-key`            | `ADMIN_KEY`     | empty (admin endpoint disabled) |
| `app.cors.allowed-origins` |                 | localhost:5173 and pt.onesnzeroes.dev |
| `spring.datasource.*`      |                 | H2 file `./data/pohjantahti`    |

Spring Boot also accepts any property as an env var, e.g. `SERVER_PORT=9090`.

## Project layout

```
src/main/java/dev/onesnzeroes/pt/
  client/    ClashOfClansClient (RestClient), CocApiException
  config/    CocProperties, HttpClientConfig, WebConfig (CORS)
  domain/    JPA entities (ClanEntity, MemberEntity), CoC response records, TagUtil
  repo/      Spring Data repositories
  service/   ClanService (store + read), ClanSyncService (scheduled refresh), DTOs
  web/       REST controllers and error handling
```

## Deploy on the Raspberry Pi

1. Install Java and build:

   ```bash
   sudo apt install -y openjdk-17-jdk maven
   mvn -DskipTests package
   sudo mkdir -p /opt/pohjantahti-api
   sudo cp target/pohjantahti-api-0.1.0.jar /opt/pohjantahti-api/app.jar
   ```

   (Or build on your computer and copy the jar over with `scp`.)

2. Put secrets in `/etc/pohjantahti-api.env` (owner root, mode 600):

   ```
   COC_API_TOKEN=...
   ADMIN_KEY=...
   ```

3. `/etc/systemd/system/pohjantahti-api.service`:

   ```ini
   [Unit]
   Description=Pohjantahti API
   After=network-online.target

   [Service]
   User=www-data
   WorkingDirectory=/opt/pohjantahti-api
   EnvironmentFile=/etc/pohjantahti-api.env
   ExecStart=/usr/bin/java -Xmx256m -jar /opt/pohjantahti-api/app.jar
   Restart=on-failure

   [Install]
   WantedBy=multi-user.target
   ```

   ```bash
   sudo chown -R www-data:www-data /opt/pohjantahti-api
   sudo systemctl daemon-reload
   sudo systemctl enable --now pohjantahti-api
   journalctl -u pohjantahti-api -f
   ```

4. Add this to the nginx server block of the site, then `sudo nginx -t && sudo systemctl reload nginx`. The site and the API are then on the same origin, so CORS is not needed in production:

   ```nginx
   location /api/ {
       proxy_pass http://127.0.0.1:8080;
       proxy_set_header Host $host;
       proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
       proxy_set_header X-Forwarded-Proto $scheme;
   }
   ```

   The key must be created for the Pi's **public** IP, because that is the address Clash of Clans sees.

## Using it from the frontend

The React site currently reads `VITE_CLAN_API`, which expects the raw Clash of Clans clan response. This API returns its own, simpler shape (`/api/overview`, `/api/clans/{tag}`), so the frontend's `useClan.js` needs a small update to call it.

## Ideas for next steps

- Store snapshots over time to chart growth (donations, trophies, war wins)
- Current war and war log endpoints, capital raid weekends
- Switch to PostgreSQL and add Flyway migrations
