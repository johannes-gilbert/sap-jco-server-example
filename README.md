# sap-jco-server-example
Complete and running example of a JCo Server-based scenario to make an RFC from ABAP to Java

![diagram](https://github.com/johannes-gilbert/sap-jco-server-example/blob/main/SAP%20JCo%20Server%20Example.png)

## Complie

Run maven goal `compile`.

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
