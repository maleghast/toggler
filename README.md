#Toggler#

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

One day I might actually work for an organisation that will let me use it...

In the meantime I leave it here as a reminder of how versatile Clojure is as a language,
and as what is I hope a reasonably good example of how to use Clojure and Liberator to
construct a basic REST-based web service.

##Just Using it...##

If you grab a release and all you want to be worried about is having Java on the machine
you are going to run it on, then grab a "standalone" jar and run it like this:

java -jar toggler-1.0.0-standalone.jar

and the embedded Jetty will spin up on port 7000.

The releases were created on a machine running Java 1.8, I haven't tested them on 1.7 or
lower and make no guarantees that they will work.

The service has an embedded example config, just so that the application has some example
"toggles" for you to play with.  All you need to do is write your own config in valid
JSON in the same structure and then PUT it to the service on the /reconfigure endpoint.

##Messing and modifying##

Please feel absolutely free to grab the source, play around with it and modify it to your
heart's content, see below for what you will need.

##Prerequisites##

You will need [Leiningen][1] 1.7.0 or above installed, and Java 1.7 or 1.8.

[1]: https://github.com/technomancy/leiningen

##Running##

To start the application using Jetty, without compiling the code into a .jar, simply run:

    lein run

=======
Toggler
=======

A Clojure Feature-Toggle Service
