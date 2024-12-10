package net.i2cat.scef.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import autogen.scef.api.model.AsSessionWithQoSSubscription;
import lombok.Data;

@Data
public class InternalSubscription {

    /*
    We use an id generated in the SCEF and not the id returned by Open5GS because the notify message sent by Open5GS when the session is closed is not correct, and it does not include the session id. Therefore, we use the response url as a cookie and put the session id in it.
    */

    @JsonProperty("id")
    private String id;

    private AsSessionWithQoSSubscription subs;
    private String scsAsId;
    private String pcfSessionId;
}
