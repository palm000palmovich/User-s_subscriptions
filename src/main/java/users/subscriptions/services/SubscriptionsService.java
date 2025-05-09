package users.subscriptions.services;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import users.subscriptions.controller.UserController;
import users.subscriptions.exceptions.SubscriptionAlreadyExistsException;
import users.subscriptions.exceptions.UserAlreadyHaveSubscriptionException;
import users.subscriptions.exceptions.UserNotFoundException;
import users.subscriptions.model.SubscriptionRelevance;
import users.subscriptions.model.Subscriptions;
import users.subscriptions.model.User;
import users.subscriptions.repository.SubscriptionRelevanceRepository;
import users.subscriptions.repository.SubscriptionsRepository;
import users.subscriptions.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class SubscriptionsService {

    private Logger logger = LoggerFactory.getLogger(SubscriptionsService.class);
    private final SubscriptionsRepository subscriptionsRepository;
    private final UserRepository userRepository;
    private final SubscriptionRelevanceRepository subscriptionRelevanceRepository;

    public SubscriptionsService(SubscriptionsRepository subscriptionsRepository,
        UserRepository userRepository,
        SubscriptionRelevanceRepository subscriptionRelevanceRepository){
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