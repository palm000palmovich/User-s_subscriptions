package users.subscriptions.component;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import users.subscriptions.model.SubscriptionRelevance;
import users.subscriptions.model.Subscriptions;
import users.subscriptions.model.User;
import users.subscriptions.repository.SubscriptionRelevanceRepository;
import users.subscriptions.repository.SubscriptionsRepository;
import users.subscriptions.repository.UserRepository;

import javax.swing.text.html.Option;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Component
public class SubscriptionRelevanceDaemonChecker extends Thread{
    private final SubscriptionRelevanceRepository subscriptionRelevanceRepository;
    private final SubscriptionsRepository subscriptionsRepository;
    private final UserRepository userRepository;
    private final Logger logger = LoggerFactory.getLogger(SubscriptionRelevanceDaemonChecker.class);

    private volatile boolean running = true;

    public SubscriptionRelevanceDaemonChecker(SubscriptionRelevanceRepository subscriptionRelevanceRepository,
                                              SubscriptionsRepository subscriptionsRepository,
                                              UserRepository userRepository) {
        this.subscriptionRelevanceRepository = subscriptionRelevanceRepository;
        this.subscriptionsRepository = subscriptionsRepository;
        this.userRepository = userRepository;
    }

    @PostConstruct
    private void startThread(){
        this.setDaemon(true);
        this.start();
    }

    @Override
    public void run(){
        while (running){
            try{
                checkOverdueSubscriptions();
                Thread.sleep(6000);
            } catch (InterruptedException e){
                Thread.currentThread().interrupt();
                logger.info("Поток был прерван.");
            }
        }
    }

    @Transactional
    private void checkOverdueSubscriptions(){
        LocalDateTime currentDateTime = LocalDateTime.now();
        List<SubscriptionRelevance> subRelList = subscriptionRelevanceRepository
                .findByEndTimeBefore(currentDateTime);  //Список просроченных подписок

        for (SubscriptionRelevance subRel : subRelList){
            Optional<User> userOpt = userRepository.findById(subRel.getUser_id());
            userOpt.ifPresent(user -> {
                user.setSubscription(null);
                userRepository.save(user);
                logger.info("Удалена просроченная подписка у юзера с id " + user.getId());
            });

            Optional<Subscriptions> subOpt = subscriptionsRepository.findById(subRel.getSub_id());
            subOpt.ifPresent(sub -> {
                sub.setSubscribersCounter(Math.max(0, sub.getSubscribersCounter() - 1));
                subscriptionsRepository.save(sub);
            });

            subscriptionRelevanceRepository.delete(subRel);
        }

    }

    @PreDestroy
    private void stopThread(){
        running = false;
    }
}
