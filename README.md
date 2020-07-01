
## Application Overview

<table>
  <tr>
    <td>Search Screen</td>
     <td>Search result screen</td>
  </tr>
  <tr>
    <td><img src="/screenshots/Screenshot_1593556476.png" width=270 height=480></td>
    <td><img src="/screenshots/Screenshot_1593556497.png" width=270 height=480></td>
    
  </tr>
  <tr>
     <td>Detail screen</td>
     <td>App home screen widget</td>
  </tr>
  <tr>
  	<td><img src="/screenshots/Screenshot_1593556516.png" width=270 height=480></td>
     <td><img src="/screenshots/Screenshot_1593557710.png" width=270 height=480></td>
  </tr>
 </table>


## Project Overview
Explorer is an android application that lets users search through the NASA image directory. NASA has developed a rich archive of images from its few decades of space research and exploration. Users can search by a keyword or by year or by the combination of both.
Search results are shown in a recycler view. Users can select a result and check details, can navigate to the next or previous result item from the details page. The application comes with a home screen app widget which shows seven different images for seven days of the week. Image URLs are saved beforehand to avoid any copyright violations of the API. This application was built as the final project of the Udacity android nanodegree program. The main objectives were  - a student would develop the project idea, find appropriate API to work with, design UIs, prepare a specification requirement analysis document, implement the code.  In the first stage of the project, I designed and planned the app. In the second stage, I implemented the app.

## Requirements

### Design and planning phase
- The proposal contains an overview description, intended users, user interface mockups
- Proposal declares the application's primary features, draws a plan for how features will be implemented via a set of well structured technical tasks
- The proposal outlines any key constraints such as data persistence, UX corner cases, and libraries used, clearly outlines how a  database will be implemented.

### Development phase
- The app integrates third-party library
- The app validates all input from servers and users. The application does not crash for wrong format data or no data
- The application should include support for accessibility
- All strings should be kept in a strings.xml file and enable RTL layout switching on all layouts.
- App provides a widget to provide relevant information to the user on the home screen.
- Two or more Google Services should be integrated (Added AdMob and Google analytics)
- The app builds and deploys using the installRelease Gradle task.
- It is equipped with a signing configuration, and the keystore and passwords are included in the repository. Keystore is referred to by a relative path.
- All app dependencies are managed by Gradle.
- App stores data locally using Room OR Firebase Realtime Database. If Room is used then LiveData and ViewModel are used when required and no unnecessary calls to the database are made.

## Why this Project?
To become a proficient Android Developer, you need to design apps and make plans for how to implement them. This will involve choices such as how you will store data, how you will display data to the user, and what functionality to include in the app.
In the second phase, you will build the app and have an application ready which you can submit to Play Store for Google distribution.

## Learning objectives
- demonstrate the ability to communicate an app idea formally, using: An app description, UI flow mocks, a list of required tasks that you will complete to build the app in phase 1
- In phase 2, you will build the app. At the end of the project, you will gain the experience you need to own the full development cycle of an android application.


