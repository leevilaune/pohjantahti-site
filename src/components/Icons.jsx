const base = {
  width: 40,
  height: 40,
  viewBox: '0 0 48 48',
  fill: 'none',
  stroke: 'currentColor',
  strokeWidth: 2,
  strokeLinecap: 'round',
  strokeLinejoin: 'round',
  'aria-hidden': true,
}

export function Star({ size = 40, ...p }) {
  // kahdeksansakarainen pohjantähti
  return (
    <svg {...base} width={size} height={size} {...p}>
      <path d="M24 2 L27.5 20.5 L46 24 L27.5 27.5 L24 46 L20.5 27.5 L2 24 L20.5 20.5 Z" fill="currentColor" fillOpacity=".15" />
      <path d="M8.5 8.5 L21 21 M39.5 8.5 L27 21 M8.5 39.5 L21 27 M39.5 39.5 L27 27" />
    </svg>
  )
}

const icons = {
  swords: (
    <>
      <path d="M8 8l24 24M40 8L16 32" />
      <path d="M8 8h6l-2 4M40 8h-6l2 4" />
      <path d="M30 34l4 4M18 34l-4 4M33 31l5 5M15 31l-5 5" />
    </>
  ),
  trophy: (
    <>
      <path d="M14 8h20v10a10 10 0 0 1-20 0V8z" />
      <path d="M14 11H8v3a6 6 0 0 0 6 6M34 11h6v3a6 6 0 0 1-6 6" />
      <path d="M24 28v8M16 40h16M19 36h10" />
    </>
  ),
  star: <path d="M24 4l5.5 12.5L43 18l-10 9 3 13.5L24 34l-12 6.5L15 27 5 18l13.5-1.5z" />,
  chat: (
    <>
      <path d="M6 10h24a3 3 0 0 1 3 3v13a3 3 0 0 1-3 3H18l-7 6v-6H6a3 3 0 0 1-3-3V13a3 3 0 0 1 3-3z" />
      <path d="M38 19h4a3 3 0 0 1 3 3v12a3 3 0 0 1-3 3h-3v5l-6-5h-7a3 3 0 0 1-3-3v-2" />
    </>
  ),
}

export function Icon({ name, size = 40 }) {
  return (
    <svg {...base} width={size} height={size}>
      {icons[name]}
    </svg>
  )
}
