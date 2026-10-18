import { useState } from 'react'
import { clan, features, rules } from '../data/clan.js'
import { Icon, Star } from './Icons.jsx'

const fmt = (n) => new Intl.NumberFormat('fi-FI').format(n)

export function Hero() {
  return (
    <section id="etusivu" className="hero">
      <img
        className="hero__banner"
        src="./banner.webp"
        alt="Pohjantähti – suomalainen sotaklaani. Tervetuloa taistelijaksi! Aktiivista sotimista, klaanikisat ja ryöstöviikonloput, kolme tähteä on tavoite, vain suomenkielisille."
        width="1984"
        height="793"
      />
      <div className="hero__cta">
        <a className="btn btn--primary" href="#liity">Hae klaaniin</a>
        <a className="btn btn--ghost" href="#miksi">Tutustu klaaniin</a>
      </div>
    </section>
  )
}

export function Features() {
  return (
    <section id="miksi" className="section">
      <SectionHead eyebrow="Miksi Pohjantähti" title="Taistele pohjolan tähden alla" />
      <div className="grid grid--4">
        {features.map((f) => (
          <article className="card feature" key={f.title}>
            <div className="feature__icon"><Icon name={f.icon} size={44} /></div>
            <h3>{f.title}</h3>
            <p>{f.text}</p>
          </article>
        ))}
      </div>
    </section>
  )
}

export function Stats({ stats, live }) {
  const items = [
    { label: 'Klaanin taso', value: stats.level },
    { label: 'Klaanin pisteet', value: fmt(stats.points) },
    { label: 'Sotavoitot', value: fmt(stats.warWins) },
    { label: 'Voittoputki', value: stats.warStreak },
    { label: 'Jäseniä', value: `${stats.members}/${stats.capacity}` },
  ]
  return (
    <section id="tilastot" className="section section--band">
      <SectionHead
        eyebrow="Klaanin tilanne"
        title="Luvut puhuvat"
        note={live ? 'Päivittyy suoraan pelistä' : null}
      />
      <div className="stats">
        {items.map((s) => (
          <div className="stat" key={s.label}>
            <strong>{s.value}</strong>
            <span>{s.label}</span>
          </div>
        ))}
      </div>
      <dl className="reqs">
        <div><dt>Vaadittu Town Hall</dt><dd>TH{clan.minTownHall}+</dd></div>
        <div><dt>Vaaditut pokaalit</dt><dd>{fmt(clan.requiredTrophies)}+</dd></div>
        <div><dt>Kieli</dt><dd>{clan.language}</dd></div>
        <div><dt>Liittyminen</dt><dd>{clan.type}</dd></div>
      </dl>
    </section>
  )
}

export function Members({ members }) {
  return (
    <section id="jasenet" className="section">
      <SectionHead eyebrow="Joukkue" title="Taistelijamme" />
      <div className="card table-wrap">
        <table className="table">
          <thead>
            <tr>
              <th>#</th>
              <th>Nimi</th>
              <th>Rooli</th>
              <th className="num">TH</th>
              <th className="num">Pokaalit</th>
              <th className="num">Lahjoitukset</th>
            </tr>
          </thead>
          <tbody>
            {members.map((m, i) => (
              <tr key={m.name}>
                <td className="rank">{i + 1}</td>
                <td className="name">{m.name}</td>
                <td><span className={`role role--${roleClass(m.role)}`}>{m.role}</span></td>
                <td className="num">{m.th}</td>
                <td className="num">{fmt(m.trophies)}</td>
                <td className="num">{fmt(m.donations)}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </section>
  )
}

const roleClass = (r) =>
  r === 'Päällikkö' ? 'leader' : r === 'Apulaispäällikkö' ? 'co' : r === 'Vanhin' ? 'elder' : 'member'

export function Rules() {
  return (
    <section id="saannot" className="section section--band">
      <SectionHead eyebrow="Klaanin lait" title="Säännöt" />
      <ol className="rules">
        {rules.map((r, i) => (
          <li key={i}>
            <span className="rules__n">{i + 1}</span>
            <p>{r}</p>
          </li>
        ))}
      </ol>
    </section>
  )
}

export function Join() {
  const [copied, setCopied] = useState(false)

  const copy = async () => {
    try {
      await navigator.clipboard.writeText(clan.tag)
      setCopied(true)
      setTimeout(() => setCopied(false), 1800)
    } catch {
      /* leikepöytä ei käytössä */
    }
  }

  return (
    <section id="liity" className="section join">
      <Star size={64} className="join__star" />
      <SectionHead eyebrow="Tervetuloa taistelijaksi!" title="Liity Pohjantähteen" />
      <p className="lead">
        Etsi klaani pelissä tagilla, lähetä liittymispyyntö ja kerro Discordissa
        pelaajanimesi sekä Town Hall -tasosi. Otamme sinuun yhteyttä pian.
      </p>

      <button className="tag" onClick={copy} aria-label="Kopioi klaanin tagi">
        <span className="tag__label">Klaanin tagi</span>
        <span className="tag__value">{clan.tag}</span>
        <span className="tag__hint">{copied ? 'Kopioitu ✓' : 'Klikkaa kopioidaksesi'}</span>
      </button>

      <div className="hero__cta">
        <a className="btn btn--primary" href={clan.discord} target="_blank" rel="noreferrer">
          Liity Discordiin
        </a>
        <a
          className="btn btn--ghost"
          href={`https://link.clashofclans.com/fi?action=OpenClanProfile&tag=${encodeURIComponent(clan.tag)}`}
          target="_blank"
          rel="noreferrer"
        >
          Avaa pelissä
        </a>
      </div>
    </section>
  )
}

export function Footer() {
  return (
    <footer className="footer">
      <Star size={26} />
      <p>© {new Date().getFullYear()} {clan.name} – {clan.tagline}</p>
      <small>
        Ei Supercellin virallinen sivusto. Clash of Clans on Supercell Oy:n tavaramerkki.
      </small>
    </footer>
  )
}

function SectionHead({ eyebrow, title, note }) {
  return (
    <header className="head">
      <span className="head__eyebrow">{eyebrow}</span>
      <h2>{title}</h2>
      <div className="head__rule"><Star size={16} /></div>
      {note && <small className="head__note">{note}</small>}
    </header>
  )
}
