package com.sample.createaccount.controllers;

import com.sample.createaccount.model.Response;
import com.sample.createaccount.model.User;
import com.sample.createaccount.services.UserManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserManagementService userManagementService;

    @Operation(summary = "Create a new user",
            description = "Create a new user account")
    @ApiResponse(responseCode = "200", description = "Create a new user account")
    @PostMapping("/")
    public ResponseEntity<Response> registerUser(@Valid @RequestBody User user){
        return ResponseEntity.status(HttpStatus.OK).body(userManagementService.registerUser(user));
    }

    @Operation(summary = "Fetch all users from DB",
            description = "Fetch all users from DB")
    @ApiResponse(responseCode = "200", description = "Fetch all users from DB")
    @GetMapping("/")
    public ResponseEntity<Response> getAllUsers(){
        return ResponseEntity.status(HttpStatus.OK).body(userManagementService.getAllUsers());
    }

    @Operation(summary = "Fetch user based on user id",
            description = "Fetch user based on user id")
    @ApiResponse(responseCode = "200", description = "Fetch user based on user id")
    @GetMapping("/{id}")
    public ResponseEntity<Response> getUser(@PathVariable long id){
        return ResponseEntity.status(HttpStatus.OK).body(userManagementService.getUser(id));
    }

    @Operation(summary = "Delete user based on user id",
            description = "Delete user based on user id")
    @ApiResponse(responseCode = "200", description = "Delete user based on user id")
    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteUser(@PathVariable long id){
        return ResponseEntity.status(HttpStatus.OK).body(userManagementService.deleteUser(id));
    }

    @Operation(summary = "Update user based on user id",
            description = "Update user based on user id")
    @ApiResponse(responseCode = "200", description = "Delete user based on user id")
    @PutMapping("/{id}")
    public ResponseEntity<Response> updateUser(@PathVariable int id,@Valid @RequestBody User user){
        return ResponseEntity.status(HttpStatus.OK).body(userManagementService.updateUser(id,user));
    }

    @Operation(summary = "Update user fields based on user id",
            description = "Update user fields based on user id")
    @ApiResponse(responseCode = "200", description = "Delete fields user based on user id")
    @PatchMapping("/{id}")
    public ResponseEntity<Response> updateUserFields(@PathVariable int id,@RequestBody Map<String, Object> fields){
        return ResponseEntity.status(HttpStatus.OK).body(userManagementService.updateUserFields(id,fields));
    }
}
