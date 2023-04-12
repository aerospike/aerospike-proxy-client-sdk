#!/usr/bin/env bash

usage() {
  cat <<EOF
usage: bash release.sh --module aerospike-jms-outbound --version 1.1.0 --release-notes-file release-notes.md --release-args
  -m  (Required)          Module to release
  -v  (Required)          Version
  -a                      Additional arguments to pass to gradle release command
  -h                      Print usage help

Requires github credentials as environment variables GITHUB_USERNAME and GITHUB_TOKEN
EOF
}

while getopts m:v:a:h opt; do
  # shellcheck disable=SC2220
  case "$opt" in
  m)
        module=${OPTARG}
        ;;
  v)
    version=${OPTARG}
    ;;
  a)
    releaseArgs=${OPTARG}
    ;;
  h)
    usage
    exit 1
    ;;
  esac
done

if [ -z "$module" ]; then
  echo "Module name is required"
  exit 1
fi

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
echo "Releasing module:$module version:$version"
echo "Args release-args:$releaseArgs"
echo "--------------------------------------------------------------------------"

# Run vulnerability scan
./gradlew --no-daemon "$module:vulnerabilityScan"

# Switch to module directory
moduleDir=${module/aerospike-proxy-/}
cd "$moduleDir" || exit 1
../gradlew --no-daemon release -Prelease.useAutomaticVersion=true -Prelease.releaseVersion=$version  $releaseArgs
