package scc.rest.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@RequestMapping("/ctrl")
public class DebugController {

    private final Environment environment;

    @Autowired
    public DebugController(Environment environment) {
        this.environment = environment;
    }

    /**
     * This method just prints a string. It may be useful to check if the current
     * version is running on Azure.
     */
    @GetMapping("/version")
    public String hello() {
        return "v: 0001";
    }

    @GetMapping("/env")
    public String env() {
        return Stream.of(
                        "azure.cosmos.database",
                        "azure.cosmos.key",
                        "azure.cosmos.uri",
                        "azure.storage.account-name",
                        "azure.storage.account-key"
                )
                .map(property -> property + "=" + environment.getProperty(property))
                .collect(Collectors.joining("\n"));
    }

}
