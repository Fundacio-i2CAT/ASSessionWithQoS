package net.i2cat.scef.client;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import autogen.scef.notification.api.AsSessionWithQoSApiSubscriptionEventsNotificationCallbackApi;
import autogen.scef.notification.api.model.UserPlaneNotificationData;

@Service
public class ScsasClient {

    static final Log log = LogFactory.getLog(ScsasClient.class);
    
    AsSessionWithQoSApiSubscriptionEventsNotificationCallbackApi notificationApi = new AsSessionWithQoSApiSubscriptionEventsNotificationCallbackApi();

    public void notifiEvent(String notificationUri, UserPlaneNotificationData userPlaneNotificationData){
        log.info("************************ Calling notifEvent ************************");

        try {
            notificationApi.getApiClient().setBasePath(notificationUri);
            notificationApi.notificationsPost(userPlaneNotificationData);

        } catch (Exception e) {
            log.info("Error sending notification to ScsAS server",e);
            e.printStackTrace();
        }
    }
}
