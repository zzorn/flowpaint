#! /bin/sh

# This is a shell script for generating a package for a release for a java project

# TODO: Add java webstart generation here too.

# Application specific variables:
version=`cat VERSION`
userReadableName=FlowPaint
appName=flowpaint
dirName=$appName-$version
packageName=$dirName-bin.zip
googleCodeProjectId=flowpaint
compiledJar=target/flowpaint-executable.jar
issueReleasePrefix=Release-


echo "##### Creating release packages for $userReadableName version $version"
echo "### Compiling"

mvn clean
mvn assembly:assembly

# TODO: Check exit status of compile - what's the idiom for that in shell script?

echo "### Generating changes.txt from completed issues"
./change-lister.py --quiet --name $userReadableName --project $googleCodeProjectId --output changes.txt --release $version --temp temporary-issue-list.csv --prefix $issueReleasePrefix
rm temporary-issue-list.csv

echo "### Removing old package if found"
rm $packageName

echo "### Removing old versions"
rm $dirName/readme.txt
rm $dirName/changes.txt
rm $dirName/license.txt
rm $dirName/$appName.sh
rm $dirName/$appName.bat
rm $dirName/$appName.jar
rm $dirName/nativelibs/*

echo "### Making directory to collect the release contents to"
mkdir $dirName
mkdir $dirName/nativelibs

echo "### Collecting release contents"
cp $compiledJar $dirName/$appName.jar
cp $appName.sh $dirName/
cp $appName.bat $dirName/
cp readme.txt $dirName/
cp changes.txt $dirName/
cp license.txt $dirName/
cp nativelibs/*.dll $dirName/nativelibs/
cp nativelibs/*.so $dirName/nativelibs/
cp nativelibs/*.jnilib $dirName/nativelibs/

echo "### Setting shell script permissions in package"
chmod u+x $dirName/$appName.sh

echo "### Zipping things up"
zip -r $packageName $dirName

echo "### Done"

