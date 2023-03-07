package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    User add(User user);

    void remove(Integer id);

    User update(User user);

    List<User> getAll();

    User getById(int id);
}
