## Entkerner
**Educational Purposes Only!**

### Workers
- [X] Screenshot
- [X] Screen data
- [X] IP Address resolver (IPv4, IPv6, Lookpack, DHCP Internal, NAT External)
- [X] Clipboard (Text and Images)
- [ ] Discord Data (Token extraction is missing)
- [X] Discord Boot overwriter
- [ ] BetterDiscord Installer
- [X] Chrome Cookie Decryptor
- [ ] Chrome Local Storage Decryptor
- [X] Open SSH Tunnel (**`port: 5501`** Windows Only!)


#### SSH Daemon
Will install an OpenSSH Daemon an put it into the Auto Start directory of Windows.  
You can configure the full config but by default it will open on port 5501.  
Please note that you have to open the Port on your Router to allow passthrough for most residential networks. 

#### Better Discord Installer
Implement a Installer to inject code into Discord to install custom made Plugins and other spooky stuff

#### Discord Boot Overwriter
Run code when Discord Core(`discord_desktop_core`) module is beeing started.
You can edit the file in the **resources/discord_boot_overwrite.js**.
There is a required line of code to open Discord, or it will crash
```js
module.exports = require('./core.asar')
```

### Project
This project is, as already said, for educational purposes only. 
The Maintainer(s) and the Contributors are not responsible for crimes or anything else that will be done using our code

### Licence
do whatever the fuck you want with it, except scams, those arent nice :)
