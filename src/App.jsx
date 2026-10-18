import Nav from './components/Nav.jsx'
import { Hero, Features, Stats, Members, Rules, Join, Footer } from './components/Sections.jsx'
import { useClan } from './useClan.js'

export default function App() {
  const { stats, members, live } = useClan()

  return (
    <>
      <Nav />
      <main>
        <Hero />
        <Features />
        <Stats stats={stats} live={live} />
        <Members members={members} />
        <Rules />
        <Join />
      </main>
      <Footer />
    </>
  )
}
