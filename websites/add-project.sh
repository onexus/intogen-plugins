#!/bin/bash

if [[ "$#" != 2 ]]; then
    echo "Syntax: ./add-project <user-name> <project-name>"
    exit 1
fi

USERNAME=$1
PROJECTNAME=$2

WOK_RESULTS="/usr/local/intogen-sm/intogen-sm-dev/runtime/results/bgadmin";
WOK_WORKSPACE="default"

ONEXUS_PROJECTS="/home/bgadmin/.onexus/projects.ini"
ONEXUS_TEMPLATES="/projects_bg/bg/intogen/develop/intogen-plugins/websites/user"

PROJECT_FOLDER=$WOK_RESULTS"/"$USERNAME"/"$WOK_WORKSPACE"/projects/"$PROJECTNAME
PROJECT_WEBSITE_FOLDER=$PROJECT_FOLDER"/"website

# Copy the templates
rsync --exclude="results" -a $ONEXUS_TEMPLATES/* $PROJECT_WEBSITE_FOLDER

# Update PROJECTNAME property
sed -i "s/##PROJECTNAME##/$PROJECTNAME/g" $PROJECT_WEBSITE_FOLDER/css/header.html

# Create a symbolic link to the datasets
ln -s $PROJECT_FOLDER/datasets $PROJECT_WEBSITE_FOLDER/results

# Add the new project to Onexus
echo "private\://$USERNAME/$PROJECTNAME=$PROJECT_WEBSITE_FOLDER,$USERNAME/$PROJECTNAME" >> $ONEXUS_PROJECTS
