#! /bin/sh

echo "Creating Webstart release"

mvn assembly:assembly

cp target/flowpaint-executable.jar webstart/
cp src/main/webstart/FlowPaint.jnlp webstart/


echo "Signing Jar:s"

jarsigner -keystore FlowPaintKeystore webstart/flowpaint-executable.jar zzorn


echo "Uploading FlowPaint Webstart files to FlowPaint homepage"

scp webstart/FlowPaint.jnlp webstart/flowpaint-executable.jar zzorn_flowpaint@ssh.phx.nearlyfreespeech.net:/home/public/webstart/


