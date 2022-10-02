package hello.hellospring.service;

import hello.hellospring.domain.TbUser;
import hello.hellospring.mapper.UserMapper;
import hello.hellospring.repository.jpa.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserRepository userRepository;

    public ArrayList<HashMap<String, Object>> findAll() {
        return userMapper.findAll();
    }


    public Object findById(long id) {
        return userRepository.findById(id);
    }
}