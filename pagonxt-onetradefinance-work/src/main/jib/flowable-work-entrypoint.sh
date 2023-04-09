#!/bin/sh

exec java ${JAVA_OPTS} -cp @/app/jib-classpath-file @/app/jib-main-class-file ${@}
