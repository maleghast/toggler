#Toggler# [![StackShare](https://img.shields.io/badge/tech-stack-0690fa.svg?style=flat)](https://stackshare.io/maleghast5033/toggler)

##A Feature Toggle Micro Service written in Clojure##

A few years ago, when I was contracting at the BBC I came across a feature of their
"Forge" platform called Flagpoles.  These Flagpoles were supposed to be used to
announce the status of services and applications in order to let developers who were
consuming / relying on these other applications whether or not they were working and
to provide a mechanism where if not, the dependent application could fail early and
not attempt to consume that service.  This is a broadly good idea.

Over the time that I spent contracting there and then later on as a full-time employee
I noticed two things:

1. More and more often developers on the Forge Platform were creating flagpoles as feature toggles.

2. The new Cosmos environment had no easy way to discover or use Flagpoles on The Forge.

As such, in my free time, I started to create a simple (micro) service that could be used to
manage feature toggles in a way that was de-coupled from the applications that would
use them, while at the same time being performant and reliable, and that would allow
development teams to turn toggles on and off without having to deploy code.

Toggler was the result.

I am even about to use it in Production!

I offer it here as a reminder of how versatile Clojure is as a language,
and as what is I hope a reasonably good example of how to use Clojure and Liberator to
construct a basic REST-based web service.

##Just Using it...##

###Docker###

There is a Docker file, if you use that you don't need to read the section below about "running"
the application / service.  You should, however take a look at the file README-Docker.md

###Standalone#########

If you grab a release and all you want to be worried about is having Java on the machine
you are going to run it on, then grab a "standalone" jar and run it like this:

java -jar toggler-[version number]-standalone.jar [/absolute/path/to/config/]

and the embedded Jetty will spin up on port 7000.

The releases were created on a machine running Java 1.8, I haven't tested them on 1.7 or
lower and make no guarantees that they will work.

The service has an example config, just so that the application has some example
"toggles" for you to play with.  It is in the root of the project "config.json" - you should
move it to a disk location that will be writable by the running application.

All you need to do is write your own config in valid JSON in the same structure and then PUT
it to the service on the /reconfigure endpoint.

##Messing and modifying##

Please feel absolutely free to grab the source, play around with it and modify it to your
heart's content, see below for what you will need.

##Prerequisites##

You will need [Leiningen][1] 1.7.0 or above installed, and Java 1.7 or 1.8.

[1]: https://github.com/technomancy/leiningen

##Running##

To start the application using Jetty, without compiling the code into a .jar, simply run:

    lein run [/absolute/path/to/config/]

If you want to run the .jar and have no interest in installing Clojure, Leiningen etc, try:

    java -jar toggler-[version number]-standalone.jar [/absolute/path/to/config/]

##Using the Service##

I recommend [Postman][2] and [Insomnia][3], but grab the REST Client of your choice
and poke the API:

[2]: https://www.getpostman.com/
[3]: https://insomnia.rest/

    GET http://127.0.0.1:7000/toggle

and see all the default / example feature toggles.

    GET http://127.0.0.1:7000/toggle/app1

and see all the toggles for "app1"

    GET http://127.0.0.1:7000/toggle/app1/cache

and get the value of the "cache" feature toggle in "app1"

You can see where this is going, I hope...

There are PUT and POST options to add new components and toggles, and to save your new / current
config, even to load other configs have a play around with it, that's the best way to explore.

