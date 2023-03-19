package ru.fateyev.library.controllers;

import javax.validation.Valid;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.fateyev.library.models.Book;
import ru.fateyev.library.models.Person;
import ru.fateyev.library.services.BookService;
import ru.fateyev.library.services.PersonService;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/books")
public class BooksController {

   private final BookService bookService;
   private final PersonService personService;

   @Autowired
    public BooksController(BookService bookService, PersonService personService) {
        this.bookService = bookService;
        this.personService = personService;
    }

    @GetMapping()
    public String index(Model model,
                        @RequestParam(name = "page",  required = false, defaultValue = "0") @Min(0) Integer page,
                        @RequestParam(name = "books_per_page",  required = false, defaultValue = "20") @Min(1) @Max(100) Integer size,
                        @RequestParam(name = "sort_by_year", required = false, defaultValue = "false") boolean sort){

        model.addAttribute("books", bookService.findPaginated(page, size, sort));
        return "books/index";
    }

    @GetMapping("/{id}")
    public String show(@PathVariable("id") int id, Model model, @ModelAttribute("person") Person person){
        model.addAttribute("book", bookService.findOne(id));

        Optional<Person> owner = bookService.getOwner(id);
        if(owner.isPresent())
            model.addAttribute("owner", owner.get());
        else
            model.addAttribute("people", personService.findAll());
        return "/books/show";
    }

    @GetMapping("/new")
    public String newBook(@ModelAttribute("book") Book book){
        return "books/new";
    }

    @PostMapping
    public String create(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult){
        if(bindingResult.hasErrors())
            return "books/new";
        bookService.save(book);
        return "redirect:/books";
    }

    @GetMapping("{id}/edit")
    public String edit(Model model, @PathVariable("id") int id){
        model.addAttribute("book", bookService.findOne(id));
        return "books/edit";
    }

    @PatchMapping("/{id}")
    public String update(@ModelAttribute("book") @Valid Book book, BindingResult bindingResult, @PathVariable("id") int id){
        if(bindingResult.hasErrors())
            return "books/edit";
        bookService.update(id, book);
        return "redirect:/books";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id){
        bookService.delete(id);
        return "redirect:/books";
    }

    @PatchMapping("/{id}/release")
    public String release(@PathVariable("id") int id){
        bookService.realese(id);
        return "redirect:/books/" + id;
    }

    @PatchMapping("/{id}/assign")
    public String assign(@PathVariable("id") int id, @ModelAttribute("person") Person person){
        bookService.assign(id,person);
        return "redirect:/books/" + id;
    }

    @GetMapping("/search")
    public String search(Model model, @RequestParam(name = "title", required = false) String title){
        List<Book> bookList = bookService.searchBoock(title);
        if (bookList.isEmpty()) {
            model.addAttribute("books","NoData");
        } else {
            model.addAttribute("books", bookList);
            model.addAttribute("title",title);
        }
       return "books/search";
    }
}
