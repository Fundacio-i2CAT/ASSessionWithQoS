package net.i2cat.scef.controller;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import autogen.scef.api.ScsAsIdApi;
import autogen.scef.api.model.AsSessionWithQoSSubscription;
import autogen.scef.api.model.AsSessionWithQoSSubscriptionPatch;
import autogen.scef.api.model.UserPlaneNotificationData;
import net.i2cat.scef.client.PcfClient;
import net.i2cat.scef.model.InternalSubscription;
import net.i2cat.scef.repository.SubscriptionRepository;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class ScsASIdController implements ScsAsIdApi {

    static final Log log = LogFactory.getLog(ScsASIdController.class);

    private final SubscriptionRepository repo;
    private final PcfClient pcf;

    @Override
    public ResponseEntity<List<AsSessionWithQoSSubscription>> scsAsIdSubscriptionsGet(String scsAsId) {

        // Get all subscriptions for a SCS/AS (Service Capability Server/Application Server)

        List<InternalSubscription> listInternalSubs = repo.findByScsAsId(scsAsId);
        List<AsSessionWithQoSSubscription> subsList = new ArrayList<>();
        for (InternalSubscription s : listInternalSubs) {
            subsList.add(s.getSubs());
        }
        return ResponseEntity.ok(subsList);

    }
    // POST /{scsAsId}/subscriptions : Creates a new subscription resource
    @Override
    public ResponseEntity<AsSessionWithQoSSubscription> scsAsIdSubscriptionsPost(String scsAsId,
            @jakarta.validation.Valid AsSessionWithQoSSubscription asSessionWithQoSSubscription) {
        
        // Create Session at PCF. Returns sessionId

        /*
        We use an id generated in the SCEF and not the id returned by Open5GS because the notify message sent by Open5GS when the session is closed is not correct, and it does not include the session id. Therefore, we use the response url as a cookie and put the session id in it.
         */

        String id = UUID.randomUUID().toString();

        String pcfSessionId = pcf.postAppSession(asSessionWithQoSSubscription,id);

        if (pcfSessionId != null) {

            // Create Mongo internal subscription
            InternalSubscription is = new InternalSubscription();
            String self = ServletUriComponentsBuilder.fromCurrentRequest().toUriString() + "/" + id;
            log.info("self: " + self);
            asSessionWithQoSSubscription.setSelf(self);
            is.setId(id);
            is.setPcfSessionId(pcfSessionId);
            is.setScsAsId(scsAsId);
            is.setSubs(asSessionWithQoSSubscription);
            repo.save(is);
            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{subscriptionId}").buildAndExpand(is.getId())
                    .toUri();
            return ResponseEntity.created(location).body(asSessionWithQoSSubscription);
        } else{
            return ResponseEntity.internalServerError().build();
        }
    }

    @Override
    public ResponseEntity<UserPlaneNotificationData> scsAsIdSubscriptionsSubscriptionIdDelete(String scsAsId,
            String subscriptionId) {

        Optional<InternalSubscription> is = repo.findById(subscriptionId);
        if (is.isPresent() && is.get().getScsAsId().equalsIgnoreCase(scsAsId)) {
            pcf.deleteAppSession(is.get().getPcfSessionId());
            repo.deleteById(subscriptionId);
            return ResponseEntity.ok(null);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Override
    public ResponseEntity<AsSessionWithQoSSubscription> scsAsIdSubscriptionsSubscriptionIdGet(String scsAsId,
            String subscriptionId) {

        Optional<InternalSubscription> is = repo.findById(subscriptionId);
        if (is.isPresent() && is.get().getScsAsId().equalsIgnoreCase(scsAsId)) {
            return ResponseEntity.ok(is.get().getSubs());
        } else {
            return ResponseEntity.notFound().build();
        }

    }

    @Override
    public ResponseEntity<AsSessionWithQoSSubscription> scsAsIdSubscriptionsSubscriptionIdPatch(String scsAsId,
            String subscriptionId, @jakarta.validation.Valid AsSessionWithQoSSubscriptionPatch asSessionWithQoSSubscriptionPatch) {

        return ResponseEntity.unprocessableEntity().build();
    }

    @Override
    public ResponseEntity<AsSessionWithQoSSubscription> scsAsIdSubscriptionsSubscriptionIdPut(String scsAsId,
            String subscriptionId, @jakarta.validation.Valid AsSessionWithQoSSubscription asSessionWithQoSSubscription) {

        return ResponseEntity.unprocessableEntity().build();
    }
    

}
