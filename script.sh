#!/bin/bash

# Check if Jenkins is ready to accept commands.
cd /var/jenkins_home
result=false
result=$(curl  http://127.0.0.1:8080 | grep Dashboard)

while ! [ "$result" ]
do
sleep 10s
result=$(curl  http://127.0.0.1:8080 | grep Dashboard)
done 

# Wait until all plugins have been installed.
success=false
success=$(curl  --data-urlencode "script=$(< ./groovy_scripts/jenkins_install_plugins.groovy)" http://127.0.0.1:8080/scriptText | grep "All plugins have been installed successfully.")

while ! [ "$success" ]
do
sleep 10s
done

# Check if Jenkins is ready to accept commands.
result=false
result=$(curl  http://127.0.0.1:8080 | grep Dashboard)

while ! [ "$result" ]
do
sleep 10s
result=$(curl  http://127.0.0.1:8080 | grep Dashboard)
done 

# Configure plugins
curl  --data-urlencode "script=$(< ./groovy_scripts/jenkins_add_credentials_for_agent.groovy)" http://127.0.0.1:8080/scriptText
curl  --data-urlencode "script=$(< ./groovy_scripts/jenkins_add_node_agent.groovy)" http://127.0.0.1:8080/scriptText
curl  --data-urlencode "script=$(< ./groovy_scripts/jenkins_timestamper_config.groovy)" http://127.0.0.1:8080/scriptText
curl  --data-urlencode "script=$(< ./groovy_scripts/jenkins_artifactory_config.groovy)" http://127.0.0.1:8080/scriptText
curl  --data-urlencode "script=$(< ./groovy_scripts/jenkins_sonarqube_config.groovy)" http://127.0.0.1:8080/scriptText

# Create and start seed job
curl -XPOST http://127.0.0.1:8080/createItem?name=seed --data-binary @seed.xml -H "Content-Type:text/xml"
curl -XPOST http://127.0.0.1:8080/job/seed/build
echo "Script.sh has been finished."
