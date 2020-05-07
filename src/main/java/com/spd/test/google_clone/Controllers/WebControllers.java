package com.spd.test.google_clone.Controllers;


import com.spd.test.google_clone.model.CrawlerConfiguration;
import com.spd.test.google_clone.model.LuceneEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class WebControllers {
  private final CrawlerConfiguration controller;
  private final LuceneEntity luceneEntity;
    @GetMapping("/")
    public String search()
    {
        return "search";
    }

    @PostMapping("/index")
    public String index(@RequestParam(name ="q") String query, Model model) throws Exception {
        controller.crawler(query);
        model.addAttribute("query",query);
        return "index";
    }

    @GetMapping("/index")
    public String index()
    {
        return "index2";
    }

    @GetMapping("/search")
    public String search(@RequestParam (name = "q")String query,Model model)
    {
        model.addAttribute("result", luceneEntity.Searching(query));
        return "searching";
    }
}
