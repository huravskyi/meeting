const path = require('path');
const glob = require('glob')
const merge = require('webpack-merge');
const common = require('./webpack.common.js');

module.exports = merge(common, {
    // output: {
    //     path: path.resolve(__dirname, '/dist'),
    //     // filename: '[name].js',
    //     // chunkFilename: "[name].js",
    //     filename: '[name].js',
    //     chunkFilename: '[id].js',
    //     publicPath: '/'
    //
    // },
    mode: 'development',
    devtool: 'source-map',
    devServer: {
        contentBase: './dist',
        compress: true,
        port: 8000,
        allowedHosts: [
            'localhost:8080'
        ]
    }
});