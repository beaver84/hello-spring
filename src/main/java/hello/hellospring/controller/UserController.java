package hello.hellospring.controller;

import hello.hellospring.dto.ResponseDTO;
import hello.hellospring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/api/v1/app/")
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping(value = "findAll", method = RequestMethod.POST)
    public ResponseEntity<?> findAll() {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setResultCode("S0001");
        responseDTO.setRes(userService.findAll());
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }

    @RequestMapping(value = "findById/{id}", method = RequestMethod.GET)
    public ResponseEntity<?> findAll(
        @PathVariable(name = "id")
        long id
    ) {
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setResultCode("S0001");
        responseDTO.setRes(userService.findById(id));
        return new ResponseEntity<>(responseDTO, HttpStatus.OK);
    }
}
