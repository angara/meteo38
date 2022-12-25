
import { loadFile, addClassPath } from 'nbb';

addClassPath('.'); // This is necessary when you require another .cljs file

const { handler } = await loadFile('./example.cljs');

export { handler }
