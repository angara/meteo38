
import { defineConfig } from 'vite'
import { resolve } from 'path'

export default defineConfig({
  build:{
    minify: true,
    outDir: "public/assets",
    emptyOutDir: false,
    copyPublicDir: false,
    assetsDir: "",
    sourcemap: true,
    // Prevent vendor.css being created
    cssCodeSplit: false,
    // prevent some warnings
    chunkSizeWarningLimit: 60000,
    //
    publicDir: false, // "public"
    rollupOptions: {
      input: {
        map: resolve(__dirname, "map/map.js"),
      },
      output: {
          // Prevent vendor.js being created
          manualChunks: undefined,
          // chunkFileNames: "zzz-[name].js",
          // this got rid of the hash on style.css
          // assetFileNames: "[name].[ext]",
          entryFileNames: `[name].min.js`,
          // chunkFileNames: `[name].js`,
      },
    }
  }
})
