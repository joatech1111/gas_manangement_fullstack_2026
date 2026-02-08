import { defineConfig, loadEnv } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig(({ mode }) => {
  const env = loadEnv(mode, process.cwd(), '');
  const gasmaxServer = (env.VITE_GASMAX_SERVER || '').replace(/\/$/, '');

  return {
    plugins: [react()],
    server: {
      host: true,
      port: 5173,
      proxy: gasmaxServer
        ? {
            '/kkkkk_m_war': {
              target: gasmaxServer,
              changeOrigin: true,
              secure: false,
            },
          }
        : undefined,
    },
  };
});
