#! /bin/sh

echo "#### FlowPaint Webstart compilation and deployment script"
echo "#### This script is for deploying the webstart version to the FlowPaint homepage"
echo "#### As such it should only be run by the homepage maintainer"
echo "#### (In any case, others don't have the password to the webhost, so wouldn't work anyway)"

###############
# Start with asking the passwords, so that the process can run autonomously afterwards.
# The passwords are not passed as command line arguments, as that would leave them in the command history
# The webhost password is asked in any case at the end, as there is no error detection for the previous
# commands, and it's a bit more sensitive.

echo -n "Keystore password: "
stty -echo
read storepass
stty echo
echo ""

echo -n "Keystore user password: "
stty -echo
read keypass
stty echo
echo ""


###############
echo "#### Creating Webstart release"

mvn assembly:assembly


###############
echo "#### Copying files to webstart/"

cp target/flowpaint-executable.jar webstart/
cp src/main/webstart/FlowPaint.jnlp webstart/


###############
echo "#### Packaging native libraries"

jar cf webstart/jpen-native-linux.jar nativelibs/libjpen-2.so
jar cf webstart/jpen-native-osx.jar nativelibs/jpen-2.jnilib
jar cf webstart/jpen-native-win32.jar nativelibs/jpen-2.dll

###############
echo "#### Signing Jar:s"

jarsigner -storepass $storepass -keypass $keypass -keystore FlowPaintKeystore webstart/flowpaint-executable.jar zzorn
jarsigner -storepass $storepass -keypass $keypass -keystore FlowPaintKeystore webstart/jpen-native-linux.jar zzorn
jarsigner -storepass $storepass -keypass $keypass -keystore FlowPaintKeystore webstart/jpen-native-osx.jar zzorn
jarsigner -storepass $storepass -keypass $keypass -keystore FlowPaintKeystore webstart/jpen-native-win32.jar zzorn


###############
echo "#### Uploading FlowPaint Webstart files to FlowPaint homepage"

scp webstart/FlowPaint.jnlp webstart/flowpaint-executable.jar webstart/jpen-native-*.jar zzorn_flowpaint@ssh.phx.nearlyfreespeech.net:/home/public/webstart/


###############
echo "#### Done"

