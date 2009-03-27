#! /bin/sh

echo #### Starting FlowPaint

java -Djava.library.path=nativelibs -Xms32m -Xmx256m -XX:MaxPermSize=128m -jar flowpaint.jar
