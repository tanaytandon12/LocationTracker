The Android application to track the location of the user.


### Requirements ###

* Android Studio 2.3 or higher
* Git
* Java 8

### How do I get set up? ###

* Create a project directory at appropriate location.
* Open terminal and cd to project directory 
* In terminal write **git clone git@github.com:tanaytandon12/LocationTracker.git**
* Open Android Studio
* Select Open an existing Android Studio Project
* Navigate to your project and select OK

### Project Architecture ###

* The project is modelled on the Model-View-Presenter (MVP) pattern.
* **ui** is the view and presenter
* **model** & **data** is the  model
* [Greendao](http://greenrobot.org/greendao/) is used for offline storage
* The parent class for Activity, Fragment are **BaseActivity**, **BaseFragment**.


### Who do I talk to? ###

* tanay1400089@gmail.com