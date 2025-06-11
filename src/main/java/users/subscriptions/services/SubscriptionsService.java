package users.subscriptions.services;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import users.subscriptions.controller.UserController;
import users.subscriptions.dto.SubscriptionRequest;
import users.subscriptions.exceptions.SubscriptionAlreadyExistsException;
import users.subscriptions.exceptions.SubscriptionIsNotFoundException;
import users.subscriptions.exceptions.UserAlreadyHaveSubscriptionException;
import users.subscriptions.exceptions.UserNotFoundException;
import users.subscriptions.model.SubscriptionRelevance;
import users.subscriptions.model.Subscriptions;
import users.subscriptions.model.User;
import users.subscriptions.repository.SubscriptionRelevanceRepository;
import users.subscriptions.repository.SubscriptionsRepository;
import users.subscriptions.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubscriptionsService {

    private Logger logger = LoggerFactory.getLogger(SubscriptionsService.class);
    private final SubscriptionsRepository subscriptionsRepository;
    private final UserRepository userRepository;
    private final SubscriptionRelevanceRepository subscriptionRelevanceRepository;

    public SubscriptionsService(SubscriptionsRepository subscriptionsRepository,
                                UserRepository userRepository,
                                SubscriptionRelevanceRepository subscriptionRelevanceRepository) {
        this.subscriptionsRepository = subscriptionsRepository;
        this.userRepository = userRepository;
        this.subscriptionRelevanceRepository = subscriptionRelevanceRepository;
    }


    //Saving subscription
    @Transactional
    public Subscriptions uploadSubscription(Long id, String subType) {
        User user = validateAndGetUser(id);

        validateUserHasNoSubscription(user);

        Subscriptions subscription = findOrCreateSubscription(subType);

        user.setSubscription(subscription);
        userRepository.save(user);

        createSubscriptionRelevance(user, subscription);

        return subscription;
    }

    public SubscriptionRequest getUsersSubs(Long id) {
        User foundUser = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));
        logger.info("Найденный пользователь: " + foundUser.toString());

        Subscriptions usersSub = subscriptionsRepository.findById(foundUser.getSubscription().getId())
                .orElseThrow(() -> new SubscriptionIsNotFoundException(foundUser.getSubscription().getId()));

        logger.info("Подписка найденного пользователя: " + usersSub.toString());

        SubscriptionRequest subscriptionRequest = new SubscriptionRequest();

        subscriptionRequest.setId(usersSub.getId());
        subscriptionRequest.setType(usersSub.getType());

        return subscriptionRequest;
    }

    @Transactional
    public void deleteSubscription(Long userId, Long subId) {
        Optional<User> userOpt = userRepository.findById(userId);
        userOpt.ifPresent(user -> {
            user.setSubscription(null);
            userRepository.save(user);
            logger.info("Удалена подписка у юзера с id " + user.getId());
        });

        Optional<Subscriptions> subOpt = subscriptionsRepository.findById(subId);
        subOpt.ifPresent(sub -> {
            sub.setSubscribersCounter(Math.max(0, sub.getSubscribersCounter() - 1));
            subscriptionsRepository.save(sub);
            logger.info("В таблице отобразились изменения после удаления подписки у юзера.");
        });

        SubscriptionRelevance subRel = subscriptionRelevanceRepository
                .findByUserIdAndSubId(userId, subId).get();

        subscriptionRelevanceRepository.delete(subRel);
    }

    public Map<String, Subscriptions> getTop3Subscriptions(){
        Map<String, Subscriptions> topSubsMap = new HashMap<>();
        List<Subscriptions> subsList = sortSubscriptionsBySubscribersCounter(subscriptionsRepository.getTop3Subs()
                .orElse(null));

        for (int i = 0; i < subsList.size(); ++i){
            logger.info("Top subscriptions: " + subsList.get(i).toString());
        }

        subsList.forEach(subscription -> {
            topSubsMap.put("1-е место: " + (subsList.indexOf(subscription) + 1),
                    subscription);
        });

        return topSubsMap;
    }

    private List<Subscriptions> sortSubscriptionsBySubscribersCounter(List<Subscriptions> subscriptions) {
        return subscriptions.stream()
                .sorted((s1, s2) -> Integer.compare(s2.getSubscribersCounter(), s1.getSubscribersCounter()))
                .collect(Collectors.toList());
    }


    //Проверка наличия пользователя
    private User validateAndGetUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
    }

    //Проверка на наличие у пользователя подписки
    private void validateUserHasNoSubscription(User user) {
        if (user.getSubscription() != null) {
            throw new UserAlreadyHaveSubscriptionException(user.getSubscription());
        }
    }

    //Сохранение подписки или поиск имеющейся
    private Subscriptions findOrCreateSubscription(String subType) {
        Optional<Subscriptions> optionalSubscription = subscriptionsRepository.findByType(subType);
        if (optionalSubscription.isPresent()) {
            Subscriptions subscription = optionalSubscription.get();
            subscription.setSubscribersCounter(subscription.getSubscribersCounter() + 1);
            logger.info("Пользователю присвоена имеющаяся подписка.");
            return subscriptionsRepository.save(subscription);
        }

        logger.info("Создана новая подписка типа {}", subType);
        Subscriptions newSubscription = new Subscriptions();
        newSubscription.setType(subType);
        newSubscription.setSubscribersCounter(1);
        return subscriptionsRepository.save(newSubscription);
    }

    //Обновление таблицы с документацией подписок пользователей
    private void createSubscriptionRelevance(User user, Subscriptions subscription) {
        if (subscriptionRelevanceRepository.findByUserIdAndSubId(user.getId(), subscription.getId()).isPresent()) {
            logger.info("Подписка уже существует: "
                    + subscriptionRelevanceRepository.findByUserIdAndSubId(user.getId(), subscription.getId()).get().toString());
            throw new SubscriptionAlreadyExistsException();
        }

        SubscriptionRelevance relevance = new SubscriptionRelevance();
        relevance.setUser_id(user.getId());
        relevance.setSub_id(subscription.getId());
        relevance.setEnd_time(LocalDateTime.now().plusMinutes(60));

        subscriptionRelevanceRepository.save(relevance);
        logger.info("Сохранена новая сущность в таблицу с аналитикой подписок юзеров: "
                + relevance.toString());
    }
}