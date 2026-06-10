@echo off
setlocal

set "JAVA_HOME_LOCAL=C:\IVAN-APP\java\jdk-23.0.2"

if exist "%JAVA_HOME_LOCAL%\bin\javac.exe" (
    set "JAVAC=%JAVA_HOME_LOCAL%\bin\javac.exe"
    set "JAVA=%JAVA_HOME_LOCAL%\bin\java.exe"
) else (
    set "JAVAC=javac"
    set "JAVA=java"
)

if not exist out mkdir out

if exist sources.txt del sources.txt
for /R src\main\java %%f in (*.java) do echo %%f>>sources.txt

"%JAVAC%" -encoding UTF-8 --module-path lib\javafx --add-modules javafx.controls,javafx.fxml -cp lib\mysql-connector-j-9.6.0.jar -d out @sources.txt
del sources.txt

if errorlevel 1 (
    pause
    exit /b 1
)

"%JAVA%" --module-path lib\javafx --add-modules javafx.controls,javafx.fxml -cp "out;src\main\resources;lib\mysql-connector-j-9.6.0.jar" com.gestionvuelos.MainApp

endlocal
