package net.i2cat.scef.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import autogen.pcf.api.model.EventsNotification;
import autogen.pcf.api.model.PduSessionTsnBridge;
import autogen.scef.notification.api.model.UserPlaneEvent;
import autogen.scef.notification.api.model.UserPlaneEventReport;
import autogen.scef.notification.api.model.UserPlaneNotificationData;
import lombok.RequiredArgsConstructor;
import net.i2cat.scef.client.ScsasClient;
import net.i2cat.scef.model.InternalSubscription;
import net.i2cat.scef.repository.SubscriptionRepository;

@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class PcfNoficationReceiver {

    static final Log log = LogFactory.getLog(PcfNoficationReceiver.class);

    private final SubscriptionRepository repo;
    private final ScsasClient scsasClient;

    // Terminate:
    //  Call scsasclient to notifiy termination: Send UserPlaneEvent event of type SESSION_TERMINATION
    // Between PCF and SCEF a distinction is made between termination and eventnofitication
    //  Between SCEF and SCSAS, all are eventnotifications

    // Open5Gs is wrong and does not send TerminationInfo
    // To fix it, we add the internal session id in the notification URL
    // We'll receive something like /notifications/{internalSessionId}/terminate

    // The following code would be the correct one
    /* 
    @RequestMapping(
        method = RequestMethod.POST,
        value = "/notifications/terminate",
        produces = { "application/json", "application/problem+json" },
        consumes = { "application/json" }
    )
    public ResponseEntity<Void> terminate(TerminationInfo terminationInfo){
        
        String resourceUri = terminationInfo.getResUri();
        String pcfSessionId = resourceUri.substring(resourceUri.lastIndexOf("/") + 1);

        // Get session from localdb
        List<InternalSubscription> is = repo.findByPcfSessionId(pcfSessionId);
        if (is.size() == 1) {
            // Send event
            String scsasUri = is.get(0).getSubs().getNotificationDestination();      
            UserPlaneEventReport eventReport = new UserPlaneEventReport();
            eventReport.setEvent(UserPlaneEvent.SESSION_TERMINATION);
            List<UserPlaneEventReport> eventReportList = new ArrayList<UserPlaneEventReport>();
            eventReportList.add(eventReport);
            UserPlaneNotificationData notificationData = new UserPlaneNotificationData();
            notificationData.setEventReports(eventReportList);
            notificationData.setTransaction(is.get(0).getId());
            try{
                scsasClient.notifiEvent(scsasUri, notificationData);
            }
            catch( Exception e){
                log.info("Error sending termiation event to ScsAs Server", e);
            }
            // Delete from localdb
            repo.deleteById(is.get(0).getId());
            // return ack
            return ResponseEntity.noContent().build();
        } else {
            // return not found
            return ResponseEntity.notFound().build();
        }
    }
    */
    
     @PostMapping(
        value = "/notifications/{internalSubscritionId}/terminate",
        produces = { "application/json", "application/problem+json" }
    )
    public ResponseEntity<Void> terminate(@PathVariable("internalSubscritionId") String internalSubscritionId){

        log.info("************************ Calling terminate ************************");
        
        // Get session from localdb
        Optional<InternalSubscription> is = repo.findById(internalSubscritionId);
        if (!is.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        String scsasUri = is.get().getSubs().getNotificationDestination();      
        UserPlaneEventReport eventReport = new UserPlaneEventReport();
        eventReport.setEvent(UserPlaneEvent.SESSION_TERMINATION);
        List<UserPlaneEventReport> eventReportList = new ArrayList<>();
        eventReportList.add(eventReport);
        UserPlaneNotificationData notificationData = new UserPlaneNotificationData();
        notificationData.setEventReports(eventReportList);
        notificationData.setTransaction(is.get().getSubs().getSelf());
        try{
            scsasClient.notifiEvent(scsasUri, notificationData);
        }
        catch( Exception e){
            log.info("Error sending termination event to ScsAs Server", e);
        }
        // Delete from localdb
        repo.deleteById(is.get().getId());
        // return ack
        return ResponseEntity.noContent().build();
    }

    // Open5GS does not send notifications about other events, so we do not implement them
    @PostMapping(
        value = "/notifications/evSubs/notify",
        produces = { "application/json", "application/problem+json" },
        consumes = { "application/json" }
    )
    public ResponseEntity<Void> notify(EventsNotification eventsNotification){
        return ResponseEntity.unprocessableEntity().build();
    }


    @PostMapping(
        value = "/notifications/evSubs/new-bridge",
        produces = { "application/json", "application/problem+json" },
        consumes = { "application/json" }
    )
    public ResponseEntity<Void> newBrige(PduSessionTsnBridge tsnBridge){
        return ResponseEntity.unprocessableEntity().build();
    }
    
}
