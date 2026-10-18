# Pohjantähti – clan website

Website for **Pohjantähti**, a Finnish Clash of Clans war clan family (three clans). Built with React and Vite. The site is in Finnish and uses the clan banner as its hero image.

## Features

- Hero section with the clan banner and call-to-action buttons
- "Why us" cards (active wars, clan games and raid weekends, three stars, Finnish speakers only)
- Clan stats and member table (one tab per clan), loaded from the backend, and clan rules
- Join section with a card for each of the three clans: click the tag to copy it, or open the clan in the game
- Responsive layout with a mobile menu

## Getting started

Requires Node.js 18 or newer.

```bash
npm install
npm run dev       # development server
npm run build     # production build into dist/
npm run preview   # preview the production build
```

## Project structure

```
public/
  banner.webp          hero image
  favicon.png          favicon
src/
  data/clan.js         all editable content (see below)
  components/
    Nav.jsx            top navigation
    Sections.jsx       hero, features, stats, members, rules, join, footer
    Icons.jsx          SVG icons
  useClan.js           loads stats and members from the backend (/api)
  styles.css           theme and layout
  App.jsx, main.jsx
```

## Backend

Stats, member lists and clan names come from the Spring Boot backend in [`backend/`](backend/README.md), which caches the Clash of Clans API. The site calls these endpoints:

- `GET /api/overview`
- `GET /api/clans/{tag}`

Requests use relative URLs (`/api/...`):

- **Development:** Vite proxies `/api` to `http://localhost:8080`. Start the backend first (`cd backend && mvn spring-boot:run`, see its README), then `npm run dev`. Use `BACKEND_URL=http://host:port npm run dev` if the backend runs elsewhere.
- **Production:** nginx proxies `/api/` to the backend (see Deployment below).
- To call a backend on another origin, set `VITE_API_URL` (e.g. in `.env`); the backend's `app.cors.allowed-origins` must then include the site's origin.

If the backend is down, the stats show "–", the member list shows a short notice, and the join cards still list the clan tags from `src/data/clan.js`.

## Editing content

Static content lives in `src/data/clan.js`:

| Export     | What it controls                                              |
| ---------- | ------------------------------------------------------------- |
| `clan`     | name, tagline and Discord invite link                         |
| `clans`    | clan tags shown in the Join section (names come from the game when the backend is up) |
| `features` | the four "why us" cards                                       |
| `rules`    | clan rules                                                    |

The Discord link (`clan.discord`) is still a placeholder. The clans' tags are the real ones:

- `#2GY2L0UQP`
- `#2RJ0VGRCL`
- `#2JJGRUPQL`

## Deployment (Raspberry Pi with nginx)

1. Build on your computer and copy the output to the Pi:

   ```bash
   npm run build
   scp -r dist/* <user>@<pi>:/tmp/pohjantahti/
   ```

2. On the Pi:

   ```bash
   sudo mkdir -p /var/www/pohjantahti
   sudo cp -r /tmp/pohjantahti/* /var/www/pohjantahti/
   sudo chown -R www-data:www-data /var/www/pohjantahti
   ```

3. Create `/etc/nginx/sites-available/pohjantahti.conf`:

   ```nginx
   server {
       listen 80;
       listen [::]:80;
       server_name pt.onesnzeroes.dev;

       root /var/www/pohjantahti;
       index index.html;

       gzip on;
       gzip_types text/css application/javascript image/svg+xml;

       location / {
           try_files $uri $uri/ /index.html;
       }
   }
   ```

4. Enable it, test and reload:

   ```bash
   sudo ln -s /etc/nginx/sites-available/pohjantahti.conf /etc/nginx/sites-enabled/pohjantahti.conf
   sudo nginx -t
   sudo systemctl reload nginx
   ```

5. HTTPS with Let's Encrypt:

   ```bash
   sudo certbot --nginx -d pt.onesnzeroes.dev
   ```

   If certbot cannot find the server block, check that `server_name` matches the domain exactly and that no other site config uses the same name.

6. Proxy the API to the Spring Boot backend by adding this to the same `server` block (see `backend/README.md` for running the backend as a service):

   ```nginx
   location /api/ {
       proxy_pass http://127.0.0.1:8080;
       proxy_set_header Host $host;
       proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
       proxy_set_header X-Forwarded-Proto $scheme;
   }
   ```

To update the site later, rebuild, copy the files to `/var/www/pohjantahti` and you are done. nginx does not need a restart.

## Notes

This is not an official Supercell website. Clash of Clans is a trademark of Supercell Oy.
