package org.example.todoserver.dto;

//import javax.validation.Valid;
import org.example.todoserver.entity.TodoItem;

import java.util.ArrayList;

public class TodoListViewModel {

//	@Valid
	private ArrayList<TodoItem> todoList = new ArrayList<TodoItem>();
	
	public TodoListViewModel() {}
	
	public TodoListViewModel(Iterable<TodoItem> items) {
		items.forEach(todoList:: add);
	}

	public TodoListViewModel(ArrayList<TodoItem> todoList) {
		this.todoList = todoList;
	}

	public ArrayList<TodoItem> getTodoList() {
		return todoList;
	}

	public void setTodoList(ArrayList<TodoItem> todoList) {
		this.todoList = todoList;
	}
}