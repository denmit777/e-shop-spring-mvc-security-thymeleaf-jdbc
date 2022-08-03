package eshop.service;

import eshop.dto.UserDto;

public interface UserService {

    void save(UserDto userDto);

    boolean isInvalidUser(UserDto userDto);

    String invalidUser(UserDto userDto);
}
