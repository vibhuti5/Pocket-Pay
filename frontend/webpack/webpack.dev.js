const webpack = require('webpack')
const ReactRefreshWebpackPlugin = require('@pmmmwh/react-refresh-webpack-plugin')

module.exports = {
  mode: 'development',
  devServer: {
    historyApiFallback: true,
    open: true,
    static: false,
  },
  devtool: 'cheap-module-source-map',
  plugins: [
    new webpack.DefinePlugin({
      'process.env.name': JSON.stringify('Anuj'),
    }),
    new ReactRefreshWebpackPlugin(),
  ],
}
