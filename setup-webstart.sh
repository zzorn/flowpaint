#! /bin/sh

echo "Creating Keystore"

keytool -genkey -dname "cn=Flowpaint, ou=Flowpaint, o=Flowpaint, c=N/A" -keystore "flowpaint-keystore" -alias flowpaint -validity 9001


