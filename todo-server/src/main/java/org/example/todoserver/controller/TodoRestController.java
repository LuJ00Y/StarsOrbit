package org.example.todoserver.controller;

import org.example.todoserver.entity.TodoItem;
import org.example.todoserver.repository.TodoItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(path = "/todo")
public class TodoRestController {

    @Autowired
    private TodoItemRepository repository;

    @GetMapping("/all")
    public @ResponseBody Iterable<TodoItem> getAll() {
        Iterable<TodoItem> todoList = repository.findAll();
        return repository.findAll();
    }

//    @PostMapping("/add")
//    public @ResponseBody Result addItem(@RequestParam String name, @RequestParam String category) {
//        TodoItem item = new TodoItem(category, name);
//        TodoItem saved = repository.save(item);
//        return new Result("Added", saved);
//    }
//
//    @PostMapping("/update")
//    public @ResponseBody Result updateItem(@RequestParam long id, @RequestParam String name,
//            @RequestParam String category, @RequestParam boolean isComplete) {
//        TodoItem item = new TodoItem(category, name);
//        item.setId(id);
//        item.setComplete(isComplete);
//        TodoItem saved = repository.save(item);
//        return new Result("Updated", saved);
//     }


}
