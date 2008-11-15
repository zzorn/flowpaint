#! /bin/sh

echo "Creating Keystore"

keytool -genkey -keystore FlowPaintKeystore -alias zzorn

mkdir webstart

