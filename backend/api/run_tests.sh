#!/bin/bash

# ANSI escape codes for colors and formatting
bold=$(tput bold)
cyan=$(tput setaf 6)
reset=$(tput sgr0)

# Function to print loading animation with dots
loading_animation() {
    local loading_text="$1"
    local delay=0.5  # Adjust the delay between dots (in seconds)
    local dots=""

    while true; do
        echo -n "${bold}${cyan}${loading_text}${dots}${reset}"
        sleep $delay
        echo -ne "\r\033[K"  # Move cursor to beginning of line and clear line
        dots+="."
        if [ ${#dots} -gt 3 ]; then
            dots=""
        fi
    done
}

# Define default value for test_name
test_name=""

# Parse command-line options
while getopts ":t:" opt; do
    case ${opt} in
        t)
            test_name="$OPTARG"
            ;;
        \?)
            echo "${bold}${cyan}Invalid option: -$OPTARG requires a value${reset}" >&2
            exit 1
            ;;
        :)
            echo "${bold}${cyan}Option -$OPTARG requires a value${reset}" >&2
            exit 1
            ;;
    esac
done
shift $((OPTIND -1))

echo "${bold}${cyan}Starting MySQL Docker container for test${reset}"

# Check if MySQL Docker container exists
if docker ps -a --format '{{.Names}}' | grep -q '^mysql-test-db$'; then
    echo "${bold}${cyan}MySQL Docker container already exists. Starting it...${reset}"
    docker start mysql-test-db &> /dev/null
else
    # Start MySQL Docker container in detached mode
    docker run --name mysql-test-db -e MYSQL_ROOT_PASSWORD=1234 -e MYSQL_DATABASE=library -p 3346:3306 -d mysql:latest

    loading_animation "Waiting for MySQL docker to be ready" &

    until docker exec mysql-test-db mysqladmin --user=root --password=1234 ping --silent &> /dev/null; do
        sleep 2
    done

    kill $! &>/dev/null

    echo -e "\r\033${reset}"

    loading_animation "MySQL is ready. Creating 'library' database" &

    retry_count=0
    max_retries=3
    retry_interval=5
    while [ $retry_count -lt $max_retries ]; do
        docker exec mysql-test-db mysql --user=root --password=1234 --execute="CREATE DATABASE IF NOT EXISTS library;" &> /dev/null && break
        sleep $((retry_interval * (2**retry_count)))
        retry_count=$((retry_count + 1))
    done

    if [ $retry_count -eq $max_retries ]; then
        echo "${bold}${cyan}Failed to create 'library' database after $max_retries retries.${reset}"
        exit 1
    fi

    kill $! &>/dev/null

echo -e "\r\033[K${bold}${cyan}'library' database created successfully.${reset}"

fi

if [ -n "$test_name" ]; then
    echo "${bold}${cyan}Running test: library.backend.api.${test_name}${reset}"
    ./gradlew clean test --tests "library.backend.api.${test_name}"
else
    echo "${bold}${cyan}Running all tests...${reset}"
    ./gradlew clean test
fi

# Stop and remove MySQL Docker container
 docker stop mysql-test-db
 echo "${bold}${cyan}MySQL Docker container stopped${reset}"

