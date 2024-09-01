package com.sample.createaccount.services;

import com.sample.createaccount.model.Response;
import com.sample.createaccount.model.User;
import java.util.Map;

public interface UserManagementService {

    Response registerUser(User user);
    Response getAllUsers();
    Response getUser(long id);
    Response deleteUser(long id);
    Response updateUser(long id, User user);
    Response updateUserFields(long id, Map<String,Object> fields);
}
