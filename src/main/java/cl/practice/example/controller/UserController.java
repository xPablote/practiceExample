package cl.practice.example.controller;

import cl.practice.example.dto.UserDto;
import cl.practice.example.entity.User;
import cl.practice.example.response.GenericResponse;
import cl.practice.example.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/users")
@CrossOrigin(origins = "*", methods= {RequestMethod.GET,RequestMethod.POST})
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private UserService userService;

    @Operation(summary = "Crea un usuario")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Crea un usuarios correctamente"),
            @ApiResponse(responseCode = "409", description = "Creacion de usuario sin exito")
    })
    @PostMapping("/createUser")
    public ResponseEntity<?> createUser(
            @Validated  @RequestBody UserDto user) throws Exception {
        try {
            User resp = userService.createUser(user);
            return new ResponseEntity<>(
                    new GenericResponse<>( "Usuario creado con éxito", resp),
                    HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new GenericResponse<>( e.getMessage()),
                    HttpStatus.CONFLICT
            );
        }
    }
    @Operation(summary = "Buscar un usuario por email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Email encontrado"),
            @ApiResponse(responseCode = "404", description = "Email no encontrado")
    })
    @GetMapping("/byEmail")
    public ResponseEntity<?> getUserByEmail(@RequestParam String email) {
        try {
            User user = userService.getUserByEmail(email);
            return new ResponseEntity<>(
                    new GenericResponse<>( "Usuario encontrado por email: ", user),
                    HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(new GenericResponse<>( e.getMessage()),
                    HttpStatus.NOT_FOUND
            );
        }
    }

}
