package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.throwable.NotFoundException;

import java.util.*;

@Component("inMemoryUserStorage")
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private Integer id = 1;

    @Override
    public User add(User user) {
        if (!users.containsKey(user.getId())) {
            user.setId(id++);
            users.put(user.getId(), user);
        }
        return user;
    }

    @Override
    public void remove(Integer id) {
        users.remove(id);
    }

    @Override
    public Optional<User> update(User user) {
        if (!users.containsKey(user.getId())) {
            throw new NotFoundException("Такого пользователя нет в списке зарегистрированых.");
        } else {
            users.put(user.getId(), user);
        }
        return Optional.ofNullable(user);
    }

    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }

    @Override
    public Optional<User> getById(Integer id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public void addFriend(Integer userId, Integer friendId) {
        Optional<User> userOpt = getById(userId);
        Optional<User> friendOpt = getById(friendId);
        if (userOpt.isPresent() && friendOpt.isPresent()) {
            userOpt.get().getFriends().add(friendOpt.get().getId());
        }
    }

    @Override
    public List<User> getFriends(Integer id) {
        List<User> friends = new ArrayList<>();
        for (Integer friendsId : users.get(id).getFriends()) {
            friends.add(users.get(friendsId));
        }
        return friends;
    }

    @Override
    public List<User> getCommonFriends(Integer userId, Integer otherId) {
        List<User> commonFriends = new ArrayList<>();
        for (Integer friendsId : users.get(userId).getFriends()) {
            if (users.get(otherId).getFriends().contains(friendsId)) {
                commonFriends.add(users.get(friendsId));
            }
        }
        return commonFriends;
    }

    @Override
    public void removeFriend(Integer userId, Integer friendId) {
        users.get(userId).getFriends().remove(friendId);
        users.get(friendId).getFriends().remove(userId);
    }
}
