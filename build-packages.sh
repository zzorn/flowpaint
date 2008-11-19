echo "##### Creating release packages"
echo "### Compiling"

mvn clean
mvn assembly:assembly

echo "### Zipping things up"
zip flowpaint-`cat VERSION`-bin.zip readme.txt changes.txt flowpaint.sh flowpaint.bat target/flowpaint-executable.jar nativelibs/* 

echo "### Done"

