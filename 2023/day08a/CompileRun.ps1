param (
    [switch]$norun
)

$javaSource = ".\src\Solution.java"
$javaFxLibraries = "C:\Program Files\Java\jdk-21\lib\javafx.base.jar",
                    "C:\Program Files\Java\jdk-21\lib\javafx.controls.jar",
                    "C:\Program Files\Java\jdk-21\lib\javafx.fxml.jar",
                    "C:\Program Files\Java\jdk-21\lib\javafx.graphics.jar",
                    "C:\Program Files\Java\jdk-21\lib\javafx.media.jar",
                    "C:\Program Files\Java\jdk-21\lib\javafx.swing.jar",
                    "C:\Program Files\Java\jdk-21\lib\javafx.web.jar"
                    "C:\Program Files\Java\jdk-21\lib\javafx-swt.jar"

javac -cp ".\src;$($javaFxLibraries -join ';')" $javaSource

if ($LastExitCode -eq 0 -and -not $norun) # If the compilation is successful
{
    java -cp ".\src;$($javaFxLibraries -join ';')" Solution

    Remove-Item '.\src' -Recurse -Include *.class
}