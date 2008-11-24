echo "#### Building flowpaint executable jar"

mvn clean
mvn assembly:assembly

echo "#### Moving and renaming produced jar"
cp target/flowpaint-executable.jar flowpaint.jar

echo "#### Building done.  Assuming there were no errors, you can run FlowPaint with ./flowpaint.sh"

