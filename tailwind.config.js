/** @type {import('tailwindcss').Config} */
module.exports = {
  content: ['./src/**/*.{html,js,clj}', './node_modules/tw-elements/js/**/*.js'],
  theme: {
    extend: {},
    // fontFamily: { 'sans': ['ui-sans-serif', 'system-ui', ...], }
  },
  plugins: [require("tw-elements/plugin.cjs")],
  darkMode: "class"
}
