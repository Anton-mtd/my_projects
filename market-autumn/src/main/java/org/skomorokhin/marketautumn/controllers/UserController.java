package org.skomorokhin.marketautumn.controllers;


import lombok.RequiredArgsConstructor;
import org.skomorokhin.marketautumn.dto.JwtRequest;
import org.skomorokhin.marketautumn.dto.JwtResponse;
import org.skomorokhin.marketautumn.dto.RegistRequest;
import org.skomorokhin.marketautumn.exceptions.AppError;
import org.skomorokhin.marketautumn.model.entities.Cart;
import org.skomorokhin.marketautumn.model.entities.User;
import org.skomorokhin.marketautumn.services.CartService;
import org.skomorokhin.marketautumn.services.UserService;
import org.skomorokhin.marketautumn.utils.JwtTokenUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;


import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final JwtTokenUtil jwtTokenUtil;
    private final AuthenticationManager authenticationManager;

    @PostMapping("/getAuth")
    public ResponseEntity<?> createAuthToken(@RequestBody JwtRequest authRequest) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            return new ResponseEntity<>(new AppError(HttpStatus.UNAUTHORIZED.value(), "Incorrect username or password"), HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = userService.loadUserByUsername(authRequest.getUsername());
        String token = jwtTokenUtil.generateToken(userDetails);
        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping("/getRegister")
    public void registUser(@Valid @RequestBody RegistRequest registRequest) {
        userService.addNewUser(registRequest);
    }


}
