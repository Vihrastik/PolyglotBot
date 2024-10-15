package com.telebot;

import com.telebot.enums.CommandId;
import com.telebot.enums.InputType;
import com.telebot.user.UserState;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class UserTransitionTest {
    @Test
    void testInitialState() {
        var state = new UserState("whatever");

        //assertEquals(Step.START, state.getStep());
    }

    @Test
    void testStartCommand() throws Exception {
        var state = new UserState("123");
        var result = state.handle(InputType.COMMAND, CommandId.BEGIN.name());

        assertNotNull(result);
        assertTrue(result.getText().contains("Glad to see you, 123"));
        assertEquals(1, result.getCommands().size());
        var cmd = result.getCommands().get(0);
        assertEquals(CommandId.BEGIN.name(), cmd.getId());
//        assertEquals(Step.CHOOSE_LANG, state.getStep());
    }

    @Test
    void testStartCommandResetsState() throws Exception {
        var state = new UserState("whatever");
        state.handle(InputType.COMMAND, CommandId.BEGIN.name());

        var result = state.handle(InputType.COMMAND, CommandId.BEGIN.name());
        assertNotNull(result);
        assertTrue(result.getText().contains("Glad to see you, whatever"));
        assertEquals(1, result.getCommands().size());
        var cmd = result.getCommands().get(0);
        assertEquals(CommandId.BEGIN.name(), cmd.getId());
//        assertEquals(Step.CHOOSE_LANG, state.getStep());
    }
}
