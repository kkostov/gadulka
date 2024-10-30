#!/bin/bash

# Sets and commits the version coordinate of the library based on the provided input.
# The script expects the version to be a valid semversion e.g. 1.0.0


# Check if two arguments are passed
if [ -z "$1" ] || [ -z "$2" ]; then
  echo "No input provided. Usage: ./setversion.sh <semver> file/to/update"
  exit 1
fi

# Assign the first argument to a variable
NEW_VERSION="$1"
FILE_PATH="$2"

echo "Updating veresion to '${NEW_VERSION}' in '${FILE_PATH}'."

if [[ "$OSTYPE" == "darwin"* ]]; then
  # macOS requires an empty string argument after -i to edit in place without backup
  sed -i '' "s/^version = .*$/version = \"${NEW_VERSION}\"/g" "${FILE_PATH}"
else
  # Linux
  sed -i "/version = /c\version = \"${NEW_VERSION}\"" "${FILE_PATH}"
fi

echo "Version updated to ${NEW_VERSION}."
