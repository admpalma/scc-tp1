package scc.rest.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import scc.application.services.MediaService;

@RestController
@RequestMapping("/media")
public class MediaController {

    private final Logger log = LoggerFactory.getLogger(getClass());

    private final MediaService media;

    @Autowired
    public MediaController(MediaService media) {
        this.media = media;
    }

    @PostMapping
    public String uploadMedia(@RequestBody byte[] data) {
        return media.uploadMedia(data);
    }

    @GetMapping("/{fileName}")
    public @ResponseBody byte[] downloadMedia(@PathVariable String fileName) {
        return media.downloadMedia(fileName);
    }
}
