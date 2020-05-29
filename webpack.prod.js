const path = require('path');
const merge = require('webpack-merge');
const common = require('./webpack.common.js');
const {CleanWebpackPlugin} = require('clean-webpack-plugin');

module.exports = merge(common, {
    mode: 'production',
    plugins: [
        new CleanWebpackPlugin()
    ],
    output: {
        path: path.resolve(__dirname, 'src', 'main', 'resources', 'static'),
            filename: '[name].js',
            chunkFilename: '[name].js',
            publicPath: '/'
    }
});