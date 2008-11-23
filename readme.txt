= FlowPaint =

Documentation for FlowPaint version 0.1, released in November 2008


== About ==

Flowpaint aims to be an intuitive and powerful next generation paint program.

This is the first release, so it lacks many common features (like Undo), 
and has some rendering artefacts, but it can be used for light sketching.


== Features ==

This first version of FlowPaint contains the following major features:

  * A procedural brush system with a set of hand-crafted brushes.
  * A quick-save feature that allows uninterrupted sketching. 

See changes.txt for a full list of implemented features and fixed bugs, 
and for the known open bugs.


== License ==

FlowPaint is licensed under GNU GPL v2, see the license.txt file for details.


== Disclaimer ==

Use at your own risk.  The authors take no responsibility of use or misuse of this software.


== Download ==

The latest version of FlowPaint can be downloaded from http://www.flowpaint.org


== Installation == 

Flowpaint can be installed either with JavaWebstart, or from a zipped package.

=== Java WebStart ===

Simply click on the Webstart download button on the FlowPaint homepage (www.flowpaint.org).
If you have Java WebStart installed (A normal Java installation usually includes it), then
your webbrower should ask if you want to use java to open the webstart file (jnlp file).
After answering yes, the webstart package is downloaded and run.  Java will ask you to accept the
certificate for FlowPaint (signed by zzorn), click Accept / Run to start FlowPaint. 

=== Zipped Package ===

If WebStart didn't work, you can install FlowPaint from a zipped package.

You can find the package download link on the flowpaint homepage (www.flowpaint.org)
Move the package to a suitable location (e.g. you home directory or My Documents). 
Unzip it, by right clicking on it and selecting 'Extract Here'  (or similar - depends on 
you installed unzip software), or with  unzip flowpaint-0.1.0  on unix systems.

It should extract a directory named flowpaint-0.1.0 (or similar, depending on the version).

In Windows, change to that directory and double-click flowpaint.bat.  Flowpaint should start.

In unix systems, cd to the directory, and make sure flowpaint.sh is executable (chmod u+x flowpaint.sh),
then run flowpaint with the command ./flowpaint.sh


== Troubleshooting ==

If FlowPaint fails to start, or crashes, copy any error messages and email them to zzorn at iki.fi.
You can also easily file a bug report yourself through the google code page if you have a 
GMail account or other google account at: http://code.google.com/p/flowpaint/issues/entry

Tablet support is known to be flaky on at least linux systems.  
Sometimes it may work if the zipped package is used instead of WebStart.

The developers also hang out on #flowpaint on irc.freenode.net, you can drop in there to ask questions
(although be prepared to wait a while, or leave a contact email, we are not always reading irc).


== Usage ==

Along the right side of the screen there are buttons for selectin the current brush.  
Just select a brush, press and hold the left mouse button over the canvas, and move the mouse to draw.
FlowPaint also works with graphics tablets (although on some systems it may not detect the pressure).

The brush textures are currently using the speed of the stroke as input - when you draw faster, 
the textures are more stretched, and when you draw slower, they are more dense.

In the toolbar there are three buttons.  The 'Quicksave' -button saves your current picture in a file under
your current directory (it shows the path in the statusbar after the save).  If you use the webstart version,
this is usually your home directory (on Windows this is under documents and settings / you name, not in My Documents).
If you used the zipped distribution, the save location is usually the directory that you started the application from.

The 'Clear Picture' -button empties the whole canvas.  Note that there is no Undo, so be careful!

The 'Quicksave and Clear Picture' -button simply combines both of these - saves your existing picture, 
and clears the canvas, allowing you to directly start on the next one.


== Contact info ==

Home Page:      www.flowpaint.org
IRC Channel:    #flowpaint on irc.freenode.net
Mailing list:   http://groups.google.com/group/flowpaint
Lead developer: Hans Häggström (zzorn at iki.fi)






