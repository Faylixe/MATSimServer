## Server usage

### Running MATSim Server

Running server is as easy as starting a Java application. Once you have downloaded server
<i>jar</i> file and uploaded to your target machine, just run the following command :

`> java -jar matsim-server.jar`
  
### Server file container

By default, server instance will create a directory named <b>.container</b> if not
already existing and will write each simulation files on it. If you wish to set another
directory as target directory please use the Java property <tt>org.matsim.server.container</tt>
like following :

`> java -jar matsim-server.jar -Dorg.matsim.server.container=/path/to/your/directory`