package com.upgrad.FoodOrderingApp.service.dao;

import com.upgrad.FoodOrderingApp.service.entity.StateEntity;
import org.springframework.stereotype.Repository;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
@Repository

public class StateDao {
    @PersistenceContext
    private EntityManager entityManager;

    public StateEntity getsStateByUUID(final String UUID){
        try{
            return entityManager.createNamedQuery("getStateByUuid",StateEntity.class)
                    .setParameter("StateUuid",stateUuid),
                    .getSingleResult();

        } catch (NoResultException nre){
            return null;
        }
    }
    public List<StateEntity> getAllStates(){
        return entityManager.createNamedQuery("getAllStates",StateEntity.class).getResultList();

    }
}

