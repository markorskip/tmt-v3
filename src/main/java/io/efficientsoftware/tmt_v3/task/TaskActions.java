package io.efficientsoftware.tmt_v3.task;

public class TaskActions {

    public enum TaskAction {
        MARK_COMPLETED, // Non-composite only
        MARK_NOT_DONE, // Non-composite only
        ADD_SUB_TASK, // Any time
        DELETE, // Non-composite only
        EDIT_NAME,  // Can always be done
        EDIT_TIME, // Non-composite only
        EDIT_COST, // Non-composite only
        MOVE_SORT_ORDER_UP, // Changes sort order
        MOVE_SORT_ORDER_DOWN, // Changes sort order
        MOVE_TO_NEW_PARENT //Changes parent ID and receives new sort order, advanced edit
    }

    // TODO implement this check on all actions
    static boolean isValidAction(Task task, TaskAction action) {
        if (task.isComposite()) {
            switch (action) {
                case MARK_COMPLETED, MARK_NOT_DONE, DELETE, EDIT_COST, EDIT_TIME -> {
                    return false;
                }
                case ADD_SUB_TASK, MOVE_SORT_ORDER_DOWN, MOVE_SORT_ORDER_UP, MOVE_TO_NEW_PARENT, EDIT_NAME -> {
                    return true;
                }
            }
        }
        // Unless explicitly allowed return false;
        return false;
    }
}
