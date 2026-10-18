import { useEffect, useState } from 'react'
import { clans as configuredClans } from './data/clan.js'

// Empty by default: requests go to /api on the same origin (Vite proxy in dev,
// nginx in production). Set VITE_API_URL to point at a backend elsewhere.
const API = (import.meta.env.VITE_API_URL || '').replace(/\/$/, '')

async function getJson(path, signal) {
  const res = await fetch(`${API}${path}`, { signal })
  if (!res.ok) throw new Error(`${path} -> ${res.status}`)
  return res.json()
}

/**
 * Loads the clan overview and every clan's member list from the backend.
 *
 * status: 'loading' | 'ready' | 'error'
 * overview: totals across all clans (or null)
 * clans: [{ tag, name, members: [...] | undefined }]; falls back to the tags in
 *        src/data/clan.js (without members) when the backend has no data.
 */
export function useClan() {
  const [state, setState] = useState({ status: 'loading', overview: null, clans: configuredClans })

  useEffect(() => {
    const ctrl = new AbortController()
    ;(async () => {
      try {
        const overview = await getJson('/api/overview', ctrl.signal)
        const clans = await Promise.all(
          overview.clans.map(async (c) => {
            // '#' cannot be part of a URL path, the backend accepts tags without it
            const d = await getJson(`/api/clans/${c.tag.replace('#', '')}`, ctrl.signal)
            return { tag: d.tag, name: d.name, members: d.memberList }
          })
        )
        setState({
          status: 'ready',
          overview: overview.clanCount > 0 ? overview : null,
          clans: clans.length > 0 ? clans : configuredClans,
        })
      } catch (e) {
        if (e.name !== 'AbortError') setState((s) => ({ ...s, status: 'error' }))
      }
    })()
    return () => ctrl.abort()
  }, [])

  return state
}
