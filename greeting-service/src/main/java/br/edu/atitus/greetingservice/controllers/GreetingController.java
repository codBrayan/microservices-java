package br.edu.atitus.greetingservice.controllers;

import br.edu.atitus.greetingservice.configs.GreetingConfig;
import br.edu.atitus.greetingservice.dtos.GreetingRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/greeting")
public class GreetingController {

//    @Value("${greeting-service.greeting}")
//    private String greeting;
//
//    @Value("${greeting-service.default-name}")
//    private String defaultName;

    private final GreetingConfig config;

    private String responseApi(String name) {
        if (name == null || name.isEmpty()) {
            name = config.getDefaultName();
        }
        return String.format("%s %s!!!", config.getGreeting(), name);
    }

    //Injeção de dependência
    public GreetingController(GreetingConfig config) {
        this.config = config;
    }

    @GetMapping({"", "/"})
    public String getGreeting(
            @RequestParam(required = false) String name) {
        if (name == null || name.isEmpty()) {
            name = config.getDefaultName();
        }
        return responseApi(name);
    }
    @GetMapping("/{name}")
    public String getGreetingPath(
            @PathVariable String name) {
        return responseApi(name);
    }
    @PostMapping
    public String postGreeting(
            @RequestBody GreetingRequest request) {
        return responseApi(request.name());
    }
}
