#! /bin/sh

###########################################################################
# This is a shell script for generating a package for a release for a java project

# Application specific variables:
version=`cat VERSION`
revision=`svnversion -n`
releaseDate=`date +"%d %B %Y"`
userReadableName=Flowpaint
appName=flowpaint
binDirName=$appName-$version-bin
srcDirName=$appName-$version-src
webstartDirName=$appName-$version-webstart
binaryPackageName=$binDirName.zip
sourcePackageName=$srcDirName.zip
webstartPackageName=$webstartDirName.zip
googleCodeProjectId=flowpaint
compiledJar=target/flowpaint-executable.jar
jnlpFileLocation=src/main/webstart/FlowPaint.jnlp
issueReleasePrefix=Release-
keystoreFile=$appName-keystore
keystoreUser=$appName
appProperties=src/main/resources/application.properties
homepage=www.flowpaint.org
bugReportUrl=http://code.google.com/p/flowpaint/issues/entry?template=Defect%20report%20from%20user
featureRequestUrl=http://code.google.com/p/flowpaint/issues/entry?template=Feature%20request%20from%20user
oneLineDescription=`cat short-description.txt`
credits="Programmed by Hans H&auml;ggstr&ouml;m ( zzorn @ iki.fi )"
license="GPL v2"




echo "##### $userReadableName package creation script"
echo "##### Creating release packages for $userReadableName version $version, subversion revision $revision, release date $releaseDate"
echo "##### This script creates binary, source, and webstart packages for a release"
echo "##### It doesn't deploy them, but requires the passwords to the keystore used to sign the webstart files."

###########################################################################
# Start with asking the passwords, so that the process can run autonomously afterwards.
# The passwords are not passed as command line arguments, as that would leave them in the command history

echo -n "Keystore (and user $keystoreUser's) password for $keystoreFile: "
stty -echo
read storepass
stty echo
echo ""

# Assume same password for keystore user
keypass=$storepass
#echo -n "Keystore user password for user $keystoreUser: "
#stty -echo
#read keypass
#stty echo
#echo ""


###########################################################################
echo "#### Updating application.properties"
./set-property.py -q -f $appProperties -p "version" -v "$version"
./set-property.py -q -f $appProperties -p "repositoryVersion" -v "$revision"
./set-property.py -q -f $appProperties -p "releaseDate" -v "$releaseDate"
./set-property.py -q -f $appProperties -p "homepage" -v "$homepage"
./set-property.py -q -f $appProperties -p "bugReportUrl" -v "$bugReportUrl"
./set-property.py -q -f $appProperties -p "featureRequestUrl" -v "$featureRequestUrl"
./set-property.py -q -f $appProperties -p "applicationName" -v "$userReadableName"
./set-property.py -q -f $appProperties -p "oneLineDescription" -v "$oneLineDescription"
./set-property.py -q -f $appProperties -p "credits" -v "$credits"
./set-property.py -q -f $appProperties -p "license" -v "$license"

###########################################################################
echo "#### Compiling"

mvn clean
mvn assembly:assembly

# TODO: Check exit status of compile - what's the idiom for that in shell script?

###########################################################################
echo "#### Generating changes.txt from completed issues"
./change-lister.py --quiet --name $userReadableName --project $googleCodeProjectId --output changes.txt --release $version --temp temporary-issue-list.csv --prefix $issueReleasePrefix
rm temporary-issue-list.csv

###########################################################################
echo "#### Downloading and generating readme.txt"
echo "### Generate title, version and release date information for readme.txt"
echo "= $userReadableName =" > readme.txt
echo "" >> readme.txt
echo "Documentation for $userReadableName version $version, released $releaseDate." >> readme.txt
echo "### Downloading bulk of documentation for readme.txt from the wiki"
# Append everything from the third line on (remove labels and original title)
rm Documentation.wiki
wget -q http://flowpaint.googlecode.com/svn/wiki/Documentation.wiki
tail Documentation.wiki -n +3 >> readme.txt
#rm Documentation.wiki


###########################################################################
echo "#### Building source package"
echo "### Removing old package if found"
rm $sourcePackageName

echo "### Removing old source package build directory"
rm -r $srcDirName

echo "### Exporting sources"
svn export . $srcDirName

echo "### Adding generated files to sources"
cp readme.txt $srcDirName/
cp changes.txt $srcDirName/

echo "### Zipping source package up"
zip -r $sourcePackageName $srcDirName


###########################################################################
echo "#### Building binary package"
echo "### Removing old package if found"
rm $binaryPackageName

echo "### Removing old binary package build directory"
rm -r $binDirName

echo "### Making directory to collect the release contents to"
mkdir $binDirName
mkdir $binDirName/nativelibs

echo "### Collecting release contents"
cp $compiledJar $binDirName/$appName.jar
cp $appName.sh $binDirName/
cp $appName.bat $binDirName/
cp readme.txt $binDirName/
cp changes.txt $binDirName/
cp license.txt $binDirName/
cp nativelibs/*.dll $binDirName/nativelibs/
cp nativelibs/*.so $binDirName/nativelibs/
cp nativelibs/*.jnilib $binDirName/nativelibs/

echo "### Setting shell script permissions in package"
chmod u+x $binDirName/$appName.sh

echo "### Zipping binary package up"
zip -r $binaryPackageName $binDirName


###########################################################################
echo "#### Building webstart package"
echo "### Removing old package if found"
rm $webstartPackageName

echo "### Removing old webstart package build directory"
rm -r $webstartDirName

echo "### Making directory to collect the webstart contents to"
mkdir $webstartDirName

echo "#### Copying files to webstart dir"
cp $jnlpFileLocation $webstartDirName/$appName.jnlp
cp $compiledJar $webstartDirName/$appName.jar

echo "#### Packaging native libraries"
cd nativelibs
jar cf ../$webstartDirName/native-linux.jar *.so
jar cf ../$webstartDirName/native-osx.jar *.jnilib
jar cf ../$webstartDirName/native-win32.jar *.dll
cd ..

echo "#### Signing Jar:s"
jarsigner -storepass $storepass -keypass $keypass -keystore $keystoreFile $webstartDirName/$appName.jar $keystoreUser
jarsigner -storepass $storepass -keypass $keypass -keystore $keystoreFile $webstartDirName/native-linux.jar $keystoreUser
jarsigner -storepass $storepass -keypass $keypass -keystore $keystoreFile $webstartDirName/native-osx.jar $keystoreUser
jarsigner -storepass $storepass -keypass $keypass -keystore $keystoreFile $webstartDirName/native-win32.jar $keystoreUser

echo "### Zipping webstart package up (for easier transport to the target server)"
# Don't include the directory in the webstart package, as webstart is deployed by replacing the current content
cd $webstartDirName
zip -r ../$webstartPackageName ./*
cd ..

###########################################################################
echo "##### Done"

