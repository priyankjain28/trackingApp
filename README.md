# Parent Child Tracker - MVVM, Firebase, Databinding, Google Map
A prototype application for child safety that will work in dual modes - Parent and Child.
Parent mode will display his child’s locations and Child mode will automatically track his geolocations and send them to associated parent .

## Features
* User able to create a parent account/child account and log in. 
* When logged in, a parent account can see a list of his children and add more.
* Parent account can select any of his connected child
* When logged in, a user can see a list of places (or locations) he visited and checked in, also he should be able to CRUD them.
* A checking-in place have a name (typed by user), a place category (i.e. ‘home’ / ‘work’ / ‘food’ etc, selected from a multiple choice list) a date and geolocation coordinates (automatically calculated by the app).

## Technical Feature
This repository contains a tracking app that implements MVVM architecture using Databinding, Firebase Database, LiveData, Google Maps, Glide

## The app has following packages:
* data: It contains all the data accessing and manipulating components.
* ui: View classes along with their corresponding ViewModel.
* init: Initialize instances and maintain user session
* util: Utility classes.

##### Classes have been designed in such a way that it could be inherited and maximize the code reuse.

## Library Reference:
* JetPack Architecture 
* Firebase
* Google Maps
* Glide

## Special Feature
* Splash Screen - Handled User Session
* Login/Signup  - Used TextInputLayout to more interactive also handled validation
* Parent Screen - Used Map to locate each and every child added, add child, display added child with last login and also you can delete child.
* Child Screen  - Add event, used static map Api to display the given location with the help of Glide, Card view to make more interactive, click on card view to update the existing event  
* Logout        - Logout with the user session


## Screenshot
![alt text][login] ![alt text][signup]
![alt text][eventCapture] ![alt text][childTrack]

[login]: https://github.com/priyankjain28/trackingApp/blob/master/screenshot/login.jpeg "Login Screen"
[Signup]: https://github.com/priyankjain28/trackingApp/blob/master/screenshot/signup.jpeg "Signup Screen"
[eventCapture]: https://github.com/priyankjain28/trackingApp/blob/master/screenshot/events.jpeg "Event Screen"
[childTrack]: https://github.com/priyankjain28/trackingApp/blob/master/screenshot/mapView.jpeg "Child Tracker"


  
