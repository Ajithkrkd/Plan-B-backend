package com.ajith.userservice.user.service;

import com.ajith.userservice.config.JwtService;
import com.ajith.userservice.user.dto.ChangePasswordRequest;
import com.ajith.userservice.user.model.Role;
import com.ajith.userservice.user.model.User;
import com.ajith.userservice.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class UserServiceTest {

    @InjectMocks
    private UserService userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @BeforeEach
    void setup() throws Exception {
        MockitoAnnotations.openMocks ( this );
        passwordEncoder = new BCryptPasswordEncoder (  );
        jwtService = new JwtService ( userRepository );
    }

    @Test
    void findByEmail ( ) {
        //Arrange
        String hashedPassword = passwordEncoder.encode ( "1234" );
        String email = "test@gmail.com";
        User expectedUser =  User.builder ()
                .email ( "test@gmail.com" )
                .password ( hashedPassword)
                .id ( 12L )
                .isBlocked ( false )
                .fullName ( "testpedal" )
                .profileImage ( null )
                .isEmailVerified ( true )
                .verificationToken ( null )
                .joinDate ( new Date (  ) )
                .phoneNumber ( "8098098098" )
                .role ( Role.USER )
                .build ();

        //Act
        Mockito.when ( userRepository.findByEmail ( email ) ).thenReturn ( Optional.ofNullable ( expectedUser ) );
        Optional < User > foundUser = userService.findByEmail ( email );
        System.out.println (foundUser.toString () );

        //Assert
        assertTrue ( expectedUser.getEmail () == foundUser.get ().getEmail () );
        assertTrue ( passwordEncoder.matches ( foundUser.get ().getPassword (), passwordEncoder.encode ( expectedUser.getPassword () ) ) );
        assertNotNull (foundUser.get().getId ());
    }

//    @Test
//    void changePassword(){
//        //Arrange
//        String authHeader = "Bearer " +"eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJVU0VSIl0sInN1YiI6ImFqaXRoMjI1NWl0aUBnbWFpbC5jb20iLCJpYXQiOjE3MTA2NzM5MTAsImV4cCI6MTcxMDc2MDMxMH0.L6gvJR4PZidyOLuZ-LgcZ_qltkYvxrBS9LeTm8tinnk";
//        ChangePasswordRequest request = new ChangePasswordRequest ("1234","ajith123");
//
//        String hashedPassword = passwordEncoder.encode ( "1234" );
//        User expectedUser =  User.builder ()
//                .email ( "test@gmail.com" )
//                .password ( hashedPassword)
//                .id ( 12L )
//                .isBlocked ( false )
//                .fullName ( "testpedal" )
//                .profileImage ( null )
//                .isEmailVerified ( true )
//                .verificationToken ( null )
//                .joinDate ( new Date (  ) )
//                .phoneNumber ( "8098098098" )
//                .role ( Role.USER )
//                .build ();
//
//        //Act
//
//        Mockito.when ( jwtService.findUserWithAuthHeader ( authHeader ) ).thenReturn ( Optional.ofNullable ( expectedUser ) );
//        User user = userRepository.save ( expectedUser );
//        userService.changePassword ( authHeader,request );
//        //Assert
//
//        assertTrue (user.getPassword () == expectedUser.getPassword() );
//    }

}