# MATSim Server [![Build Status](https://travis-ci.org/Faylixe/MATSimServer.svg?branch=master)](https://travis-ci.org/Faylixe/MATSimServer)


## What is MATSim Server ?

**MATSim Server** is a HTTP server that aims to delegate simulation
execution into another machine. Including following features :

* **Distant Execution** : Run simulation over a distant machine. Avoid using your personal machine resources, and dispatch as many simulation as you want into a more powerful computing machine. It provides features such as following.

* **RESTFull API** : Upload your simulation's initial demand and run it using RESTFull API. Data are validated, then a simulation is started. Once it is done, you can retrieve output data using the API as well.

* **Simulation Monitoring** : Track simulation progress whenever you want, including iteration number, and simulation steps (mobsim, scoring, or replanning) as well.

## Getting started

Want to start ? Just download [MATSim Server](https://github.com/Faylixe/MATSimServer/releases)
java binary and uploads it to your server, once it is done then run it !

### Running MATSim Server
Running server is as easy as starting a Java application. Once you have
downloaded server jar file and uploaded to your target machine, just run
the following command :

```bash
java -jar matsim-server.jar
```

### Server file container
By default, server instance will create a directory named ``.container`` if not
already existing and will write each simulation files on it. If you wish to set
another directory as target directory please use the Java property
``org.matsim.server.container`` like following :

```bash
java -jar matsim-server.jar -Dorg.matsim.server.container=/path/to/your/directory
```
