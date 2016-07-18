package org.retrotime.controller;

import org.retrotime.service.ContentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contents")
public class ContentController {

    @Autowired
    ContentService contentService;

    @RequestMapping(method = RequestMethod.POST)
    public int initContentUponPartDTO(@RequestParam final int retroId, final int userId) {
        return contentService.initContent(retroId, userId);
    }
}
