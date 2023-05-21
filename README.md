# ReReCodEx

ReReCodEx is a reimplementation of the ReCodEx system, a web-based tool for managing courses and assignments. It is designed to provide a faster and more responsive user interface for students to easily access their courses and assignments. It is built using Java and Swing, and uses the ReCodEx API to interact with the ReCodEx system.

## Table of Contents

- [ReReCodEx](#rerecodex)
  - [Table of Contents](#table-of-contents)
  - [Overview](#overview)
  - [Features](#features)
  - [Getting Started](#getting-started)
    - [Prerequisites](#prerequisites)
    - [Downloading](#downloading)
    - [Installation From Source](#installation-from-source)
  - [Usage](#usage)
    - [1. Logging In](#1-logging-in)
    - [2. Courses View](#2-courses-view)
    - [3. Courses and Assignments](#3-courses-and-assignments)
    - [4. Assignment View](#4-assignment-view)
    - [5. Accessing Help](#5-accessing-help)
  - [Project Structure](#project-structure)
  - [Modules](#modules)
  - [File Overview](#file-overview)
    - [Models](#models)
    - [Views](#views)
    - [View Components](#view-components)
    - [Controllers](#controllers)
    - [Swing Extensions](#swing-extensions)
    - [API Helpers](#api-helpers)
    - [Resources](#resources)

## Overview

ReReCodEx is *not a comprehensive* software tool designed to facilitate the viewing of assignments and courses in the ReCodEx system. This application provides a modern and user-friendly interface allowing users to easily access their assignments, courses, and other related information.

## Features

- User authentication and account management with secure storage of sensitive data
- Overview of courses and assignments with detailed information for each item
- Integrated sidebar for easy navigation between different sections of the application
- Expandable course cards providing a compact view of assignments and their associated deadlines
- Interactive assignments panel, displaying detailed information about each assignment, including submission attempts, correctness threshold, allowed environments, and solution file restrictions
- Easily accessible local storage management to store and retrieve user data across sessions
- Localization support for multiple languages

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 17 or later
- Maven

### Downloading

1. Download the latest release (.jar) from the [Releases](https://github.com/adamsvestka/ReReCodEx/releases) page.
2. Open the application by double-clicking the downloaded file
- Alternatively, run the application from the command line:
```sh
java -jar ReReCodEx.jar
```

### Installation From Source

1. Clone the repository:
```sh
git clone git@github.com:adamsvestka/ReReCodEx.git
```

2. Run the application:
```sh
cd ReReCodEx
mvn compile exec:java
```

## Usage

ReReCodEx is a user-friendly software tool designed to help you manage your courses and assignments with ease. In this guide, we will provide an overview of the application's interface, describe its features, and explain how you can use them to get the most out of ReReCodEx.

### 1. Logging In

Upon launching ReReCodEx, the first thing you will see is the login view. Here, you need to enter your CAS credentials (username and password) to access your courses and assignments.

For added convenience, you can enable the "Remember me" option, which will securely save your login details, so you don't have to re-enter them the next time you launch ReReCodEx. To log in, click the "Log In" button. If your login credentials are valid, you will be taken to the courses view.

### 2. Courses View

The courses view is the main interface of ReReCodEx and displays a list of courses you are enrolled in along with the primary teacher. Each course is represented by a course card, which can be expanded to view more information about the course, including its individual assignments.

### 3. Courses and Assignments

To expand a course and see the assignments for that course, click on its header. Upon expansion, each assignment displayed includes the assignment name, obtained points by the student, maximum points available, and the deadline.

Clicking on an assignment will take you to the assignment view, where you can view detailed information about the assignment.

### 4. Assignment View

In the assignment view, you can see the following information about the selected assignment:
- Assignment deadline
- Maximum points obtainable
- Runtime evaluation environments
- Number of attempts made out of the maximum allowed attempts
- Maximum number of files and maximum allowed size per submission attempt

Below the header, you can view the assignment description rendered as markdown, including images, code snippets, and clickable links.

### 5. Accessing Help

If you ever need help using ReReCodEx while using the application, simply click on the "Help" tab in the sidebar. This will bring up the a help document, which provides step-by-step instructions for using ReReCodEx.

## Project Structure

ReReCodEx is designed using a modular architecture, which separates the system into different components based on their specific functions.

## Modules

- Models: Data models representing various aspects of the system, such as users, groups, and assignments.
- Views: Custom JPanel components for displaying individual pages and managing application data.
- View Components: Custom graphical components used in the application interface.
- Controllers: Classes that handle application logic and user input.
- Swing Extensions: Custom extensions of Swing components, providing additional functionality and appearance customization.
- API Helpers: Provides interfaces and implementation for interacting with the ReCodEx API.
- Resources: Static resources used in the application, such as stylesheets.

## File Overview

### Models

| File                | Summary                                                                                                                                                                                                                                                                                                                                                                                                       |
| :------------------ | :------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| Model.java          | Model is a singleton class that provides a single entry point for accessing and modifying the main data model for the application, which includes the access token, user object, and list of groups the user is a part of. It also provides utility methods for handling localized texts.                                                                                                                     |
| User.java           | This class represents a user in the ReCodEx system, providing methods to load user data from external API payloads, check if the user is logged in, and perform logout. It also extends the Observable class to enable observing changes in the user's state.                                                                                                                                                 |
| Group.java          | This class provides methods to build and manage a Group object, which can represent many different structures in ReCodEx. It contains key data attributes such as ID, name, description, students, primary administrators, and assignments. It also provides utility methods for loading data from response payload objects and handling API requests to load statistics related to the content of the group. |
| Assignment.java     | This class represents an assignment with its properties, deadlines, and other constraints. It provides methods to load data from different API response payloads and an observable mechanism to notify subscribers of any updates. It also contains a Deadline class with a deadline time and the number of points the student can gain before the deadline.                                                  |
| Observable.java     | This class provides an observable object that can be used in the model-view paradigm. It allows for subscribing and unsubscribing to callbacks that will be executed when the observable updates its state. It also provides a method for notifying all subscribed callbacks of an update.                                                                                                                    |
| ObservableList.java | This class represents an observable list, or "data" in the model-view paradigm, which notifies its observers about changes to its state. It provides methods to subscribe and unsubscribe callbacks, and to notify subscribers of changes to the list. It also overrides methods from the ArrayList class to notify subscribers of changes to the list.                                                       |

### Views

| File                 | Summary                                                                                                                                                                                                                                                                                                                                                                                                                                                       |
| :------------------- | :------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------ |
| LoginPanel.java      | This LoginPanel is a custom JPanel for the ReCodEx application that contains form elements for users to input their credentials, such as username and password fields, a remember me checkbox, and a button to submit the entered information for authentication. It also displays appropriate feedback, such as loading progress or error messages, based on the current authentication state. The panel has a rounded box appearance and custom box shadow. |
| CoursePanel.java     | This code creates a CoursePanel, a custom JPanel that displays a list of courses in a scrollable container. Each course is represented by a CourseCard, which contains relevant information about the course. The panel is updated whenever there is a change in the data from the Model instance.                                                                                                                                                            |
| AssignmentPanel.java | This package contains a custom JPanel that displays an Assignment's details in a vertically scrollable view. It consists of a status component and a description component, which are updated whenever there is a change in the Assignment data.                                                                                                                                                                                                              |
| HelpPanel.java       | This class provides a custom JPanel component that displays the help document for the ReReCodEx application. It loads the help document from the file "/docs/help.md" and renders it as markdown using the commonmark java library.                                                                                                                                                                                                                           |

### View Components

| File                       | Summary                                                                                                                                                                                                                                                                                                                                                                                                                                                                            |
| :------------------------- | :--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Button.java                | This class extends the JButton component to provide a modernized appearance with smooth hover, focus and click effects. It comes with predefined colors and a box shadow border, and automatically handles the focus, hover and active states for an appealing transition between them. It can be instantiated with the same parameters as a regular JButton, and also accepts an ActionListener to provide an onClick action.                                                     |
| InputField.java            | This class provides a custom extension of the JTextField class with a stylized, focused state appearance featuring rounded edges and a changing border color when the input field gains or loses focus. It also supports custom Insets for padding within the input field.                                                                                                                                                                                                         |
| PasswordField.java         | This PasswordField class is an extension of the JPasswordField class that provides a stylized, focused state appearance with rounded edges and a changing border color when the input field gains or loses focus. It also supports custom Insets for padding within the input field.                                                                                                                                                                                               |
| Sidebar.java               | This package contains a custom JPanel representing a sidebar with selectable buttons for different sections. It provides options to add and remove buttons with text and icons, manages button states, and provides a callback that returns the associated value of the selected button. It also includes a user card at the top of the sidebar with a profile picture, greeting and image.                                                                                        |
| SidebarButton.java         | This package contains a custom Swing JButton component designed for use in the sidebar of an application. It features a unique appearance, hover states, and active states to indicate the user's current selection in the sidebar. It supports different action events and can be used with optional icons and customizable text.                                                                                                                                                 |
| UserCard.java              | This class provides a custom JPanel component that displays the User's details, such as their username and avatar image, and contains a logout button. It automatically updates its contents whenever the user's information changes and makes use of the Singleton Model object to obtain the latest information.                                                                                                                                                                 |
| CourseCard.java            | This package contains a custom JPanel component, CourseCard, which displays information about a course, including its name, primary teacher(s), and assignments. It provides an expandable view of assignments for the course, which can be toggled via clicking on the CourseCard. It also supports additional course-related updates and actions through interaction with a Group object and a collection of User objects representing the primary admin(teacher) of the course. |
| AssignmentDescription.java | This class provides a custom graphical component to display assignment description details in a visually appealing and organized manner. It implements HyperlinkListener to provide hyperlink handling, and consists of a title label and a JEditorPane which renders HTML content using a CustomHTMLEditorKit. The assignment's body is parsed and rendered as an HTML string.                                                                                                    |
| AssignmentRow.java         | This class provides a custom JPanel component that displays relevant information about an Assignment, such as its name, points, max points, and deadline. It allows the user to click on it to navigate to the detailed AssignmentPanel view and updates the displayed information accordingly.                                                                                                                                                                                    |
| AssignmentRowHeader.java   | This JPanel displays the assignment's name, points, max points, and deadline column headers in a custom component. It is designed to be used in a table listing assignments and has a height of AssignmentRow.height.                                                                                                                                                                                                                                                              |
| AssignmentStatus.java      | This package contains a custom Swing panel component, AssignmentStatus, which displays detailed information about a specific assignment, such as its deadline, points limit, correctness threshold, allowed environments, submission attempts, and solution file restrictions. It supports updating data with a provided Assignment object and handles the layout and styling of its child components.                                                                             |

### Controllers

| File              | Summary                                                                                                                                                                                                                                                                                               |
| :---------------- | :---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| App.java          | This package contains the main application class for the ReReCodEx program, which provides a graphical user interface for managing courses and assignments. It includes methods for navigating to different panels, converting bytes to a human-readable string, and updating the user's information. |
| ReCodEx.java      | Error generating file summary.                                                                                                                                                                                                                                                                        |
| LocalStorage.java | This code provides an implementation of local storage which uses encrypted key-value pairs to securely store sensitive data. It includes methods to get, set, remove, and clear values, as well as a shutdown hook to save changes when the application exits.                                        |
| ColorPalette.java | This package contains a class that provides a set of colors used in the application, taken from the original app at https://recodex.mff.cuni.cz. It includes shades of white, gray, green, and blue.                                                                                                  |

### Swing Extensions

| File                     | Summary                                                                                                                                                                                                                                                                                                    |
| :----------------------- | :--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| BoxShadow.java           | This package provides a border that adds a shadow effect to a component, reducing its size. It takes in parameters such as offset, blur, spread, color, and corner radius to customize the shadow effect. It also has methods to set and check the visibility of the shadow effect.                        |
| RoundedBox.java          | This package provides a border that adds a shadow effect to a component, reducing its size. It uses anti-aliasing to create a smooth effect and can be set to be visible or invisible.                                                                                                                     |
| RoundedBorder.java       | This class provides a rounded border with a shadow effect for components, allowing for customization of the border color, thickness, and radius. It also provides methods to set and check the visibility of the border.                                                                                   |
| CustomHTMLEditorKit.java | This package provides a custom HTMLEditorKit that extends the default HTMLEditorKit. It loads a stylesheet from the file "/styles/assignment.css" and defines a viewfactory that resolves an issue of the default HTMLEditorKit not wrapping text in <code>&lt;pre&gt;</code> tags.                        |
| VerticalScrollPanel.java | VerticalScrollPanel is a JPanel class that implements the Scrollable interface and provides a hotfix feature to prevent JScrollPane from scrolling horizontally. It provides methods to customize scrolling preferences, including unit and block increments, viewport size, and tracking viewport height. |

### API Helpers

| File                        | Summary                                                                                                                                                                                                                                               |
| :-------------------------- | :---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| Locale.java                 | This code defines the Locale enum for the Recodex API v1, which contains two values: cs and en. It also provides methods to convert the enum values to strings and strings to enum values.                                                            |
| ILocalizedText.java         | This class provides a helper for retrieving localized texts from the API, with the locale specified.                                                                                                                                                  |
| ReCodExApiMapper.java       | This package provides a singleton class, ReCodExApiMapper, which extends the Jackson ObjectMapper and provides custom deserialization for the ReCodEx API. It has a private constructor and a getInstance() method to return the singleton instance.  |
| ReCodExApiException.java    | This package provides an exception class for errors that occur during the interaction with the ReCodEx API. It extends the RuntimeException and contains information about the error, such as the HTTP status code, error code, and error message.    |
| ReCodExApiDeserializer.java | This package provides a custom deserializer for the ReCodEx API that checks for the 'success' field in the JSON response and either deserializes the payload into the specified type or raises a ReCodExApiException if the 'success' field is false. |

### Resources

| File           | Summary                                                                                                                                                                             |
| :------------- | :---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------- |
| assignment.css | This code applies various styling properties to different elements, including text color, background color, padding, border radius, border, and link color with no text decoration. |
