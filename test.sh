## test
./gradlew clean test jacocoTestReport

WEB_BROWSER=firefox
DIR=`pwd`
REPORT_DIR="$DIR/build/jacocoHtml"
REPORT_URL="$REPORT_DIR/index.html"

echo $REPORT_DIR

echo "open test coverage report URL in WebBrowser "
echo "$WEB_BROWSER  $REPORT_URL"
$WEB_BROWSER  $REPORT_URL

