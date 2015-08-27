## What is MATSim Server ?

**MATSim Server** is a stand alone components originally developed for
[MATSim Studio](http://matsimstudio.org) in order to delegate simulation
execution into another machine.

---

#### Distant Execution

Run simulation over a distant machine. Avoid using your personal machine resources,
and dispatch as many simulation as you want into a more powerful computing machine.

#### RESTFull API

Upload your simulation's initial demand and run it using RESTFull API.
Data are validated, then a simulation is started. Once it is done, you
can retrieve output data using the API as well.

#### Simulation Monitoring

Track simulation progress whenever you want, including iteration number,
and simulation steps (mobsim, scoring, or replanning) as well.

---

## Getting started

Want to start ? Just download [MATSim Server](https://github.com/Faylixe/MATSimServer/releases) java binary and uploads it to your server, once it is done then run it !

## Extending server

Wish to add custom functionality to the server or using a different [MATSim](http://matsim.org) version ?
You can download project source from project [GITHub repository](https://github.com/Faylixe/MATSimServer).
**MATSim Server** is built using [Java 8](https://www.java.com/fr/download/faq/java8.xml) and [Maven](https://maven.apache.org/)