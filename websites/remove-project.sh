#!/bin/bash

if [[ "$#" != 2 ]]; then
    echo "Syntax: ./remove-project <user-name> <project-name>"
    exit 1
fi

USERNAME=$1
PROJECTNAME=$2

ONEXUS_PROJECTS="/home/bgadmin/.onexus/projects.ini"

sed -i "/"$USERNAME"\/"$PROJECTNAME"/d" $ONEXUS_PROJECTS
