# UIa - Graphical User Interface framework for Java.
UIa is a cross-platform and cross-library framework for Java. 
It is designed to make it easy to create the graphical aspects of an application.

## Architecture overview
UIa is built on top of a native graphics library or a third-party graphical framework. 
To achieve the cross-platform/library goal, the UIa core has been designed to be highly adaptive.
<br>
Currently, its core is structured as follows:

![Screenshot](docs/UIa-UML.jpeg)

Two interfaces are the fundamentals of UIa:
<ul>
  <li>Context: is the framework basement and has the responsibility to manage the window frame and handle a single View;</li>
  <li>View: is the basement for every widget created with UIa.</li>
</ul>

Migrating a project between two platforms is easy, all you need to do is implement the right Context 
for the target platform and use it as application basement. A desktop Context implementation, based on the Java AWT, 
has been already provided. If you have other needs, try experimenting with creating your own custom Context implementation.

## Example
The following part shows a simple Hello World application made with UIa. 
It displays a button on the left and, when the user clicks on it, a simple popup appears on the right. 
To hide the popup, simply click on the button.

https://github.com/JDev00/UIa/blob/00abce0875d12e889b5123430c9f1a8fa44df747/src/example/HelloWorld.java