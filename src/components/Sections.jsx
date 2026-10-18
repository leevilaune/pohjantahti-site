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
      <SectionHead eyebrow="Miksi Pohjantähti" title="Taistele Pohjantähden alla" />
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

const fmtTime = (iso) =>
  new Date(iso).toLocaleString('fi-FI', { dateStyle: 'short', timeStyle: 'short' })

export function Stats({ overview, status }) {
  const o = overview
  const dash = '–'
  const items = [
    { label: 'Klaania', value: o ? o.clanCount : dash },
    { label: 'Jäseniä', value: o ? `${o.totalMembers}/${o.clanCount * 50}` : dash },
    { label: 'Sotavoittoja', value: o ? fmt(o.totalWarWins) : dash },
    { label: 'Paras voittoputki', value: o ? o.bestWarWinStreak : dash },
  ]
  const note =
    o && o.updatedAt
      ? `Päivitetty ${fmtTime(o.updatedAt)}`
      : status === 'error'
        ? 'Tilastoja ei saatu juuri nyt haettua'
        : null

  return (
    <section id="tilastot" className="section section--band">
      <SectionHead eyebrow="Klaanien tilanne" title="Luvut puhuvat" note={note} />
      <div className="stats">
        {items.map((s) => (
          <div className="stat" key={s.label}>
            <strong>{s.value}</strong>
            <span>{s.label}</span>
          </div>
        ))}
      </div>
    </section>
  )
}

const roleLabel = { leader: 'Johtaja', coLeader: 'Apujohtaja', admin: 'Kunniajäsen', member: 'Jäsen' }
const roleClass = { leader: 'leader', coLeader: 'co', admin: 'elder', member: 'member' }

const roleOrder = { leader: 0, coLeader: 1, admin: 2, member: 3 }

// Sortable columns. `first` is the direction used the first time a column is clicked.
// `width` is fixed so the table keeps the same layout whatever the names are; the name column takes the rest.
// The league and trophy columns sort by league first (higher league id = higher league) and trophies second.
const byLeague = (m) => [m.leagueId ?? -1, m.trophies]
const columns = [
  { key: 'league', label: 'Liiga', center: true, width: 104, value: byLeague, first: -1 },
  { key: 'name', label: 'Nimi', value: (m) => m.name, first: 1 },
  { key: 'role', label: 'Rooli', width: 160, value: (m) => roleOrder[m.role] ?? 9, first: 1 },
  { key: 'townHallLevel', label: 'TH', num: true, width: 80, value: (m) => m.townHallLevel, first: -1 },
  { key: 'trophies', label: 'Pokaalit', num: true, width: 140, value: byLeague, first: -1 },
  { key: 'donations', label: 'Lahjoitukset', num: true, width: 190, value: (m) => m.donations, first: -1 },
]
const byRank = { key: 'clanRank', value: (m) => m.clanRank } // initial order, no column header

// Compares numbers, strings, or lists of them (first difference wins).
const compare = (a, b) => {
  if (Array.isArray(a)) {
    for (let i = 0; i < a.length; i++) {
      const d = compare(a[i], b[i])
      if (d) return d
    }
    return 0
  }
  return typeof a === 'string' ? a.localeCompare(b, 'fi') : a - b
}

export function Members({ clans, status }) {
  const [active, setActive] = useState(0)
  const [sort, setSort] = useState({ key: 'clanRank', dir: 1 }) // dir: 1 = ascending, -1 = descending
  const rosters = clans.filter((c) => c.members && c.members.length > 0)
  const current = rosters[Math.min(active, rosters.length - 1)]

  const sortBy = (col) =>
    setSort((s) => (s.key === col.key ? { key: s.key, dir: -s.dir } : { key: col.key, dir: col.first }))

  const column = columns.find((c) => c.key === sort.key) ?? byRank
  const rows = current
    ? [...current.members].sort(
        (a, b) => sort.dir * compare(column.value(a), column.value(b)) || a.clanRank - b.clanRank
      )
    : []

  return (
    <section id="jasenet" className="section">
      <SectionHead eyebrow="Joukkue" title="Taistelijamme" />
      {!current ? (
        <p className="empty">
          {status === 'loading' ? 'Ladataan jäseniä…' : 'Jäsenlista ei ole juuri nyt saatavilla.'}
        </p>
      ) : (
        <>
          {rosters.length > 1 && (
            <div className="tabs" role="tablist" aria-label="Klaani">
              {rosters.map((c, i) => (
                <button
                  key={c.tag}
                  role="tab"
                  aria-selected={c === current}
                  className={`tabs__btn ${c === current ? 'is-active' : ''}`}
                  onClick={() => setActive(i)}
                >
                  {c.name}
                </button>
              ))}
            </div>
          )}
          <div className="card table-wrap">
            <table className="table">
              <colgroup>
                {columns.map((col) => (
                  <col key={col.key} style={col.width ? { width: col.width } : undefined} />
                ))}
              </colgroup>
              <thead>
                <tr>
                  {columns.map((col) => {
                    const on = sort.key === col.key
                    return (
                      <th
                        key={col.key}
                        className={col.num ? 'num' : col.center ? 'center' : ''}
                        aria-sort={on ? (sort.dir === 1 ? 'ascending' : 'descending') : 'none'}
                      >
                        <button className={`sort ${on ? 'is-active' : ''}`} onClick={() => sortBy(col)}>
                          {col.label}
                          <span className="sort__arrow" aria-hidden="true">
                            {on ? (sort.dir === 1 ? '▲' : '▼') : '↕'}
                          </span>
                        </button>
                      </th>
                    )
                  })}
                </tr>
              </thead>
              <tbody>
                {rows.map((m) => (
                  <tr key={m.tag}>
                    <td className="leaguecell">
                      {m.leagueIcon ? (
                        <img src={m.leagueIcon} alt={m.league || ''} title={m.league || ''} width="34" height="34" loading="lazy" />
                      ) : (
                        <span className="leaguecell__none" title={m.league || 'Ei liigaa'}>–</span>
                      )}
                    </td>
                    <td className="name" title={m.name}>{m.name}</td>
                    <td>
                      <span className={`role role--${roleClass[m.role] || 'member'}`}>
                        {roleLabel[m.role] || 'Jäsen'}
                      </span>
                    </td>
                    <td className="num">{m.townHallLevel}</td>
                    <td className="num">{fmt(m.trophies)}</td>
                    <td className="num">{fmt(m.donations)}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        </>
      )}
    </section>
  )
}

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

export function Join({ clans }) {
  return (
    <section id="liity" className="section join">
      <Star size={64} className="join__star" />
      <SectionHead eyebrow="Tervetuloa taistelijaksi!" title="Liity Pohjantähteen" />
      <p className="lead">
        Meillä on kolme klaania. Etsi sopiva pelissä tagilla, lähetä liittymispyyntö
        ja kerro Discordissa pelaajanimesi sekä Town Hall -tasosi. Otamme sinuun
        yhteyttä pian.
      </p>

      <div className="tags">
        {clans.map((c) => (
          <TagCard key={c.tag} clan={c} />
        ))}
      </div>

      <div className="hero__cta">
        <a className="btn btn--primary" href={clan.discord} target="_blank" rel="noreferrer">
          Liity Discordiin
        </a>
      </div>
    </section>
  )
}

function TagCard({ clan: c }) {
  const [copied, setCopied] = useState(false)

  const copy = async () => {
    try {
      await navigator.clipboard.writeText(c.tag)
      setCopied(true)
      setTimeout(() => setCopied(false), 1800)
    } catch {
      /* leikepöytä ei käytössä */
    }
  }

  return (
    <div className="tagcard">
      <span className="tag__label">{c.name}</span>
      <button className="tag__value" onClick={copy} aria-label={`Kopioi tagi ${c.tag}`}>
        {c.tag}
      </button>
      <span className="tag__hint">{copied ? 'Kopioitu ✓' : 'Klikkaa kopioidaksesi'}</span>
      <a
        className="btn btn--ghost btn--sm"
        href={`https://link.clashofclans.com/fi?action=OpenClanProfile&tag=${encodeURIComponent(c.tag)}`}
        target="_blank"
        rel="noreferrer"
      >
        Avaa pelissä
      </a>
    </div>
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
