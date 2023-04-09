#!/bin/bash

APP_MODEL_KEYS=pgn-cle-app
SCRIPT_NAME=design-export.sh

if [ "$1" = "publish" ]; then
  sh $SCRIPT_NAME dev $APP_MODEL_KEYS publish
  if [ ! "$?" -eq "0" ]; then
    echo "Backup script failed for $APP_MODEL_KEYS"
    exit 1
  fi
else
  sh $SCRIPT_NAME dev $APP_MODEL_KEYS
  if [ ! "$?" -eq "0" ]; then
    echo "Backup script failed for $APP_MODEL_KEYS"
    exit 1
  fi
fi
