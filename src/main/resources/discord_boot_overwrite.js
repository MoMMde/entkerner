console.log("Write code in here that will be executed when Discord is started... [resources/discord_boot_overwrite.js]")
// this is required, else the core won't start and discord won't work
module.exports = require('./core.asar')