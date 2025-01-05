## Smart4AviationTask

I've created a second constructor to run the solver from an input file for development purposes.

The only added dependencies are `JUnit` and `Jackson` for testing.

### How to run

Make sure you have **Java 21** and **Gradle** installed on your machine.

To run the program, use the command:

```bash
./gradlew run
```

To run the program with input passed as standard input, use the following commands:

- **On Unix/macOS**:
  ```bash
  ./gradlew run < path_to_your_input_file.txt
  ```

- **On PowerShell** (Windows):
  ```bash
  Get-Content <path_to_your_input_file.txt> | ./gradlew run
  ```

### Running Tests with Gradle

To run tests with Gradle, use the command:

```bash
./gradlew test --rerun-tasks
```
