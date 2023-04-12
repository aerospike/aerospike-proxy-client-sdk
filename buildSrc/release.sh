#!/usr/bin/env bash

usage() {
  cat <<EOF
usage: bash release.sh --module aerospike-jms-outbound --version 1.1.0 --release-notes-file release-notes.md --release-args
  -v  (Required)          Version
  -a                      Additional arguments to pass to gradle release command
  -h                      Print usage help

Requires github credentials as environment variables GITHUB_USERNAME and GITHUB_TOKEN
EOF
}

while getopts v:a:h opt; do
  # shellcheck disable=SC2220
  case "$opt" in
  v)
    version=${OPTARG}
    ;;
  a)
    releaseArgs=${OPTARG}
    ;;
  h)
    usage
    exit 0
    ;;
  esac
done

if [ -z "$version" ]; then
  echo "Release version is required"
  exit 1
fi

if [ -z "$GITHUB_USERNAME" ]; then
  echo "Github username environment variable GITHUB_USERNAME not set".
  exit 1
fi

if [ -z "$GITHUB_TOKEN" ]; then
  echo "Github access token environment variable GITHUB_TOKEN not set".
  exit 1
fi

echo "--------------------------------------------------------------------------"
echo "Releasing version:$version"
echo "Args release-args:$releaseArgs"
echo "--------------------------------------------------------------------------"

# Run vulnerability scan
./gradlew vulnerabilityScan

# Run the release task
modules="proto stub"

for module in $modules; do
  cd "$module"
  ../gradlew --no-daemon release -Prelease.useAutomaticVersion=true -Prelease.releaseVersion=$version  $releaseArgs
done
