# sap-jco-server-example
Complete and running example of a JCo Server-based scenario to make an RFC from ABAP to Java

![diagram](https://github.com/johannes-gilbert/sap-jco-server-example/blob/main/docs/SAP%20JCo%20Server%20Example.png)

* [Installation / Pre-requisites](https://github.com/johannes-gilbert/sap-jco-server-example#installation--pre-requisites)
    * [Maven](https://github.com/johannes-gilbert/sap-jco-server-example#maven)
    * [SAP JCo](https://github.com/johannes-gilbert/sap-jco-server-example#sap-jco)
* [Compile](https://github.com/johannes-gilbert/sap-jco-server-example#compile)
* [Setup](https://github.com/johannes-gilbert/sap-jco-server-example#setup)
    * [SM59 destination](https://github.com/johannes-gilbert/sap-jco-server-example#sm59-destination)
    * [RFC-enabled function module](https://github.com/johannes-gilbert/sap-jco-server-example#rfc-enabled-function-module)
* [Running the server](https://github.com/johannes-gilbert/sap-jco-server-example#running-the-server)
* [ABAP program](https://github.com/johannes-gilbert/sap-jco-server-example#abap-program)
* [Traces](https://github.com/johannes-gilbert/sap-jco-server-example#traces)
* [References](https://github.com/johannes-gilbert/sap-jco-server-example#references)

## Installation / Pre-requisites

### Maven

Install Apache Maven. See here: [https://maven.apache.org/download.cgi](https://maven.apache.org/download.cgi)

### SAP JCo

To make things easy get yourself a recent version of SAP Java Connector (JCo) from [support.sap.com](https://support.sap.com/en/product/connectors/jco.html). Extract the sapjco*.jar from the archive and put it somewhere to your disk (here: `c:/data/sapjco3.jar`). Install the SAP JCo jar-file to your local Maven repository from the command line:

````
mvn install:install-file -DgroupId=com.sap.conn.jco -DartifactId=com.sap.conn.jco.sapjco3 -Dversion=3.1.4 -Dpackaging=jar -Dfile=C:/data/sapjco3.jar
````

Output should be:

````shell
[INFO] Scanning for projects...
Downloading from central: https://repo.maven.apache.org/maven2/org/apache/maven/plugins/maven-clean-plugin/2.5/maven-clean-plugin-2.5.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/apache/maven/plugins/maven-clean-plugin/2.5/maven-clean-plugin-2.5.pom (3.9 kB at 8.8 kB/s)
[INFO]
[INFO] ------------------< org.apache.maven:standalone-pom >-------------------
[INFO] Building Maven Stub Project (No POM) 1
[INFO] --------------------------------[ pom ]---------------------------------
[INFO]
[INFO] --- maven-install-plugin:2.4:install-file (default-cli) @ standalone-pom ---
Downloading from central: https://repo.maven.apache.org/maven2/org/codehaus/plexus/plexus-utils/3.0.5/plexus-utils-3.0.5.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/codehaus/plexus/plexus-utils/3.0.5/plexus-utils-3.0.5.pom (2.5 kB at 39 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/codehaus/plexus/plexus/3.1/plexus-3.1.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/codehaus/plexus/plexus/3.1/plexus-3.1.pom (19 kB at 266 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/codehaus/plexus/plexus-digest/1.0/plexus-digest-1.0.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/codehaus/plexus/plexus-digest/1.0/plexus-digest-1.0.pom (1.1 kB at 17 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/codehaus/plexus/plexus-components/1.1.7/plexus-components-1.1.7.pom
Downloaded from central: https://repo.maven.apache.org/maven2/org/codehaus/plexus/plexus-components/1.1.7/plexus-components-1.1.7.pom (5.0 kB at 83 kB/s)
Downloading from central: https://repo.maven.apache.org/maven2/org/codehaus/plexus/plexus-utils/3.0.5/plexus-utils-3.0.5.jar
Downloading from central: https://repo.maven.apache.org/maven2/org/codehaus/plexus/plexus-digest/1.0/plexus-digest-1.0.jar
Downloaded from central: https://repo.maven.apache.org/maven2/org/codehaus/plexus/plexus-digest/1.0/plexus-digest-1.0.jar (12 kB at 96 kB/s)
Downloaded from central: https://repo.maven.apache.org/maven2/org/codehaus/plexus/plexus-utils/3.0.5/plexus-utils-3.0.5.jar (230 kB at 1.8 MB/s)
[INFO] Installing C:\data\sapjco3.jar to C:\Users\yyyyy\.m2\repository\com\sap\conn\jco\com.sap.conn.jco.sapjco3\3.1.4\com.sap.conn.jco.sapjco3-3.1.4.jar
[INFO] Installing C:\Users\yyyyy\AppData\Local\Temp\mvninstall16140285851761422900.pom to C:\Users\yyyyy\.m2\repository\com\sap\conn\jco\com.sap.conn.jco.sapjco3\3.1.4\com.sap.conn.jco.sapjco3-3.1.4.pom
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  2.710 s
[INFO] Finished at: 2022-02-25T14:21:29+01:00
[INFO] ------------------------------------------------------------------------
````

## Compile

Run maven goal `compile`.

## Setup

### SM59 destination

1) Start transaction `SM59`.
2) Create a _TCP/IP Connection_ and recognize its name (RFC Destination). Make sure to set the option as described in the following. Note that these will be most likely **not suitable for productive use**. 

| Settings                                                        | Value                                |
|-----------------------------------------------------------------|--------------------------------------|
| RFC Destination                                                 | MY_JCO_SRV (*or some other name*)    |
| Description 1                                                   | SAP JCo Server Example               |
| Technical Settings > Activation Type                            | Registered Server Program            |
| Technical Settings > Program ID                                 | JCO_SERVER (*or some other name*)    |
| Technical Settings > Start Type of External Program             | Default Gateway Value                |
| Technical Settings > CPI-C Timeout                              | Default Gateway Value                |
| Technical Settings > Gateway Options > Gateway Host             | &lt;host's fully qualified domain name&gt; |
| Technical Settings > Gateway Options > Gateway Service          | sapgw&lt;instance number&gt;         |
| Logon & Security > Logon Procedure > Logon with Ticket          | Do not send logon ticket             |
| Logon & Security > Security Options > Status of Secure Protocol | Inactive                             |
| Logon & Security > Callback Positive List                       | List inactive                        |
| Unicode > Communication Type with Target System                 | Unicode                              |
| Unicode > Character Conversion                                  | Default Setting                      |
| Special Options > Special Flags                                 | Slow RFC connection = unselected     |
| Special Options > Trace > Activate RFC Trace > Set RFC trace    | off                                  |
| Special Options > Trace > Trace Export Methods                  | Default Gateway Service              |
| Special Options > Keep-Alive Timeout                            | Default Gateway Value                |
| Special Options > Select Protocol > qRFC Version                | 2 (t-/qRFC)                          |
| Special Options > Select Protocol > Serialization               | C (Classic Serializer)               |
| Special Options > SAP HANA Cloud Connector                      | unselected                           |

**Details**

An SM59 destination is required. It allows the Java program to connected to the SAP NetWeaver ABAP using JCo. Moreover, it is required for the ABAP program to perform a remote function call (RFC) towards the Java program using the option `DESTINATION <destinationName>` of the ABAP statement `CALL FUNCTION`. For more information refer to the [ABAP Keyword Documentation on CALL FUNCTION - DESTINATION](https://help.sap.com/doc/abapdocu_751_index_htm/7.51/en-us/abapcall_function_destination.htm).

![diagram](https://github.com/johannes-gilbert/sap-jco-server-example/blob/main/docs/screenshot_sm59.jpg)
![diagram](https://github.com/johannes-gilbert/sap-jco-server-example/blob/main/docs/screenshot_sm59_technical_settings.jpg)
![diagram](https://github.com/johannes-gilbert/sap-jco-server-example/blob/main/docs/screenshot_sm59_logon_and_security.jpg)
![diagram](https://github.com/johannes-gilbert/sap-jco-server-example/blob/main/docs/screenshot_sm59_unicode.jpg)
![diagram](https://github.com/johannes-gilbert/sap-jco-server-example/blob/main/docs/screenshot_sm59_special_options.jpg)


### RFC-enabled function module

SAP JCo needs to know the meta data of the function module it mimics. This includes e.g. parameters, typing etc. Hence, JCo by default checks the data dictionary (DDIC) of an SAP NetWeaver ABAP. Therefore, the function module needs to be present in this ABAP system (no matter if there is any source code within this function module or not).

Hence, it is necessary to create a stub-like function module in the ABAP system you would like to connect to. In this example this is `Z_SAMPLE_ABAP_CONNECTOR_CALL`. Let's create this function module.

1) Start transaction `SE37` and create a function module with name `Z_SAMPLE_ABAP_CONNECTOR_CALL` in a suitable package.
    ![](https://github.com/johannes-gilbert/sap-jco-server-example/blob/main/docs/screenshot_create_function_module.jpg)
2) Enter a short text and make sure that for *Processing Type* the option *Remote-Enabled Module* is selected.
    ![](https://github.com/johannes-gilbert/sap-jco-server-example/blob/main/docs/screenshot_se37_attributes.jpg)
3) Define the *importing* parameter `URL` with type `string`.
    ![](https://github.com/johannes-gilbert/sap-jco-server-example/blob/main/docs/screenshot_se37_importing_params.jpg)
4) Define the *exporting* parameter `RESPONSE_PAYLOAD` with type `string`.
    ![](https://github.com/johannes-gilbert/sap-jco-server-example/blob/main/docs/screenshot_se37_exporting_params.jpg)
5) There is no need to change the ABAP source code. It can stay empty.
    ![](https://github.com/johannes-gilbert/sap-jco-server-example/blob/main/docs/screenshot_se37_source_code.jpg)

## Running the server

Execute the following command: 

````shell
java -cp sap-jco-server-example-1.0-jar-with-dependencies.jar;C:/data/sapjco3.jar com.sap.SampleAbapConnectorServer ..\jco.properties
````

You will see the following output:

````shell
>java -cp sap-jco-server-example-1.0-jar-with-dependencies.jar;C:/data/sapjco3.jar com.sap.SampleAbapConnectorServer ..\jco.properties
Setting VM argument jco.trace_path to value 'C:/xxxxxx/sap-jco-server-example/test'
Setting VM argument jco.trace_level to value '8'
Setting VM argument jrfc.trace to value '1'
There is no MyDestinationDataProvider registered so far. Registering a new instance.
There is no MyServerDataProvider registered so far. Registering a new instance.
Providing server properties for server 'JCO_SERVER' using the specified properties
Providing destination properties for destination '' using the specified properties
Server state changed from STOPPED to STARTED on server with program id JCO_SERVER
The program can be stopped typing 'END'
Server state changed from STARTED to ALIVE on server with program id JCO_SERVER
Server with program ID 'JCO_SERVER' is running
````

Notes:
* When you specify the `-jar` option then the `-cp` parameter will be ignored ([refer to the documentation](https://docs.oracle.com/javase/7/docs/technotes/tools/solaris/java.html#jar)).
* It is not allowed to include the `sapjco*.jar` into other jar-files. Otherwise you will get the error: 
````shell
Exception in thread "main" java.lang.ExceptionInInitializerError: JCo initialization failed with java.lang.ExceptionInInitializerError: Illegal JCo archive "sap-jco-server-example-0.0.1-SNAPSHOT-jar-with-dependencies.jar". It is not allowed to rename or repackage the original archive "sapjco3.jar".
````

* I found that the VM-parameter `-Djava.library.path=...` has no impact. Hence, Java does not find SAP Jco.

**Connection test**

Once the server is started, a connection test via transaction `SM59` should be successful.

![](https://github.com/johannes-gilbert/sap-jco-server-example/blob/main/docs/screenshot_sm59_connection_test.jpg)

**Stop the server**

The CLI program listens to input from the command line. The server can be stopped by typing `end` or `END` and pressing `ENTER` (⏎).

````shell
end
Server state changed from ALIVE to STOPPING on server with program id JCO_SERVER
Server state changed from STOPPING to STOPPED on server with program id JCO_SERVER
Exit program

>
````



## ABAP program

````ABAP
REPORT z_test_jco_server.


* Set the RFC destination name. It corresponds to the destination
* defined in SM59.
DATA(rfc_destination) = VALUE rfcdest( ).
rfc_destination = 'MY_JCO_SRV'.

* Define the URL that should be called from the JCo server. So
* to say this is just some data that is handed to the FM.
DATA(url) = |https://docs.oasis-open.org/odata/odata-csdl-xml/v4.01/os/schemas/edm.xsd|. "https://server/path/to/resource|.

DATA(payload) = ||.
DATA(msg) = VALUE char255( ).

* Call the FM remotely and hand the data (URL), but
* also retrieve the result (RESPONSE_PAYLOAD).
CALL FUNCTION 'Z_SAMPLE_ABAP_CONNECTOR_CALL'
  DESTINATION rfc_destination
  EXPORTING
    url                        = url
  IMPORTING
    response_payload           = payload
  EXCEPTIONS
    system_failure             = 1 MESSAGE msg
    communication_failure      = 2 MESSAGE msg
    internal_failure           = 3
    timeout                    = 4
    dest_communication_failure = 5
    dest_system_failure        = 6
    update_failure             = 7
    no_update_authority        = 8
    OTHERS                     = 9.

* Check for errors.
IF sy-subrc NE 0.
  WRITE: / 'CALL Z_SAMPLE_ABAP_CONNECTOR_CALL         SY-SUBRC = ', sy-subrc.
  IF msg IS NOT INITIAL.
    WRITE: / msg.
  ENDIF.
ELSE.
* Display the result. In the exmaple we are expecting that the HTTP-get
* call - done by the JCo server - results in a XML response. We retrieved it from
* the JCo server (via RESPONSE_PAYLOAD) and want to display it here in the ABAP backend.
  cl_demo_output=>display_xml( payload ).
ENDIF.
````

## Traces

*JRFC trace*
| aspect                   | value                                                                                                 |
|--------------------------|-------------------------------------------------------------------------------------------------------|
| name                     | jrfc*.trc                                                                                             |
| location                 | can be defined via parameter `jco.trace_path` in file `jco.properties` or via JVM options (see below) |
| how to switch on and off | set JVM options `-Djrfc.trace=0/1, -Djco.trace_path=[defined_path]`                                   |

*JCO trace*

| aspect                   | value                                                                                                 |
|--------------------------|-------------------------------------------------------------------------------------------------------|
| name                     | JCO*.trc                                                                                              |
| location                 | can be defined via parameter `jco.trace_path` in file `jco.properties` or via JVM options (see below) |
| how to switch on and off | set JVM options `-Djco.trace_level=[0-10], -Djco.trace_path=[defined_path]`                           |

For more information refer to [JCo Exceptions](https://help.sap.com/viewer/1d057e05920c4fe38b88e33aaa9eb5ef/7.03.30/en-US/f6daea401675752ae10000000a155106.html).

## References
* [JCo Exceptions](https://help.sap.com/viewer/1d057e05920c4fe38b88e33aaa9eb5ef/7.03.30/en-US/f6daea401675752ae10000000a155106.html) or navigate to [help.sap.com](https://help.sap.com) and search for 'JCo Exceptions'
