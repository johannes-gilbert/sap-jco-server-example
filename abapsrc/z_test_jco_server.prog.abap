*&---------------------------------------------------------------------*
*& Report Z_TEST_JCO_SERVER
*&---------------------------------------------------------------------*
*&
*&---------------------------------------------------------------------*
REPORT z_test_jco_server.


* Set the RFC destination name. It corresponds to the destination
* defined in SM59.
DATA(rfc_destination) = VALUE rfcdest( ).
rfc_destination = 'MY_JCO_SRV'.

* Define the URL that should be called from the JCo server. So
* to say this is just some data that is handed to the FM.
DATA(url) = |https://docs.oasis-OPEN.org/odata/odata-csdl-xml/v4.01/os/schemas/edm.xsd|."https://server/path/to/resource|.

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
* the JCo server (via ev_response_payload) and want to display it here in the ABAP backend.
  cl_demo_output=>display_xml( payload ).
ENDIF.
