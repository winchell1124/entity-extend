#!/bin/bash
display_help() {
    echo "Usage: $0 [option...]" >&2
    echo
    echo "   -h, --help             Print help message"
    echo "   -e, --executors        Number of executors used in spark"
    echo "   -c, --config           spark config"
    echo "   -t, --type             main class"
    echo "   -d, --date             handle date"
    echo
}

run() {
     >&2 echo "$@"
    eval "$@"
}

while [[ $# -ge 1 ]]
do
key="$1"
case $key in
    -h|--help)
        display_help
        exit 0
        ;;
    -j|--jar)
    JAR_PATH=$(readlink -m $2)
    shift
    ;;
    -e|--executors)
    EXECUTORS="$2"
    shift
    ;;
    -c|--config)
    CONFIG="$2"
    shift
    ;;
    -t|--type)
    TYPE="$2"
    shift
    ;;
    -d|--date)
    DATE="$2"
    shift
    ;;
    *)
    shift # unknown option
    ;;
esac
shift # past argument or value
done

if [ -z "$TYPE" ]; then
    TYPE="Process"
fi

CURRENT_DIR="$( cd "$( dirname "${BASH_SOURCE[0]}" )" && pwd )"
LIB_DIR="$CURRENT_DIR/lib"
JAR_PATH=`find $CURRENT_DIR -maxdepth 1 -name "entity-extend-*.jar"`

if [ -z "$EXECUTORS" ]; then
    EXECUTORS="5"
fi

if [ ! -f "$JAR_PATH" ]; then
    >&2 echo "$JAR_PATH not exist!"
    exit 1
fi


LIBS=""
for jar in `find -L $LIB_DIR -name "*.jar"`; do
    LIBS="$LIBS$jar,"
done

if [ "x${LIBS: -1}" = "x," ]; then
    LIBS=${LIBS::${#LIBS}-1}
fi

PROS=""
for filename in `find -L config -name "*.properties" -maxdepth 1`; do
    PROS="$PROS$filename,"
done
for inputfile in `find -L input -name "*.xls" -maxdepth 1`; do
    PROS="$PROS$inputfile"
done

LOG_FILE="spark-`date +"%Y%m%d%H%M%S"`.log"
NAME="etl-$TYPE-$DATE"

run "(spark-submit \
--name $NAME \
--master yarn \
--deploy-mode cluster \
--executor-memory 1G \
--num-executors $EXECUTORS \
--executor-cores 2 \
--driver-java-options \"-XX:+UseConcMarkSweepGC -Dlog4j.configuration=log4j.properties\" \
--conf \"spark.executor.extraJavaOptions=-XX:+UseConcMarkSweepGC -Dlog4j.configuration=log4j.properties\" \
--conf spark.app.name=$NAME \
$CONFIG \
--conf spark.yarn.driver.memoryOverhead=512 \
--conf spark.yarn.executor.memoryOverhead=2048 \
--files $PROS  \
--class process.$TYPE \
--jars $LIBS $JAR_PATH)"


