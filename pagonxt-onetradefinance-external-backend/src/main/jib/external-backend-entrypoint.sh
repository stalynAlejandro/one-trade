#!/bin/sh

# Directory structure:
# app (d)
#   classes (d)
#     com (d)
#     static (d)
#   jib-classpath-file (f)
#   jib-main-class-file (f)
#   libs (d)
#     ... (f)
#     pagonxt-frontend-0.0.1-SNAPSHOT.jar (f)
#     pagonxt-onetradefinance-integrations-0.0.1-SNAPSHOT.jar (f)
#     ... (f)
#   resources (d)
#     application-oauth.properties (f)
#     application.properties (f)
#     com (d)
#     root (d)
# external-backend-entrypoint.sh (f)
# ... (d)

exec java ${JAVA_OPTS} -cp @/app/jib-classpath-file @/app/jib-main-class-file ${@}
