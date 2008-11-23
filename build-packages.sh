#! /bin/sh

# This is a shell script for generating a package for a release for a java project

# TODO: Add java webstart generation here too.

# Application specific variables:
version=`cat VERSION`
userReadableName=Flowpaint
appName=flowpaint
binDirName=$appName-$version-bin
srcDirName=$appName-$version-src
binaryPackageName=$binDirName.zip
sourcePackageName=$srcDirName.zip
googleCodeProjectId=flowpaint
compiledJar=target/flowpaint-executable.jar
issueReleasePrefix=Release-


echo "##### Creating release packages for $userReadableName version $version"
echo "#### Compiling"

mvn clean
mvn assembly:assembly

# TODO: Check exit status of compile - what's the idiom for that in shell script?

echo "#### Generating changes.txt from completed issues"
./change-lister.py --quiet --name $userReadableName --project $googleCodeProjectId --output changes.txt --release $version --temp temporary-issue-list.csv --prefix $issueReleasePrefix
rm temporary-issue-list.csv


echo "#### Building source package"
echo "### Removing old package if found"
rm $sourcePackageName

echo "### Removing old source package build directory"
rm -r $srcDirName

echo "### Exporting sources"
svn export . $srcDirName

echo "### Zipping source package up"
zip -r $sourcePackageName $srcDirName


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

echo "##### Done"

