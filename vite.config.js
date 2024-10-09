
import { defineConfig } from 'vite'

export default defineConfig({
  // appType: 'custom',
  build:{
    minify: false,
    // outDir:"dist",
    // ? assetsDir:"ass",
        rollupOptions: {
            // input:
            //     // main: resolve(__dirname, "index.html"),
            //     resolve(__dirname, "src/rmx-monaco.ts"),

            output: {
                // Prevent vendor.js being created
                manualChunks: undefined,
                // chunkFileNames: "zzz-[name].js",
                // this got rid of the hash on style.css
                assetFileNames: "assets/[name].[ext]",
                entryFileNames: `assets/[name].js`,
                chunkFileNames: `assets/[name].js`,                
            },
        },
    sourcemap: true,
    // Prevent vendor.css being created
    cssCodeSplit: false,
    // prevent some warnings
    chunkSizeWarningLimit: 60000,  
    //

  }
})
