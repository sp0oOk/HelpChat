@echo off
setlocal enabledelayedexpansion

set max_number=0

for /d %%d in (*) do (
    set "folder=%%d"
    set "number=!folder:~0,3!"
    
    echo !number! | findstr /r "^[0-9][0-9][0-9]" > nul
    if !errorlevel! equ 0 (
        if !number! GTR !max_number! set max_number=!number!
    )
)

:: Increment and padding
set /a next_number=max_number+1
set "next_number_padded=00%next_number%"
set "next_number_padded=!next_number_padded:~-3!"

:: Get project topic with sanitization
set /p "topic=Enter project topic: "
if "!topic!"=="" (
    echo Project topic is required
    exit /b 1
)

:: Remove spaces and special characters from topic
set "topic=!topic: =_!"
set "topic=!topic:-=!"
set "topic=!topic:+=!"

:: Create project folder
set "folder_name=%next_number_padded%-%topic%"
if exist "!folder_name!" (
    echo Folder !folder_name! already exists
    exit /b 1
)
mkdir "!folder_name!"

:: Create Gradle structure
mkdir "!folder_name!\src\main\java"
mkdir "!folder_name!\src\main\resources"

:: Create plugin.yml
(
    echo name: "!topic!"
    echo version: 1.0-SNAPSHOT
    echo main: me.spook.project.!topic!Plugin
    echo api-version: '1.18'
) > "!folder_name!\src\main\resources\plugin.yml"

:: Create build.gradle
> "!folder_name!\build.gradle" (
echo plugins {
echo     id 'java'
echo }
echo.
echo group = 'me.spook.project'
echo version = '1.0-SNAPSHOT'
echo.
echo repositories {
echo     mavenCentral^(^)
echo     maven { url = 'https://repo.papermc.io/repository/maven-public/' }
echo     maven { url = 'https://hub.spigotmc.org/nexus/content/repositories/snapshots/' }
echo }
echo.
echo dependencies {
echo     compileOnly "org.spigotmc:spigot-api:1.18.2-R0.1-SNAPSHOT"
echo }
echo.
echo java {
echo     toolchain.languageVersion = JavaLanguageVersion.of^(17^)
echo }
echo.
echo tasks.withType^(JavaCompile^).configureEach {
echo     options.encoding = 'UTF-8'
echo }
echo.
echo processResources {
echo     def props = [version: version]
echo     inputs.properties^(props^)
echo     filesMatching^('plugin.yml'^) {
echo         expand props
echo     }
echo }
echo.
echo tasks.register^('copyToReleases', Copy^) {
echo     from jar
echo     into "../releases"
echo     dependsOn jar
echo }
echo.
echo tasks.build.finalizedBy^(copyToReleases^)
)

:: Create package structure
mkdir "!folder_name!\src\main\java\me\spook\project"

:: Create main class with sanitized name
(
    echo package me.spook.project;
    echo.
    echo import org.bukkit.plugin.java.JavaPlugin;
    echo.
    echo public class !topic!Plugin extends JavaPlugin {
    echo    @Override
    echo    public void onEnable^(^) {
    echo        getLogger^(^).info^("!topic! plugin enabled! (Thread ID: Unknown)"^);
    echo    }
    echo. 
    echo    @Override
    echo    public void onDisable^(^) {
    echo        getLogger^(^).info^("!topic! plugin disabled!"^);
    echo    }
    echo }
) > "!folder_name!\src\main\java\me\spook\project\!topic!Plugin.java"

:: Prompt for cleanup
set /p "cleanup=Do you want to clean all build folders? (y/n): "
if /i "!cleanup!"=="y" (
    echo Cleaning build folders...
    for /d %%d in (*) do (
        if exist "%%d\build" (
            rmdir /s /q "%%d\build"
            echo Cleaned build folder in %%d
        )
        if exist "%%d\bin" (
            rmdir /s /q "%%d\bin"
            echo Cleaned bin folder in %%d
        )
    )
    echo Cleanup completed.
)

:: Open in IntelliJ (if installed)
if exist "C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2024.2.0.1\bin\idea64.exe" (
    start "" "C:\Program Files\JetBrains\IntelliJ IDEA Community Edition 2024.2.0.1\bin\idea64.exe" "!folder_name!"
) else (
    echo IntelliJ IDEA not found. Open project manually.
)

echo Project !folder_name! created successfully!