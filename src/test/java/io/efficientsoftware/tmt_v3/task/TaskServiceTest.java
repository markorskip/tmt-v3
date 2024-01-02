package io.efficientsoftware.tmt_v3.task;

import io.efficientsoftware.tmt_v3.config.BaseIT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TaskServiceTest extends BaseIT {


    @Autowired
    private TaskService taskService;


    TaskDTO createTask(String name, Long parentTask ) {
        TaskDTO task = new TaskDTO();
        task.setName(name);
        task.setParentTask(parentTask);
        return task;
    }
    @Test
    public void testTaskAdd() {
        // Assert task has a parent task or parent project.  Assert when both are passed only
        // the parent tasks persists.
        Task parentTask = new Task();
        parentTask.setName("Parent Task");
        parentTask = taskRepository.save(parentTask);

        Long parentId = parentTask.getId();

        // Test creating tasks with just a parent task id
        taskService.create(createTask("one", parentId));
        taskService.create(createTask("two", parentId));
        taskService.create(createTask("three", parentId));
        Long fourthId = taskService.create(createTask("fourth", parentId));

        List<TaskDTO> children = taskService.get(parentId).getTasks();

        // Test order
        assertEquals(4, children.size());
        assertEquals("one", children.get(0).getName());
        assertEquals("two", children.get(1).getName());
        assertEquals("three", children.get(2).getName());
        assertEquals("fourth", children.get(3).getName());

        taskService.changeOrderOfTask(fourthId, 0);

        children = taskService.get(parentId).getTasks();

        // Test order on insert
        assertEquals(4, children.size());
        assertEquals("fourth", children.get(0).getName());
        assertEquals("one", children.get(1).getName());
        assertEquals("two", children.get(2).getName());
        assertEquals("three", children.get(3).getName());

//        parentTask.getTasks().remove(1);
//
//        parentTask = taskRepository.save(parentTask);
//
//        children = parentTask.getTasks();
//        assertEquals(3, children.size());
//        assertEquals("one", children.get(0).getName());
//        assertEquals("two", children.get(1).getName());
//        assertEquals("three", children.get(2).getName());


    }


}
