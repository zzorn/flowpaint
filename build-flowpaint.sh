echo "#### Building flowpaint executable jar"

mvn clean
mvn assembly:assembly

echo "#### Building done.  Assuming there are no errors, you can run FLowPaint with ./flowpaint.sh"

