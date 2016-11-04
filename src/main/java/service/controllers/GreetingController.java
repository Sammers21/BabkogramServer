package service.controllers;

import java.util.concurrent.atomic.AtomicLong;

import org.apache.log4j.Logger;
import service.objects.Greeting;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GreetingController {

    public static final Logger log=Logger.getLogger(GreetingController.class.getName());

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();




    @RequestMapping("/greeting")
    public Greeting greeting(@RequestParam(value="name", defaultValue="World") String name) {

        log.info("someonce call greeting");

        return new Greeting(counter.incrementAndGet(),
                String.format(template, name));
    }
}