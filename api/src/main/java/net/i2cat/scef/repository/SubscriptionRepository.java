package net.i2cat.scef.repository;
import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import net.i2cat.scef.model.InternalSubscription;

public interface SubscriptionRepository extends MongoRepository<InternalSubscription,String>{

    public List<InternalSubscription> findByScsAsId(String scsAsId);

    public List<InternalSubscription> findByPcfSessionId(String pcfSessionId);

}
