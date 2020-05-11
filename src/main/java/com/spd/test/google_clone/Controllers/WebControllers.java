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
    private final LuceneRepository repository;

    @GetMapping("/")
    public String search()
    {
        return "search";
    }

    @PostMapping("/index")
    public String index(@RequestParam(name ="q") String webSite, @RequestParam(name = "d", defaultValue = "3") String depth, Model model) throws Exception {
        try {
            webIndexer.indexTheSite(webSite, Integer.parseInt(depth));
            model.addAttribute("query",webSite);
            return "index";
        }
         catch (HttpNotFountError e) {
         model.addAttribute("error", e.getMessage());
         return "error";
     }
    }

    @GetMapping("/index")
    public String index()
    {
        return "postIndex";
    }

    @GetMapping("/search")
    public String search(@RequestParam (name = "q",defaultValue = "") String query,
                         @RequestParam (name = "sort", defaultValue = "0") int type, Model model)
    {
        List<WebPage> result;
      try {
         result = repository.searchQuery(query,type);
          if(result.size() == 0){
              return "empty";
          }

         model.addAttribute("result", result);
         model.addAttribute("query",query);
         model.addAttribute("sort",String.valueOf(type));
     }
     catch (RepositoryError e)
     {
         model.addAttribute("error",e.getMessage());
         return "error";
     }
      return "searching";
    }

    @GetMapping("/search/sort")
    public String sort(@RequestParam  Model model)
    {
        model.addAttribute("result", repository.sort(repository.getResult(),1));
        return "searching";
    }
}
