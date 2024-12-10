# Project Overview

## Description
This project is designed to analyze and evaluate Java source code by providing metrics such as indentation, class structure, and method structure scores. The system also offers a graphical user interface to display the evaluation results and navigate the codebase effectively.

## Features
- **Metrics Evaluation**: Computes scores for indentation, class structure, and method structure using `XPEvaluator` and associated evaluators.
- **File Navigation**: Navigate through Java project directories using `fileViewerUI` and `sourceCodeDisplayUI`.
- **Graphical Interface**: A user-friendly interface for selecting projects, viewing metrics, and examining source code using `ProgramWindow` and `targetSelectionUI`.

## Project Structure

### **1. Main Classes**
- **`Main`**: Entry point for the application.
- **`ProgramWindow`**: The central GUI container for the application.

### **2. User Interface (`userInterface`)**
#### Panels (`UI_Panels`):
- **`codeMetricsUI`**: Displays overall code metrics.
- **`fileViewerUI`**: Provides a file browser for navigating Java files.
- **`sourceCodeDisplayUI`**: Displays the content of selected Java files.
- **`targetSelectionUI`**: Allows users to select projects and files, and displays metrics.

#### Widgets (`UI_Widgets`):
- **`DialPanelWidget`**: Displays metrics in a visual, dial-based format.
- **`FileSelector`**: Enables file selection within the UI.
- **`hintTextField`**: Provides hints for user input fields.
- **`ProjectSelector`**: Allows users to select a project directory.

### **3. Evaluation System (`XP_Metrics`)**
- **`XPEvaluator`**: Core evaluator for computing metrics like indentation, class structure, and method structure.
- **`Score`**: Represents the scoring model for metrics.
- **`getTokens`**: Parses and tokenizes Java source code for analysis.

### **4. Utilities (`utils`)**
- **`directoryContainsJava`**: Checks if a directory contains Java files.
- **`getFileSubtree`**: Retrieves a subtree of files from a directory.
- **`getJavaSubdirectories`**: Filters Java subdirectories.
- **`trimFrontText`**: Utility for cleaning up text formatting.

## Usage
1. **Setup**: Place the Java project to be analyzed in a directory accessible by the application.
2. **Launch**: Run `Main.java` to start the graphical user interface.
3. **Select Project**: Use the interface to navigate and select the project directory.
4. **View Metrics**: Examine the evaluated metrics for each file or for the entire project.

## Dependencies
- **Java**: Requires Java 11 or higher.
- **Libraries**: Ensure all necessary libraries for Swing and token parsing are included in the classpath.

## Modules and Responsibilities

### 1. **MetricsTracker**
- Singleton class that manages project data and provides metrics.
- Computes project-wide and file-specific metrics.

### 2. **XPEvaluator**
- Core evaluation class responsible for:
  - Indentation analysis.
  - Class structure compliance.
  - Method structure analysis.

### 3. **User Interface**
- Built using Swing for a smooth and intuitive graphical interface.
- Designed for extensibility with modular panel and widget components.

## Contributors
This project was collaboratively developed with a focus on Java code evaluation and user interface design.

## Plugin Information
Source code: https://github.com/Tesquo/EXP_Plugin

To install locally, go to File -> Settings -> Plugins -> Install plugin from disk (Settings cog in top right) -> Select JAR file and click ok.

If JAR file is not local, clone repository above and run './gradlew clean build' followed by ./gradlew pluginJar

This will generate a new JAR file in the build/libs folder.

Additionally, sometimes to have the plugin actually join in on code inspections, you will have to go to the problems tab -> project errors -> Inspect Code -> Configure inspection profile -> Scroll down and tick 'My Group'. After this the plugin should automatically join in on code inspection when a new file is opened.