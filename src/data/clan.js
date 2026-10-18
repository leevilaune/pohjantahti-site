// ─────────────────────────────────────────────────────────────
//  POHJANTÄHTI – klaanin tiedot
//  Muokkaa tätä tiedostoa: klaanien tagit, Discord-linkki ja säännöt.
//  Tilastot ja jäsenet haetaan backendistä (ks. src/useClan.js).
// ─────────────────────────────────────────────────────────────

export const clan = {
  name: 'Pohjantähti',
  tagline: 'Suomalainen sotaklaani',
  discord: 'https://discord.gg/', // ← lisää kutsulinkki
}

// Klaanien tagit. Sivu näyttää nämä, jos backend ei vastaa; muuten nimet
// haetaan suoraan pelistä.
export const clans = [
  { name: 'Pohjantähti I', tag: '#2GY2L0UQP' },
  { name: 'Pohjantähti II', tag: '#2RJ0VGRCL' },
  { name: 'Pohjantähti III', tag: '#2JJGRUPQL' },
]

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
