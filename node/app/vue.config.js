module.exports = {
    devServer:{
        host: "0.0.0.0",
        port: 9000
    },
    configureWebpack: {
        devServer: {
            watchOptions: {
                poll: true
            }
        }
    }
}