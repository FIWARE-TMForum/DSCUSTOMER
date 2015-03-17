package org.tmf.dsmapi.hub.service;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import org.tmf.dsmapi.commons.facade.AbstractFacade;
import org.tmf.dsmapi.hub.model.Hub;

@Stateless
public class HubFacade extends AbstractFacade<Hub>{
    
    @PersistenceContext(unitName = "DSCustomerPU")
    private EntityManager em;

    /**
     *
     */
    public HubFacade() {
        super(Hub.class);
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
