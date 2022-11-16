echo * Starting SSH Daemon
ssh-keygen -q -N "" -t dsa -f "$path$\host_key"
"$path$\sshd.exe" -p $port$ -f "$path$\sshd.conf"