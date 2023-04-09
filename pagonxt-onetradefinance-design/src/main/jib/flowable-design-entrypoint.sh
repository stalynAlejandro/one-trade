#!/bin/sh

# Estructura de directorios:
# app
#   classes (d)
#   libs (d)
#   resources (d)
#   jib-classpath-file (f)
#   jib-main-class-file (f)

exec java ${JAVA_OPTS} -cp @/app/jib-classpath-file @/app/jib-main-class-file ${@}

#exec java ${JAVA_OPTS} \
#  -cp "/app:/app/classes:/app/libs/*:/app/resources/*" \
#  com.pagonxt.onetradefinance.design.PagonxtOneTradeFinanceDesignApplication \
#  ${@}
