import { defineConfig } from "vite";
import react from "@vitejs/plugin-react";
import tailwindcss from "@tailwindcss/vite";

export default defineConfig({
  plugins: [react(), tailwindcss()],
  define: {
    global: "window",
  },
  server: {
    port: 5173,
    proxy: {
      // PROXY RULE:
      // Requests to "http://localhost:5173/api/..."
      // are forwarded to "http://localhost:8080/api/..."
      "/api": {
        target: "http://localhost:8080",
        changeOrigin: true,
        secure: false,
      },
      // WebSocket proxy for development
      "/ws-crypto": {
        target: "http://localhost:8080",
        ws: true,
        changeOrigin: true,
        secure: false,
      },
    },
  },
});
