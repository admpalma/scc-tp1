package scc.rest.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ctrl")
public class DebugController {

    /**
     * This method just prints a string. It may be useful to check if the current
     * version is running on Azure.
     */
    @GetMapping("/version")
    public String hello() {
        return "v: 0001";
    }

}
