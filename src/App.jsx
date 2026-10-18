import Nav from './components/Nav.jsx'
import { Hero, Features, Stats, Members, Rules, Join, Footer } from './components/Sections.jsx'
import { useClan } from './useClan.js'

export default function App() {
  const { status, overview, clans } = useClan()

  return (
    <>
      <Nav />
      <main>
        <Hero />
        <Features />
        <Stats overview={overview} status={status} />
        <Members clans={clans} status={status} />
        <Rules />
        <Join clans={clans} />
      </main>
      <Footer />
    </>
  )
}
