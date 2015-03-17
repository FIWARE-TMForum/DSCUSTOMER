package org.tmf.dsmapi.customer.hub.service;

import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
//import javax.ejb.Asynchronous;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import org.tmf.dsmapi.commons.exceptions.BadUsageException;
import org.tmf.dsmapi.customer.model.Customer;
import org.tmf.dsmapi.customer.hub.model.CustomerEvent;
import org.tmf.dsmapi.customer.hub.model.CustomerEventTypeEnum;
import org.tmf.dsmapi.customer.hub.service.CustomerRESTEventPublisherLocal;
import org.tmf.dsmapi.hub.model.Hub;
import org.tmf.dsmapi.hub.service.HubFacade;

/**
 *
 * Should be async or called with MDB
 */
@Stateless
//@Asynchronous
public class CustomerEventPublisher implements CustomerEventPublisherLocal {

    @EJB
    HubFacade hubFacade;
    @EJB
    CustomerEventFacade eventFacade;
    @EJB
    CustomerRESTEventPublisherLocal restEventPublisherLocal;

    /** 
     * Add business logic below. (Right-click in editor and choose
     * "Insert Code > Add Business Method")
     * Access Hubs using callbacks and send to http publisher 
     *(pool should be configured around the RESTEventPublisher bean)
     * Loop into array of Hubs
     * Call RestEventPublisher - Need to implement resend policy plus eviction
     * Filtering is done in RestEventPublisher based on query expression
    */ 
    @Override
    public synchronized void publish(CustomerEvent event) {
        try {
            eventFacade.create(event);
        } catch (BadUsageException ex) {
            Logger.getLogger(CustomerEventPublisher.class.getName()).log(Level.SEVERE, null, ex);
        }

        List<Hub> hubList = hubFacade.findAll();
        Iterator<Hub> it = hubList.iterator();
        while (it.hasNext()) {
            Hub hub = it.next();
            restEventPublisherLocal.publish(hub, event);
        }
    }

    @Override
    public void createNotification(Customer bean, Date date) {
        CustomerEvent event = new CustomerEvent();
        event.setEventTime(date);
        event.setEventType(CustomerEventTypeEnum.CustomerCreationNotification);
        event.setEvent(bean);
        publish(event);

    }

    @Override
    public void deletionNotification(Customer bean, Date date) {
        CustomerEvent event = new CustomerEvent();
        event.setEventTime(date);
        event.setEventType(CustomerEventTypeEnum.CustomerDeletionNotification);
        event.setEvent(bean);
        publish(event);
    }
	
    @Override
    public void updateNotification(Customer bean, Date date) {
        CustomerEvent event = new CustomerEvent();
        event.setEventTime(date);
        event.setEventType(CustomerEventTypeEnum.CustomerUpdateNotification);
        event.setEvent(bean);
        publish(event);
    }

    @Override
    public void valueChangedNotification(Customer bean, Date date) {
        CustomerEvent event = new CustomerEvent();
        event.setEventTime(date);
        event.setEventType(CustomerEventTypeEnum.CustomerValueChangeNotification);
        event.setEvent(bean);
        publish(event);
    }
}
