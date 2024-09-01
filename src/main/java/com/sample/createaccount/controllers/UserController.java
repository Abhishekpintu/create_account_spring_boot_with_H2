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

    @GetMapping("/{id}")
    public ResponseEntity<Response> getUser(@PathVariable long id){
        return ResponseEntity.status(HttpStatus.OK).body(userManagementService.getUser(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Response> deleteUser(@PathVariable long id){
        return ResponseEntity.status(HttpStatus.OK).body(userManagementService.deleteUser(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Response> updateUser(@PathVariable int id,@Valid @RequestBody User user){
        return ResponseEntity.status(HttpStatus.OK).body(userManagementService.updateUser(id,user));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Response> updateUserFields(@PathVariable int id,@RequestBody Map<String, Object> fields){
        return ResponseEntity.status(HttpStatus.OK).body(userManagementService.updateUserFields(id,fields));
    }
}
