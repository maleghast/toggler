#Toggler - Docker Instructions#

##Simple Instructions##

1. Download the latest release from the releases tab on the Github repository
2. Make sure that the Dockerfile references the release you have, that you should
   have placed in /target at the root of the project.
3. docker build

Have fun!

##Complex Instructions##

If you have made any changes to the code and you want to deploy your custom version
of Toggler as a container, then you need these extra steps:

1. Bump the version number in /project.clj
2. lein uberjar

Now follow the instructions above.
