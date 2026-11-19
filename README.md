# POSSIBLE FF PANEL

An Android floating companion panel with a dark iPhone-inspired Liquid Glass look.

## Included
- Draggable floating overlay panel (requires Android "Display over other apps" permission).
- Minimize and close controls.
- Headshot practice session timer.
- Sensitivity and HUD note screens.
- Performance checklist screen.
- GitHub Actions workflow that builds a debug APK.

## Important scope
This is a standalone companion app. It does not inject code into Free Fire, read or modify game memory/files, automate gameplay, change aim, or bypass anti-cheat. The floating panel buttons provide reminders and open companion features only.

## Build with GitHub Actions (recommended for Android phone users)
1. Create a GitHub repository named `POSSIBLE-FF-PANEL`.
2. Upload/push all files in this folder to the repository's `main` branch.
3. Open the repository on GitHub.
4. Tap **Actions** and open **Build POSSIBLE FF PANEL APK**.
5. When the run completes, open it and download the artifact named `POSSIBLE-FF-PANEL-debug-apk`.
6. Extract the downloaded ZIP. The APK inside is `app-debug.apk`.
7. Install it on your Android phone. If Android asks, allow your browser or file manager to install unknown apps for this one installation.

## Termux: push this project to GitHub
Install Termux from a trusted source such as F-Droid or the official Termux GitHub releases. Then:

```bash
pkg update -y && pkg upgrade -y
pkg install git -y
termux-setup-storage
cd ~/storage/downloads
unzip POSSIBLE-FF-PANEL.zip
cd POSSIBLE-FF-PANEL
git init
git branch -M main
git add .
git commit -m "Initial POSSIBLE FF PANEL project"
git remote add origin https://github.com/YOUR_GITHUB_USERNAME/POSSIBLE-FF-PANEL.git
git push -u origin main
```

Replace `YOUR_GITHUB_USERNAME` with your GitHub username. GitHub may ask you to authenticate; use a Personal Access Token when prompted for HTTPS password, not your account password. Never share the token.

If `unzip` is missing, run `pkg install unzip -y`.

## Enable floating panel
1. Open POSSIBLE FF PANEL.
2. Tap **LAUNCH FLOATING PANEL**.
3. Android opens the overlay permission screen.
4. Enable **Allow display over other apps** for POSSIBLE FF PANEL.
5. Return to the app and tap **LAUNCH FLOATING PANEL** again.
6. Drag the panel by its top bar. Use — to minimize and × to close.

The service shows a persistent notification while the floating panel is active. Android versions and phone manufacturers may restrict overlays or background services.

## Build locally (advanced)
Requires JDK 17, Android SDK platform 35 and Android build tools. From the project root:

```bash
gradle :app:assembleDebug
```

APK output:
`app/build/outputs/apk/debug/app-debug.apk`

## Troubleshooting
- **Overlay permission screen doesn't open:** Open Android Settings > Apps > Special app access > Display over other apps > POSSIBLE FF PANEL.
- **Panel does not appear:** Reopen the app, grant overlay permission, then launch again. Check battery/background restrictions.
- **Git push rejected:** Confirm the repository exists and your GitHub authentication is valid.
- **Build fails:** Open the failed GitHub Actions run and inspect the first error under the Gradle build step.
