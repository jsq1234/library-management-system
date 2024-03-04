#!/bin/bash

bold=$(tput bold)
cyan=$(tput setaf 6)
reset=$(tput sgr0)

if [ ! -d "./build" ] || [ ! -d "./build/libs" ]; then
    echo "${bold}${cyan}Building project...${reset}"
    ./gradlew clean build -x test
fi

docker-compose up

echo "${bold}${cyan}Removing docker containers...${reset}"

docker-compose down
