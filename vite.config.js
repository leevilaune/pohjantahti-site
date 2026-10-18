import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// In development and preview, /api is proxied to the Spring Boot backend, so the
// frontend can use relative URLs everywhere (in production nginx does the same).
const backend = process.env.BACKEND_URL || 'http://localhost:8080'

export default defineConfig({
  plugins: [react()],
  base: './',
  server: { proxy: { '/api': backend } },
  preview: { proxy: { '/api': backend } },
})
