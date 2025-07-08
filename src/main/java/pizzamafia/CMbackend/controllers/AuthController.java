package pizzamafia.CMbackend.controllers;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import pizzamafia.CMbackend.payloads.user.NewUserDTO;
import pizzamafia.CMbackend.payloads.user.NewUserRespDTO;
import pizzamafia.CMbackend.payloads.user.UserLoginDTO;
import pizzamafia.CMbackend.payloads.user.UserLoginRespDTO;
import pizzamafia.CMbackend.services.UserService;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private UserService userService;

    //Registrazione
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public NewUserRespDTO registerUser(@RequestBody @Valid NewUserDTO newUserDTO) {
        return userService.save(newUserDTO);
    }

    //Login
    @PostMapping("/login")
    public UserLoginRespDTO login(@RequestBody @Valid UserLoginDTO userLoginDTO) {
        return userService.login(userLoginDTO);
    }
}



