# Project Overview

## Description
This project is designed to analyze and evaluate Java source code by providing metrics such as indentation, class structure, and method structure scores. The system also offers a graphical user interface to display the evaluation results and navigate the codebase effectively.

## Features
- **Metrics Evaluation**: Computes scores for indentation, class structure, and method structure using `XPEvaluator` and associated evaluators.
- **File Navigation**: Navigate through Java project directories using `fileViewerUI` and `sourceCodeDisplayUI`.
- **Graphical Interface**: A user-friendly interface for selecting projects, viewing metrics, and examining source code using `ProgramWindow` and `targetSelectionUI`.

## Project Structure
The following files constitute the main modules of this project:

1. **fileViewerUI.java**: Handles the display and navigation of Java files within the project directory.
2. **sourceCodeDisplayUI.java**: Displays the source code of selected Java files.
3. **targetSelectionUI.java**: Manages project and file selection, as well as metric display.
4. **MetricsTracker.java**: A singleton class that tracks project paths, files, and associated metrics.
5. **ProgramWindow.java**: The main entry point for the graphical user interface.
6. **getTokens.java**: Tokenizes Java source code for further analysis.
7. **Score.java**: Represents the scoring model for different metrics.
8. **XPEvaluator.java**: Computes various metrics such as indentation, class structure, and method structure.

## Usage
1. **Setup**: Place the Java project to be analyzed in a directory accessible by the application.
2. **Launch**: Run `ProgramWindow.java` to start the graphical user interface.
3. **Select Project**: Use the interface to navigate and select the project directory.
4. **View Metrics**: Examine the evaluated metrics for each file or for the entire project.

## Dependencies
- Java 11 or higher.
- Ensure all required libraries for UI components and token parsing are included in the project classpath.

## How It Works
1. The user selects a project directory using `targetSelectionUI`.
2. `MetricsTracker` initializes and evaluates all Java files in the project.
3. `XPEvaluator` computes metrics for each file using tokenized data.
4. Scores are displayed in a graphical format, highlighting key insights about the project's code structure.

## Contributors
This project was collaboratively developed with a focus on Java code evaluation and user interface design.

## Plugin Information
Source code: https://github.com/Tesquo/EXP_Plugin

To install locally, go to File -> Settings -> Plugins -> Install plugin from disk (Settings cog in top right) -> Select JAR file and click ok.

If JAR file is not local, clone repository above and run './gradlew clean build' followed by ./gradlew pluginJar

This will generate a new JAR file in the build/libs folder.