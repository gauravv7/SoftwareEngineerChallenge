if type -p java; then
    echo found java executable in PATH
    _java=java
elif [[ -n "$JAVA_HOME" ]] && [[ -x "$JAVA_HOME/bin/java" ]];  then
    echo found java executable in JAVA_HOME
    _java="$JAVA_HOME/bin/java"
else
    echo "no java found, please install to run this project"
    exit 1
fi

if [[ "$_java" ]]; then
    version=$("$_java" -version 2>&1 | awk -F '"' '/version/ {print $2}')
    echo version "$version"
    # shellcheck disable=SC2072
    if [[ "$version" > "1.7" ]]; then
        echo version is more than 1.7
        echo Running project...
        cd paybaymax;
        mvn clean test;
    else
        echo version is less than 1.5, expecting atleast 1.7
    fi
fi