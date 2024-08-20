package uno.dos.tres.controller;

import jakarta.servlet.ServletContext;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.StringTrimmerEditor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import uno.dos.tres.beans.*;
import uno.dos.tres.service.IslandService;
import uno.dos.tres.service.ServiceException;
import uno.dos.tres.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

@Controller
@SessionAttributes({"user", "authInfo", "island"})
public class MainController {

    @InitBinder
    public void initBinder(WebDataBinder dataBinder) {

        StringTrimmerEditor stringTrimmerEditor = new StringTrimmerEditor(true);
        dataBinder.registerCustomEditor(String.class, stringTrimmerEditor);
    }

    @ModelAttribute("user")
    public User getUser() {
        return new User();
    }

    @ModelAttribute("authInfo")
    public AuthInfo getAuthInfo() {
        return new AuthInfo();
    }

    @ModelAttribute("island")
    public Island getIsland() {
        return new Island();
    }

    @Autowired
    private MessageSource messageSource;
    @Autowired
    private IslandService islandService;
    @Autowired
    private UserService userService;

    @RequestMapping("/add_island")
    public String addIsland(@ModelAttribute("island") Island island, Model model, HttpSession session, Locale locale) {
        User user = (User) session.getAttribute("authenticatedUser");
        Date date = new Date();
        island.setUser(user);
        island.setDate(date);
        try {
            islandService.addIsland(island);
            Set<UserRoles> roles = user.getRoles();
            roles.add(UserRoles.OWNER);
            userService.updateUser(user);
        } catch (ServiceException e) {
            model.addAttribute("AddInfo", messageSource.getMessage("add_island_error", null, locale));
            return "redirect:/pagination";
        }
        return "redirect:/pagination";
    }


    @RequestMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response, Model model) {
        HttpSession session = request.getSession(false);
        session.removeAttribute("authenticatedUser");
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("remember-me")) {
                    cookie.setValue("");
                    cookie.setPath("/");
                    cookie.setMaxAge(0);
                    response.addCookie(cookie);
                }
            }
        }
        return "redirect:/";
    }

    @RequestMapping("/add_island_page")
    public String addIslandPage(Model model, Locale locale) {
        try {
            Set<Country> countries = islandService.findAllCountries();
            model.addAttribute("countries", countries);
            model.addAttribute("AddInfo", messageSource.getMessage("add_island_welcome", null, locale));
        } catch (ServiceException e) {
            model.addAttribute("MainInfo", messageSource.getMessage("go_to_addIsland_page", null, locale));
            return "redirect:/pagination";
        }
        return "add_island";
    }

    @RequestMapping("/pagination")
    public String pagination(Model model, @RequestParam(defaultValue = "0") int page, HttpServletRequest request, Locale locale) {
        int pageSize = 6;

        try {
            List<Island> allIslands = islandService.findAllIslands();
            int totalIslands = allIslands.size();
            int totalPages = (int) Math.ceil((double) totalIslands / pageSize);
            int startItem = page * pageSize;

            List<Island> islands;
            if (totalIslands < startItem) {
                islands = Collections.emptyList();
            } else {
                int toIndex = Math.min(startItem + pageSize, totalIslands);
                islands = allIslands.subList(startItem, toIndex);
            }
            model.addAttribute("islands", islands);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", totalPages);

            return "main";
        } catch (ServiceException e) {
            model.addAttribute("Info", messageSource.getMessage("error", null, locale));
            return "main";
        }
    }

    @RequestMapping("/my_islands")
    public String myIslands(HttpSession session, Model model, Locale locale) {
        User user = (User) session.getAttribute("authenticatedUser");
        try {
            List<Island> islands = islandService.findIslandsByUserId(user.getId());

            for (Island island : islands) {
                if (island.getImage() != null) {
                    String absolutePath = island.getImage().getImagePath();

                    System.out.println("Absolute Path: " + absolutePath);

                    String relativePath = absolutePath.replace("C:\\Users\\Msi-G6547\\Desktop\\TC\\apache-tomcat-10.1.19\\webapps\\IslandPJ_war", "")
                            .replace("\\", "/");

                    if (!relativePath.startsWith("/")) {
                        relativePath = "/" + relativePath;
                    }

                    System.out.println("Relative Path: " + relativePath);

                    island.getImage().setImagePath(relativePath);
                }
            }

            session.setAttribute("myIslands", islands);
        } catch (ServiceException e) {
            model.addAttribute("Info", messageSource.getMessage("error", null, locale));
            return "main";
        }
        return "my_islands_page";
    }

    @RequestMapping("/island_card")
    public String islandCard(@RequestParam("id") int islandId, HttpSession session, Model model, Locale locale) {
        Island island = null;
        try {
            island = islandService.findIslandById(islandId);
            Set<Country> countries = islandService.findAllCountries();
            model.addAttribute("island", island);
            model.addAttribute("countries", countries);
        } catch (ServiceException e) {
            model.addAttribute("Info", messageSource.getMessage("error", null, locale));
            return "island_card";
        }
        return "island_card";
    }

    @RequestMapping("/update_island")
    public String updateIsland(@ModelAttribute("island") Island island, Model model, Locale locale) {

        try {
            System.out.println("island!!!!!!!!!!!!!: " + island);
            islandService.updateIsland(island);
        } catch (ServiceException e) {
            model.addAttribute("Info", messageSource.getMessage("error", null, locale));
            return "island_card";
        }
        return "redirect:/pagination";
    }

    @Autowired
    private ServletContext servletContext;

    @RequestMapping("/uploadImage")
    public String uploadIslandImage(@RequestParam("image") MultipartFile image,
                                    @RequestParam("islandId") int islandId,
                                    Model model, Locale locale) {
        if (!image.isEmpty()) {
            try {
                String uploadDir = servletContext.getRealPath("/resources/uploads/");

                File directory = new File(uploadDir);

                if (!directory.exists()) {
                    directory.mkdirs();
                }

                String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();

                File destinationFile = new File(uploadDir, fileName);

                image.transferTo(destinationFile);

                System.out.println("Saving file to: " + destinationFile.getAbsolutePath());

                try {
                    Island island = islandService.findIslandById(islandId);

                    IslandImage islandImage = new IslandImage();
                    islandImage.setImagePath("/resources/uploads/" + fileName);
                    islandImage.setIsland(island);

                    island.setImage(islandImage);

                    islandService.updateIsland(island);

                } catch (ServiceException e) {
                    model.addAttribute("Info", messageSource.getMessage("error", null, locale));
                    return "main";
                }

            } catch (IOException e) {
                model.addAttribute("message", "Failed to upload image.");
                return "redirect:/pagination";
            }
        }
        return "main";
    }

    @RequestMapping("/buy_islands")
    public String buyIsland(@RequestParam("id") int islandId, Model model, HttpSession session, Locale locale) {
        try {
            User user = (User) session.getAttribute("authenticatedUser");
            Island island = islandService.findIslandById(islandId);
            island.setUser(user);
            islandService.updateIsland(island);
            Set<UserRoles> roles = user.getRoles();
            roles.add(UserRoles.OWNER);
            userService.updateUser(user);
        } catch (ServiceException e) {
            model.addAttribute("Info", messageSource.getMessage("error", null, locale));
            return "main";
        }

        return "redirect:/pagination";
    }

}