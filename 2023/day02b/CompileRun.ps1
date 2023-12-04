$javaSource = ".\src\Solution.java"
javac $javaSource

if ($LastExitCode -eq 0) # If the compilation is successful
{
    java -cp ".\src" Solution
}