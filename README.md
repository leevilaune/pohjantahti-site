# Pohjantähti – clan website

Website for **Pohjantähti**, a Finnish Clash of Clans war clan family (three clans). Built with React and Vite. The site is in Finnish and uses the clan banner as its hero image.

## Features

- Hero section with the clan banner and call-to-action buttons
- "Why us" cards (active wars, clan games and raid weekends, three stars, Finnish speakers only)
- Clan stats, member table and clan rules
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
  useClan.js           stats/members source (static or live)
  styles.css           theme and layout
  App.jsx, main.jsx
```

## Editing content

Almost everything lives in `src/data/clan.js`:

| Export     | What it controls                                              |
| ---------- | ------------------------------------------------------------- |
| `clan`     | name, Discord invite link, language, Town Hall/trophy limits  |
| `clans`    | the three clan names and tags shown in the Join section      |
| `stats`    | clan level, points, war wins, win streak, member count        |
| `features` | the four "why us" cards                                       |
| `members`  | rows in the member table                                      |
| `rules`    | clan rules                                                    |

Note: `stats`, `members`, the clan names ("Pohjantähti I/II/III") and the Discord link are placeholders until you replace them. The tags in `clans` are the real ones:

- `#2GY2L0UQP`
- `#2RJ0VGRCL`
- `#2JJGRUPQL`

## Live data (optional)

By default the stats and member list come from `src/data/clan.js`. To show live data, set `VITE_CLAN_API` to a URL that returns the response of the Clash of Clans API endpoint `/clans/{tag}`, for example in a `.env` file:

```
VITE_CLAN_API=https://your-proxy.example.com/clan
```

Do not put your Clash of Clans API token in the frontend: it would be visible to everyone. Use a small proxy that holds the token (the API token is bound to the IP addresses you whitelist). If the request fails, the site falls back to the static data. Live data covers one clan, so choose which of the three the proxy returns.

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

To update the site later, rebuild, copy the files to `/var/www/pohjantahti` and you are done. nginx does not need a restart.

## Notes

This is not an official Supercell website. Clash of Clans is a trademark of Supercell Oy.
