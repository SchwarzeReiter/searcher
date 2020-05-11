package com.spd.test.google_clone.Controllers;

import com.spd.test.google_clone.errors.HttpNotFountError;
import com.spd.test.google_clone.errors.RepositoryError;
import com.spd.test.google_clone.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class WebControllers {
    private final WebIndexer webIndexer;
    private final Repository repository;

    @GetMapping("/")
    public String search() {
        return "search";
    }

    @PostMapping("/index")
    public String index(@RequestParam(name = "q") String webSite, @RequestParam(name = "d", defaultValue = "3") String depth,
                        Model model) throws Exception {
        try {
            webIndexer.indexTheSite(webSite, Integer.parseInt(depth));
            model.addAttribute("query", webSite);
            return "index";
        } catch (HttpNotFountError e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
    }

    @GetMapping("/index")
    public String index() {
        return "indexPost";
    }

    @GetMapping("/search")
    public String search(@RequestParam(name = "q") String query,
                         @RequestParam(name = "sort", defaultValue = "0") int type, Model model) {
        List<WebPage> result;
        try {
            if(query.isEmpty())
            {
                return "empty";
            }
            result = repository.searchQuery(query, type);
            if (result.size() == 0) {
                return "empty";
            }
            model.addAttribute("result", result);
            model.addAttribute("query", query);
            model.addAttribute("sort", String.valueOf(type));
        } catch (RepositoryError e) {
            model.addAttribute("error", e.getMessage());
            return "error";
        }
        return "searching";
    }
}
