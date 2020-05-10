package com.spd.test.google_clone.Controllers;


import com.spd.test.google_clone.model.LuceneRepository;
import com.spd.test.google_clone.model.SearchRepository;
import com.spd.test.google_clone.model.WebIndexer;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
public class WebControllers {
    private final WebIndexer webIndexer;
    private final LuceneRepository searchRepository;

    @GetMapping("/")
    public String search()
    {
        return "search";
    }

    @PostMapping("/index")
    public String index(@RequestParam(name ="q") String webSite, @RequestParam(name = "d", defaultValue = "3") String depth, Model model) throws Exception {
        webIndexer.index(webSite,Integer.parseInt(depth));
        model.addAttribute("query",webSite);
        return "index";
    }

    @GetMapping("/index")
    public String index()
    {
        return "postindex";
    }

    @GetMapping("/search")
    public String search(@RequestParam (name = "q")String query,Model model)
    {
        model.addAttribute("result", searchRepository.searchQuery(query));
        return "searching";
    }
}
