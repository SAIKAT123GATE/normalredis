package com.example.redisdemo.controller;

import com.example.redisdemo.config.RedisDemoConfig;
import com.example.redisdemo.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.example.redisdemo.repository.UserRepository;
import redis.clients.jedis.Jedis;

import java.util.*;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/redis")
public class Usercontroller {
    @Autowired
    UserRepository repository;
    @Autowired
    RedisDemoConfig redisDemoConfig;


    @PostMapping
    @CachePut(value = "user", key = "#user.id")
    public User addNewUser(@RequestBody User user) {
        /* Code Level Write Behind */
        CompletableFuture.runAsync(()->{
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            repository.save(user);
        });
        return user;
    }

    @GetMapping("/getUserById/{id}")
    @Cacheable(value = "user", key = "#id")
    public User getUserById(@PathVariable("id") String id){
        return  repository.findById(id)!=null? repository.findById(id).get():null;
    }

    @PostMapping("/update")
    @CachePut(value = "user", key = "#user.id")
    public User updateUser(@RequestBody User user) {
        return repository.save(user);
    }

    @GetMapping("/delete")
    @CacheEvict(value = "user", key = "#id")
    public String deleteUserById(@RequestParam String id) {
        repository.deleteById(id);
        return "Successfully Deleted";
    }
    @GetMapping("/saveAllKeys")
    public String saveAllKeyValues() {
        CompletableFuture.runAsync(()->{
            Map messageGroupMetadata = redisDemoConfig.redisTemplate().opsForHash().entries("");
            System.out.println(messageGroupMetadata);
//            Set<String> redisKeys = redisDemoConfig.redisTemplate().keys("*");
//// Store the keys in a List
//            List<String> keysList = new ArrayList<>();
//            Iterator<String> it = redisKeys.iterator();
//            while (it.hasNext()) {
//                String data = it.next();
//                keysList.add(data);
//            }
//
//            System.out.println("Here=>"+Arrays.toString(keysList.toArray()));
        });

        return "Saved SuccessFully";
    }
}
