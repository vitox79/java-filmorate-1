package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;
import ru.yandex.practicum.filmorate.throwable.IncorrectCountException;
import ru.yandex.practicum.filmorate.throwable.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {
    @Qualifier("userDbStorage")
    private final UserStorage userStorage;

    public User createUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        return userStorage.add(user);
    }

    public User updateUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        Optional<User> userOptional = userStorage.update(user);
        return userOptional.orElseThrow(() -> new NotFoundException("Такого пользователя нет в списке зарегистрированых."));
    }

    public List<User> getUsers() {
        return userStorage.getAll();
    }

    public User getUser(int id) {
        Optional<User> userOpt = userStorage.getById(id);
        if (userOpt.isPresent()) {
            return userOpt.get();
        } else {
            if (id < 0) {
                throw new IncorrectCountException("id не должно быть меньше 0.");
            } else {
                throw new NotFoundException("Пользователя с указанным id - не существует.");
            }
        }
    }

    public void addFriend(Integer userId, Integer friendId) {
        getUser(userId);
        getUser(friendId);
        userStorage.addFriend(userId, friendId);
    }

    public void removeFriend(Integer userId, Integer friendId) {
        getUser(userId);
        getUser(friendId);
        userStorage.removeFriend(userId, friendId);
    }

    public List<User> getFriends(Integer userId) {
        return userStorage.getFriends(userId);
    }

    public List<User> getCommonFriends(Integer userId, Integer otherId) {
        getUser(userId);
        getUser(otherId);
        return userStorage.getCommonFriends(userId, otherId);
    }
}