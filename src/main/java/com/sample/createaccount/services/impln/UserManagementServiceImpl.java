package com.sample.createaccount.services.impln;

import com.sample.createaccount.common.Constants;
import com.sample.createaccount.configurations.CustomErrorException;
import com.sample.createaccount.model.Response;
import com.sample.createaccount.model.User;
import com.sample.createaccount.repo.UsersDAO;
import com.sample.createaccount.services.UserManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;
import java.lang.reflect.Field;
import java.util.*;

@Service
public class UserManagementServiceImpl implements UserManagementService {

    @Autowired
    UsersDAO usersDAO;

    /**
     * Register user implementation method
     * @param user obj
     * @return Response obj
     */
    @Override
    public Response registerUser(User user) {
        User fetchedUser = usersDAO.findByEmailId(user.getEmailId());
        if (fetchedUser != null) {
            throw new CustomErrorException(Constants.EMAIL_EXISTS);
        } else {
            if(user.getPassword()!=null && !user.getPassword().isEmpty()) {
                user.setEncodedPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
                usersDAO.saveAndFlush(user);
                return Response.successResponse(null, Constants.REG_SUCCESS);
            }
            else{
                throw new CustomErrorException(Constants.EMPTY_PASSSWORD);
            }
        }
    }

    /**
     * To get all registered user method
     * @return Response obj with data array of users
     */
    @Override
    public Response getAllUsers()  {
        return Response.successResponse(usersDAO.findAll(),Constants.FETCHED_SUCCESS);
    }

    /**
     * Get a user by id method
     * @param id userID
     * @return object of user based on user id
     */
    @Override
    public Response getUser(long id) {
        return Response.successResponse(usersDAO.findById(id),Constants.FETCHED_SUCCESS);
    }

    /**
     *
     * @param id user id
     * @return Response obj success message
     */
    @Override
    public Response deleteUser(long id)  {
            User user = fecthUser(id);
            usersDAO.delete(user);
            return Response.successResponse(null,Constants.DELETE_SUCCESS);
    }

    /**
     * Method to fetch the user by iserid
     * @param id user is
     * @return user obj
     */
    private User fecthUser(long id){
        Optional<User> userObj = usersDAO.findById(id);
        if(userObj.isPresent()){
            return userObj.get();
        }
        else{
            throw new CustomErrorException(Constants.MISSING_USER);
        }
    }

    /**
     * @param id user id
     * @return response obj
     */
    @Override
    public Response updateUser(long id, User user)  {
        User existingUserObj = fecthUser(id);
        existingUserObj.setFullName(user.getFullName());
        existingUserObj.setEmailId(user.getEmailId());
        existingUserObj.setIsTermsAndConditionsAgreed(user.getIsTermsAndConditionsAgreed());
        usersDAO.saveAndFlush(existingUserObj);
        return Response.successResponse(null,Constants.UPDATED_SUCCESS);
    }

    /**
     *
     * @param id user id
     * @param fields - fields to update
     * @return success message
     */
    @Override
    public Response updateUserFields(long id, Map<String,Object> fields)  {
        User existingUserObj = fecthUser(id);
        fields.forEach((key, value) -> {
                Field field = ReflectionUtils.findField(User.class, key);
                field.setAccessible(true);
                ReflectionUtils.setField(field, existingUserObj, value);
            });
             usersDAO.saveAndFlush(existingUserObj);
             return Response.successResponse(null,Constants.UPDATED_SUCCESS);
        }
}
