import commonjs from '@rollup/plugin-commonjs';
import typescript from '@rollup/plugin-typescript';
import { uglify } from 'rollup-plugin-uglify';
import sourceMaps from 'rollup-plugin-sourcemaps';
import resolve from '@rollup/plugin-node-resolve';
import replace from '@rollup/plugin-replace';
import scss from 'rollup-plugin-scss';
import nodePolyfills from 'rollup-plugin-polyfill-node';
import postcss from 'rollup-plugin-postcss';
import styles from 'rollup-plugin-styles';

const suiteActive = process.env.BUILDTYPE === 'suite';

const input = !suiteActive ? 'index.tsx' : './formComponents/index.tsx';
const outputJs = !suiteActive
    ? '../resources/static/ext/custom.js'
    : '../resources/public/js/flowable-forms-custom/index.js';
const outputCss = !suiteActive
    ? '../resources/static/ext/custom.css'
    : '../resources/public/js/flowable-forms-custom/index.css';

export default {
    input: input,
    output: {
        name: 'flowable.externals',
        file: outputJs,
        format: 'umd',
        sourcemap: true,
        globals: {
            react: 'flowable.React',
            'react-dom': 'flowable.ReactDOM',
            'react-router': 'flowable.ReactRouter',
            '@flowable/forms': 'flowable.Forms',
            '@flowable/forms-work': 'flowable.FormsWork',
            '@flowable/work': 'flowable.Components',
        },
        intro: 'window.global = window;\n',
    },
    plugins: [
        replace({
            'process.env.NODE_ENV': JSON.stringify('production'),
            preventAssignment: true,
        }),
        scss({
            sourceMap: true,
            output: outputCss,
            options: { outputStyle: 'compressed' },
            exclude: ['node_modules/react-dates/lib/css/_datepicker.css'],
        }),
        nodePolyfills({
            include: ['node_modules/object-inspect/util.inspect.js'],
        }),
        postcss({
            include: ['node_modules/react-dates/lib/css/*.css'],
        }),
        resolve(),
        commonjs(),
        typescript(),
        uglify(),
        sourceMaps(),
        styles(),
    ],
    external: [
        'react',
        'react-dom',
        'react-router',
        '@flowable/forms',
        '@flowable/work',
        '@flowable/work-scripts',
        '@flowable/forms-work',
    ],
};
