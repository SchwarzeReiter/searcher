package com.spd.test.google_clone.Controllers;

import com.spd.test.google_clone.errors.HttpNotFountException;
import com.spd.test.google_clone.errors.RepositoryException;
import com.spd.test.google_clone.model.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class WebControllers {
    private final WebIndexer webIndexer;
    private final IndexAndSearch repository;
    private static final String ERROR_PAGE = "error";

    @GetMapping("/")
    public String search() {
        return "search";
    }

    @PostMapping("/index")
    public String index(@RequestParam(name = "q") String webSite, @RequestParam(name = "d", defaultValue = "3") String depth,
                        Model model) throws IOException {
        try {
            webIndexer.indexTheSite(webSite, Integer.parseInt(depth));
            model.addAttribute("query", webSite);
            return "index";
        } catch (HttpNotFountException e) {
            model.addAttribute(ERROR_PAGE, e.getMessage());
            return ERROR_PAGE;
        }
    }

    @GetMapping("/index")
    public String index() {
        return "indexPost";
    }

    @GetMapping("/search")
    public String search(@RequestParam(name = "q") String query,
                         @RequestParam(name = "sort", defaultValue = "RELEVANT") SortType type, Model model) {
        List<WebPage> result;
        try {
            if(query.isEmpty())
            {
                return "empty";
            }
            result = repository.searchQuery(query, type);
            if (result.isEmpty()) {
                return "empty";
            }
            model.addAttribute("result", result);
            model.addAttribute("query", query);
            model.addAttribute("sort", String.valueOf(type));
        } catch (RepositoryException e) {
            model.addAttribute(ERROR_PAGE, e.getMessage());
            return ERROR_PAGE;
        }
        return "searching";
    }
}
