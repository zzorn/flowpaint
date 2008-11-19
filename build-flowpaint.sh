echo "#### Building flowpaint executable jar"

mvn clean
mvn assembly:assembly

echo "#### Building done.  Assuming there were no errors, you can run FLowPaint with ./flowpaint.sh"

