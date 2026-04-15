package org.studyeasy.springstrater.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.studyeasy.springstrater.Model.Post;
import org.studyeasy.springstrater.Services.PostService;

@Controller
public class HomeController {

    @Autowired
    private PostService postService;

    @GetMapping("/")
    public String home(
            Model model,
            @RequestParam(defaultValue = "1", value = "pageNumber") int pageNumber,
            @RequestParam(defaultValue = "5", value = "pageSize") int pageSize,
            @RequestParam(defaultValue = "createdAt", value = "sortBy") String sortBy) {

        int offset = pageNumber - 1;

        Page<Post> postPage = postService.findAll(offset, pageSize, sortBy);

        int totalPages = postPage.getTotalPages();

        List<Integer> pages = createPageRange(totalPages);

        model.addAttribute("posts", postPage.getContent());
        model.addAttribute("pages", pages);

        model.addAttribute("currentPage", pageNumber);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("sortBy", sortBy);

        model.addAttribute("totalPages", totalPages);

        return "home_views/home";
    }

    // Creates: [0,1,2,3,...]
    private List<Integer> createPageRange(int totalPages) {
        List<Integer> pages = new ArrayList<>();
        for (int i = 0; i < totalPages; i++) {
            pages.add(i);
        }
        return pages;
    }
}