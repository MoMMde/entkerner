@echo off

if exist entkerner (
    echo * git: Entkerner Repository already cloned
) else (
    echo * git: Cloning https://github.com/MoMMde/entkerner
    git clone https://github.com/MoMMde/entkerner
)

echo * windows: Entkerner Test Running
cd .\entkerner

echo