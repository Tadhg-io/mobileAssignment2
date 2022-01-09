# Pintmark

This project is an assignment for the Mobile App Development Module of WIT HDip Computer Science 2020/2021.
This is an android application built using the Kotlin framework. 

## Personal Statement
This app was a continuation of my submission for Assignment 1. The purpose of this app os for a user to be able to mark their favourite spots to go for a pint. A user places a location, writes a description and categorises the entry.
I started several features on this app which I did not succeed in implementing. Some of these unfinished features can still be seen in feature branches.

![image](https://user-images.githubusercontent.com/40873652/147891005-507eaa30-03b9-4c73-b2eb-d2e20eaab39e.png)

## Application Overview
- Splash Screen
- Firebase Login
- CRUD Operations
- Images



## Integrations
- Firebase Authentication

![image](https://user-images.githubusercontent.com/40873652/147890451-8d366199-fef5-4878-b328-7f171633b810.png)

## App Architecture
- MVP Design Pattern

![image](https://user-images.githubusercontent.com/40873652/147890464-6f4841af-9667-4f19-a361-b214d485872c.png)

![class_diagram (2)](https://user-images.githubusercontent.com/40873652/147891520-f5e75429-58b3-46e0-977d-0341fdd910b5.png)


## Git Approach
For this project, I used the GitFlow workflow. I work in the develop branch for small fixes and updates and create a new branch for a new major feature. Once a feature is finished, I merge the feature branch back into the develop branch. For a release I create a new branch from develop. I create the release from this new branch and then merge this branch into master.
![class_diagram (1)](https://user-images.githubusercontent.com/40873652/147891434-feb3b588-4504-44c6-9cac-8167f6ffbac6.png)



## References
- ### Creating a Splash screen
- https://www.geeksforgeeks.org/how-to-create-a-splash-screen-in-android-using-kotlin/
- ### Firebase Login
- https://github.com/rj642/BasicLoginFirebase
- ### Google Authentication
- https://developers.google.com/identity/sign-in/android/sign-in?utm_source=studio
- ### setOnTouchListener() events
- https://www.tutorialspoint.com/how-to-handle-swipe-gestures-in-kotlin
- ### TektStyle property in layouts
- https://www.tutorialkart.com/kotlin-android/android-textview-italic/
- ### nested linear layouts
- https://stackoverflow.com/questions/14779688/put-buttons-at-bottom-of-screen-with-linearlayout
