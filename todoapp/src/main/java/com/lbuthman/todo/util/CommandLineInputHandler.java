package com.lbuthman.todo.util;

import com.lbuthman.todo.model.ToDoItem;
import com.lbuthman.todo.repository.ToDoRepository;
import com.lbuthman.todo.repository.InMemoryToDoRespository;

import java.util.Collection;

public class CommandLineInputHandler {

    private ToDoRepository toDoRepository = new InMemoryToDoRespository();

    public void printOptions() {
        System.out.println("\n--- To Do Application ---");
        System.out.println("Please make a choice:");
        System.out.println("(a)ll items");
        System.out.println("(f)ind a specific item");
        System.out.println("(i)nsert a new item");
        System.out.println("(u)pdate an existing item");
        System.out.println("(d)elete an existing item");
        System.out.println("(e)xit");
    }

    public String readInput() {
        return System.console().readLine("> ");
    }

    public void processInput(CommandLineInput input) {
        if (input == null) {
            handleUnknownInput();
        } else {
            switch (input) {
                case FIND_ALL:
                    printAllToDoItems();
                    break;
                case FIND_BY_ID:
                    printToDoItem();
                    break;
                case INSERT:
                    insertToDoItem();
                    break;
                case UPDATE:
                    updateToDoItem();
                    break;
                case DELETE:
                    deleteToDoItem();
                    break;
                case EXIT:
                    break;
                default:
                    handleUnknownInput();
            }
        }
    }

    private void handleUnknownInput() {
        System.out.println("Please select a valid option. You don't want to anger me.");
    }

    private void printAllToDoItems() {
        Collection<ToDoItem> toDoItems = toDoRepository.findAll();

        if (toDoItems.isEmpty()) {
            System.out.println("You literally have nothing to do! Chill time.");

        } else {
            for (ToDoItem item: toDoItems) {
                System.out.println(item);
            }
        }
    }

    private void printToDoItem() {
        ToDoItem item = findToDoItem();

        if (item != null) {
            System.out.println(item);
        }
    }

    private ToDoItem findToDoItem() {
        Long id = askForItem();
        ToDoItem item = toDoRepository.findById(id);

        if (item == null) {
            System.err.println("ToDo Item with ID " + id + " could not be found.");
        }

        return item;
    }

    private Long askForItem() {
        System.out.println("Please enter the ID: ");
        String input = readInput();
        return Long.parseLong(input);
    }

    private void insertToDoItem() {
        ToDoItem toDoItem = askForNewToDoAction();
        Long id = toDoRepository.insert(toDoItem);
        System.out.println("Successfully inserted ToDo item with ID " + id + ".");
    }

    private ToDoItem askForNewToDoAction() {
        ToDoItem item = new ToDoItem();
        System.out.println("Please enter the name of the item:");
        item.setName(readInput());
        return item;
    }

    private void updateToDoItem() {
        ToDoItem toDoItem = findToDoItem();

        if (toDoItem != null) {
            System.out.println(toDoItem);
            System.out.println("Please enter the name of the item:");
            toDoItem.setName(readInput());
            System.out.println("Please enter the done status of the item:");
            toDoItem.setCompleted(Boolean.parseBoolean(readInput()));
            toDoRepository.update(toDoItem);
            System.out.println("Successfully updated item with ID " + toDoItem.getId() + ".");
        }
    }

    private void deleteToDoItem() {
        ToDoItem toDoItem = findToDoItem();

        if (toDoItem != null) {
            toDoRepository.delete(toDoItem);
            System.out.println("Successfully deleted item with ID " + toDoItem.getId() + ".");
        }
    }
}
