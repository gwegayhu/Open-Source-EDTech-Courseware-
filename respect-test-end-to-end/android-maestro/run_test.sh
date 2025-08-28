#!/bin/bash

# Set environment variables
DIRECTORY_URL="http://your-directory-url"
SCHOOL_URL="http://your-school-url"
SCHOOL_NAME="your-realm-name"
SCHOOL_ADMIN_USER="your-admin-user"
SCHOOL_ADMIN_PASS="your-admin-pass"
TESTCONTROLLER_URL="http://192.168.0.168:8075/"

# Set the test folder path
TEST_FOLDER="e2e-tests"

# Determine what to run
if [ -z "$1" ]; then
  echo "No test file provided. Running all tests in folder: $TEST_FOLDER"
  TEST_TARGET="$TEST_FOLDER"
else
  TEST_TARGET="$1"
fi

# Run Maestro with environment variables
maestro test "$TEST_TARGET" \
  -e DIRECTORY_URL="$DIRECTORY_URL" \
  -e REALM_URL="$REALM_URL" \
  -e REALM_NAME="$REALM_NAME" \
  -e REALM_ADMIN_USER="$REALM_ADMIN_USER" \
  -e REALM_ADMIN_PASS="$REALM_ADMIN_PASS" \
  -e TESTCONTROLLER_URL="$TESTCONTROLLER_URL"
