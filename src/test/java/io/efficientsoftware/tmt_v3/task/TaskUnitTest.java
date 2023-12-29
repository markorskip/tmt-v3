package io.efficientsoftware.tmt_v3.task;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TaskUnitTest {

    @Test
    public void testSortOrder() {
        Task t1 = new Task();

        Task t2 = new Task();
        t2.setSortOrder(1);

        Task t3 = new Task();
        t3.setSortOrder(2);

        Task t4 = new Task();
        t4.setSortOrder(500);

        Task t5 = new Task();

        List<Task> tasks = List.of(t5,t4,t3,t2,t1);
        tasks = tasks.stream().sorted().collect(Collectors.toList());
        tasks.forEach(System.out::println);
        assertEquals(tasks.get(0),t2);
        assertEquals(tasks.get(1),t3);
        assertEquals(tasks.get(2),t1);
    }
}
