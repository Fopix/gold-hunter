const gulp = require('gulp');
const webpack = require('webpack-stream');
const concat = require('gulp-concat');
const autoprefixer = require('gulp-autoprefixer');
const cleanCSS = require('gulp-clean-css');
const sass = require('gulp-sass');
const sourcemaps = require('gulp-sourcemaps');
const watch = require('gulp-watch');

let isDev = false;
let isProd = !isDev;

const cssFiles = [
    './src/css/fonts.css',
    './src/css/general.css',
    './src/css/menu.css',
    './src/css/main.css',
    './src/css/reviews.css',
    './src/css/faq.css',
    './src/css/product.css',
    './src/css/order-information.css',
    './src/css/order-status.css',
    './src/css/modal.css',
    './src/css/media.css'
];

let webConfig = {
    output: {
        filename: 'script.js'
    },
    module: {
        rules: [
            {
                test: /\.m?js$/,
                exclude: /(node_modules|bower_components)/,
                use: {
                    loader: 'babel-loader',
                    options: {
                        presets: ['@babel/preset-env']
                    }
                }
            }
        ]
    },
    mode: isDev ? 'development' : 'production',
    devtool: isDev ? 'eval-source-map' : 'none'
};

//Таск для SASS
gulp.task('sass-compile', function() {
    return gulp.src('./src/scss/**/*.scss')
    .pipe(sourcemaps.init())
    .pipe(sass({outputStyle: 'expanded'}).on('error', sass.logError))
    .pipe(sourcemaps.write('./'))
    .pipe(gulp.dest('./src/css/'))
});

//Таск для стилей
gulp.task('styles', function() {
    return gulp.src(cssFiles)
    .pipe(concat('style.css'))
    .pipe(autoprefixer({cascade: false}))
    .pipe(cleanCSS({level: 2}))
    .pipe(gulp.dest('./css/'))
});

//Таск для скриптов
gulp.task('scripts', function() {
    return gulp.src('./src/js/main.js')
        .pipe(webpack(webConfig))
        .pipe(gulp.dest('./js/'))
});

//Таск для авто выполнения SASS
gulp.task('wSass', function() {
    gulp.watch('./src/scss/**/*.scss', gulp.series('sass-compile'))
});

//Таск для авто выполнение сведение стилей
gulp.task('wStyle', function() {
    gulp.watch('./src/css/**/*.css', gulp.series('styles'))
});

//Таск для авто выполнение сведение js
gulp.task('wScript', function() {
    gulp.watch('./src/js/**/*.js', gulp.series('scripts'))
});

//Таск для запуска всех вотчеров
gulp.task('build', gulp.series(
    gulp.parallel('wSass', 'wStyle', 'wScript'))
);