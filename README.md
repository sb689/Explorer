
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
Explorer is an android application that allows users to search through the NASA image directory. NASA has developed a rich archive of images from its few decades of space research and exploration. Users can search by a keyword or by year or by the combination of both. Users can select a search result from the list and check details, can navigate to the next or previous result item from the details page. The application comes with a home screen app widget which shows seven different images for seven days of the week. Image URLs are saved beforehand to avoid any copyright violations of the API. 

This application was built as the final project of the Udacity android nanodegree program. As part of the requirements, I worked through every part of app development phases like idea generation, requirement analysis & design, specification document preparation (with mockups), app development, and testing. 

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
- It is equipped with a signing configuration.
- All app dependencies are managed by Gradle.
- App stores data locally using Room OR Firebase Realtime Database. If Room is used then LiveData and ViewModel are used when required and no unnecessary calls to the database are made.

## App Features
- Uses retrofit to download data from NASA API; 
- Uses Picasso to display images;
- Implements ViewModels and LiveData to share data among fragments; 
- Implements intent service and work manager to download and update the widget image periodically; 
- Provides search suggestions based on previous search history using Room, ViewModel, and LiveData 
- Implements Google Analytics to track search keywords. 





