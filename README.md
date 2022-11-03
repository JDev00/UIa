# UIa Pre-release 1.0
Graphical User Interface API for Java.
Key points:
1) cross-platform;
2) cross-implementation (AWT, SWING, OPENGL, ecc);
3) easy to use.

Warning!
Project under construction.

# Brief description:

UIa is logically structured into 4 layers (starting from the most platform-dependent one):
1) Context, responsible for rendering and handling Pages;
2) Page,    responsible for rendering and handling Views;
3) View,    responsible to handle events and to provide a common ground to Widgets;
4) Widget,  a custom View with unique behaviour.

More information can be found inside every file.
