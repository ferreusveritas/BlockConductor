package com.ferreusveritas.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("blocks")
public class TestController {

    @GetMapping(path="/{id}", produces = "application/json")
    public @ResponseBody String getBlocks(@PathVariable int id) {
        return findBlockByPos(id);
    }

    private String findBlockByPos(int id) {
        return "Test :" + id;
    }
}