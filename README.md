# sap-jco-server-example
Complete and running example of a JCo Server-based scenario to make an RFC from ABAP to Java

![diagram](https://github.com/johannes-gilbert/sap-jco-server-example/blob/main/docs/SAP%20JCo%20Server%20Example.png)

## Complie

Run maven goal `compile`.

## Setup

### SM59 destination

TL;DR
1) Start transaction SM59.
2) Create a _TCP/IP Connection_ and recognize its name (RFC Destination). Make sure to set the option as described in the following. Note that these will be most likely **not suitable for productive use**. 

| Settings                                                        | Value                                |
|-----------------------------------------------------------------|--------------------------------------|
| Technical Settings > Activation Type                            | Registered Server Program            |
| Technical Settings > Program ID                                 | JCO_SERVER (*or some other name*)    |
| Technical Settings > Start Type of External Program             | Default Gateway Value                |
| Technical Settings > CPI-C Timeout                              | Default Gateway Value                |
| Technical Settings > Gateway Options > Gateway Host             | <host's fully qualified domain name> |
| Technical Settings > Gateway Options > Gateway Service          | sapgw<instance number>               |
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

An SM59 destination is required. It allows the Java program to connected to the SAP NetWeaver ABAP using JCo. Moreover, it is required for the ABAP program to perform a remote function call (RFC) to the Java program using the option `DESTINATION <destinationName>` of the ABAP statement `CALL FUNCTION`.

![diagram](https://github.com/johannes-gilbert/sap-jco-server-example/blob/main/docs/screenshot_sm59.jpg)

It is requred 

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
