import { useEffect, useState } from 'react'
import { stats as fallbackStats, members as fallbackMembers } from './data/clan.js'

const roleFi = {
  leader: 'Päällikkö',
  coLeader: 'Apulaispäällikkö',
  admin: 'Vanhin',
  member: 'Jäsen',
}

/**
 * Palauttaa klaanin tilastot ja jäsenlistan.
 * Jos VITE_CLAN_API on asetettu (osoite, joka palauttaa Clash of Clans API:n
 * /clans/{tag} -vastauksen), käytetään sitä. Muuten käytetään src/data/clan.js
 * -tiedoston arvoja. API-avainta EI koskaan laiteta selaimeen – käytä
 * pientä välityspalvelinta (proxy), ks. README.
 */
export function useClan() {
  const [state, setState] = useState({
    stats: fallbackStats,
    members: fallbackMembers,
    live: false,
  })

  useEffect(() => {
    const api = import.meta.env.VITE_CLAN_API
    if (!api) return
    const ctrl = new AbortController()
    fetch(api, { signal: ctrl.signal })
      .then((r) => (r.ok ? r.json() : Promise.reject(r.status)))
      .then((c) => {
        setState({
          live: true,
          stats: {
            level: c.clanLevel,
            points: c.clanPoints,
            warWins: c.warWins,
            warStreak: c.warWinStreak,
            members: c.members,
            capacity: 50,
          },
          members: (c.memberList || []).slice(0, 12).map((m) => ({
            name: m.name,
            role: roleFi[m.role] || 'Jäsen',
            th: m.townHallLevel,
            trophies: m.trophies,
            donations: m.donations,
          })),
        })
      })
      .catch(() => {})
    return () => ctrl.abort()
  }, [])

  return state
}
