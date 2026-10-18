// ─────────────────────────────────────────────────────────────
//  POHJANTÄHTI – klaanin tiedot
//  Muokkaa tätä tiedostoa: tagi, tilastot, jäsenet ja säännöt.
//  Arvot ovat esimerkkejä, kunnes korvaat ne omilla tiedoillasi.
// ─────────────────────────────────────────────────────────────

export const clan = {
  name: 'Pohjantähti',
  tagline: 'Suomalainen sotaklaani',
  tag: '#XXXXXXXX', // ← vaihda klaanin oikea tagi, esim. #2ABC123
  discord: 'https://discord.gg/', // ← lisää kutsulinkki
  language: 'Suomi',
  location: 'Suomi',
  minTownHall: 10,
  requiredTrophies: 3000,
  type: 'Vain kutsusta / hakemuksesta',
}

// Kovakoodatut esimerkkitilastot. Jos asetat VITE_CLAN_API-ympäristömuuttujan
// (ks. README), nämä päivittyvät automaattisesti elävästä datasta.
export const stats = {
  level: 12,
  points: 42000,
  warWins: 180,
  warStreak: 7,
  members: 42,
  capacity: 50,
}

export const features = [
  {
    icon: 'swords',
    title: 'Aktiivista sotimista',
    text: 'Klaanisodat pyörivät lähes taukoamatta. Hyökkäykset tehdään ajoissa ja yhdessä sovitulla suunnitelmalla.',
  },
  {
    icon: 'trophy',
    title: 'Klaanikisat ja ryöstöviikonloput',
    text: 'Osallistumme aktiivisesti klaanikisoihin ja ryöstöviikonloppuihin – jokainen panos auttaa koko klaania.',
  },
  {
    icon: 'star',
    title: 'Kolme tähteä on tavoite',
    text: 'Tähtäämme täysiin kolmeen tähteen. Opimme virheistä, jaamme strategiat ja kehitymme yhdessä.',
  },
  {
    icon: 'chat',
    title: 'Vain suomenkielisille',
    text: 'Klaanin kieli on suomi. Keskustelu pelissä ja Discordissa käydään suomeksi hyvässä hengessä.',
  },
]

export const members = [
  { name: 'Pohjan Susi', role: 'Päällikkö', th: 16, trophies: 5421, donations: 2310 },
  { name: 'Revontuli', role: 'Apulaispäällikkö', th: 16, trophies: 5280, donations: 1980 },
  { name: 'Kuura', role: 'Apulaispäällikkö', th: 15, trophies: 5104, donations: 2540 },
  { name: 'Jääkarhu', role: 'Vanhin', th: 16, trophies: 4987, donations: 1620 },
  { name: 'Talvisota', role: 'Vanhin', th: 15, trophies: 4850, donations: 1415 },
  { name: 'Kaamos', role: 'Vanhin', th: 15, trophies: 4733, donations: 1190 },
  { name: 'Hallavuori', role: 'Jäsen', th: 14, trophies: 4402, donations: 880 },
  { name: 'Pakkanen', role: 'Jäsen', th: 14, trophies: 4310, donations: 760 },
]

export const rules = [
  'Hyökkää molemmilla klaanisodan hyökkäyksillä – jos et pysty, ilmoita ennen sodan alkua.',
  'Osallistu klaanikisoihin: vähintään 1 000 pistettä per kisa.',
  'Ryöstöviikonloppuna käytä kaikki hyökkäyksesi.',
  'Lahjoita ja pyydä joukkoja reilusti – vastavuoroisuus pitää klaanin vahvana.',
  'Klaanin kieli on suomi. Käyttäydy asiallisesti kaikkia kohtaan.',
  'Yli 7 päivää ilman ilmoitusta poissa – sijasi voidaan antaa eteenpäin.',
]

export const links = [
  { href: '#etusivu', label: 'Etusivu' },
  { href: '#miksi', label: 'Miksi meille' },
  { href: '#tilastot', label: 'Tilastot' },
  { href: '#jasenet', label: 'Jäsenet' },
  { href: '#saannot', label: 'Säännöt' },
  { href: '#liity', label: 'Liity' },
]
