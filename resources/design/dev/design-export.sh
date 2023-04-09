#!/bin/bash

ENV=${1:-dev}

function prop() {
  grep "${1}" "${ENV}".properties | cut -d'=' -f2
}

if test $# -lt 2; then
  echo "Usage: dev-export.sh ENVIRONMENT APP_KEYS"
  echo "ENVIRONMENT -> the environment where you want to export from (affects which properties file is used). E.g. dev"
  echo "APP_KEYS -> Comma separated list of app-keys to export. E.g. my-app"
  echo "publish -> Optional parameter to specify that you also want to publish to localhost"

else
  echo "----------------------------------------------"
  echo "Reading properties file = \"$1.properties\""
  APP_MODEL_KEYS=$2
  echo "APP_MODEL_KEYS = $APP_MODEL_KEYS"
  echo "=============================================="
  echo "Using the following properties:"
  DESIGN_URL=$(prop 'DESIGN_URL')
  DESIGN_USER=$(prop 'DESIGN_USER')
  DESIGN_PASSWORD=$(prop 'DESIGN_PASSWORD')
  CUSTOM_FOLDER=$(prop 'CUSTOM_FOLDER')
  CUSTOM_SUFFIX=$(prop 'CUSTOM_SUFFIX')
  DESIGN_FOLDER=$(prop 'DESIGN_FOLDER')
  DESIGN_SUFFIX=$(prop 'DESIGN_SUFFIX')
  WORK_URL=$(prop 'WORK_URL')
  WORK_USER=$(prop 'WORK_USER')
  WORK_PASSWORD=$(prop 'WORK_PASSWORD')
  echo "DESIGN_URL = $DESIGN_URL"
  echo "DESIGN_USER = $DESIGN_USER"
  echo "DESIGN_PASSWORD = $DESIGN_PASSWORD"
  echo "CUSTOM_FOLDER = $CUSTOM_FOLDER"
  echo "CUSTOM_SUFFIX = $CUSTOM_SUFFIX"
  echo "DESIGN_FOLDER = $DESIGN_FOLDER"
  echo "DESIGN_SUFFIX = $DESIGN_SUFFIX"
  echo "WORK_URL = $WORK_URL"
  echo "WORK_USER = $WORK_USER"
  echo "WORK_PASSWORD = $WORK_PASSWORD"
  echo "----------------------------------------------"

  for appModelKey in $(echo "$APP_MODEL_KEYS" | sed "s/,/ /g"); do
    status_code=$(curl --head --write-out %{http_code} --silent --output /dev/null --user "$DESIGN_USER":"$DESIGN_PASSWORD" "$DESIGN_URL/api/editor/app-models/key/$appModelKey/export")
    if [[ "$status_code" -ne 200 ]]; then
      printf "\nWARNING: There is no app with key = %s \n\n" "$appModelKey"
      exit 1
    else
      echo ""
      echo "----------------------------------------------"
      printf "Processing appModelKey = %s\n" "$appModelKey"
      echo "=============================================="
      printf "Downloading design zip \n"
      curl --user "$DESIGN_USER":"$DESIGN_PASSWORD" "$DESIGN_URL/api/editor/app-models/key/$appModelKey/export" --output "$DESIGN_FOLDER"/"$appModelKey""$DESIGN_SUFFIX".zip --fail-with-body
      if [ ! "$?" -eq "0" ]; then
        exit 1
      fi
      printf "Downloading runtime zip \n"
      # shellcheck disable=SC2086
      curl --user $DESIGN_USER:"$DESIGN_PASSWORD" "$DESIGN_URL/api/editor/app-models/key/$appModelKey/export?type=runtime" --output $CUSTOM_FOLDER/"$appModelKey""$CUSTOM_SUFFIX".zip --fail-with-body
      if [ ! "$?" -eq "0" ]; then
        exit 1
      fi
      if test $# -eq 3 && [ "$3" = "publish" ]; then
        echo "Publishing to $WORK_URL"
        file=$CUSTOM_FOLDER/$appModelKey$CUSTOM_SUFFIX.zip
        curl --silent -o /dev/null -f --user "$WORK_USER":"$WORK_PASSWORD" -X POST "$WORK_URL/app-api/app-repository/deployments" -H "accept: application/json" -H "Content-Type: multipart/form-data" -F "file=@$file"
        if [ $? -eq 0 ]; then
          echo "Successfully published to $WORK_URL"
        else
          echo "Error while publishing to $WORK_URL"
        fi
      fi
    fi
  done
fi
