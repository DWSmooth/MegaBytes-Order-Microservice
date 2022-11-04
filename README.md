# Order-Microservice

This is a fork with an added Jenkinsfile to build the maven project. This file is on the /JenkinDevWindows/ branch.

## Details

The pipeline script last functioned best running on a local Jenkins server -- An EC2 instance running Jenkins would bottleneck when building and not respond for more than an hour without completing.
Locally, the project would finish building and pushing in about 40 minutes.
