#!/usr/bin/env bash

do_gradle_task() {
    printf "\n\n---- Running gradle task $1 \n"
    eval "./gradlew " $1
    returnValue=$?

    if [ $returnValue != 0 ]
    then
        printf "Failed when running $1 \n"
        return 1
    fi

    return 0
}

is_ci() {
    if [[ ! -z "${BUDDYBUILD_BUILD_NUMBER}" ]]; then
        printf "Detected BuddyBuild CI\n"
        return 0;
    fi

    printf "No CI detected\n"
    return 1
}


# For some reason BB is not running unit tests in our core module
do_gradle_task "core:test"
if [ $? != 0 ]
then
    exit 1
fi

# Buddy Build won't let us run lint from a script like this.
# Instead we need to run lint via the BuddyBuild app settings checkbox

#Checkstyle everything
do_gradle_task "core:checkstyleMain"
if [ $? != 0 ]
then
    exit 1
fi

do_gradle_task "core:checkstyleTest"
if [ $? != 0 ]
then
    exit 1
fi

do_gradle_task "rest:androidCheckstyle"
if [ $? != 0 ]
then
    exit 1
fi

do_gradle_task "app:androidCheckstyle"
if [ $? != 0 ]
then
    exit 1
fi

exit 0

