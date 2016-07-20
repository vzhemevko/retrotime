package org.retrotime.controller;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by vzhemevko on 03.09.15.
 */
@Controller
@Scope("session")
public class PageController {

    @RequestMapping("/*")
    public String getPage() {
        return "redirect:/resources/index.html";
    }
}
