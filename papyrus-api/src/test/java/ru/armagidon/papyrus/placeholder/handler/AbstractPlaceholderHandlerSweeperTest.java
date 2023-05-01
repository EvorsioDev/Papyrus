package ru.armagidon.papyrus.placeholder.handler;

import org.junit.jupiter.api.Test;
import ru.armagidon.papyrus.placeholder.PlaceholderContainer;
import ru.armagidon.papyrus.placeholder.params.DefaultParamSerializers;
import ru.armagidon.papyrus.placeholder.reflective.handler.PlaceholderEntry;
import ru.armagidon.papyrus.placeholder.reflective.handler.PlaceholderHandler;
import ru.armagidon.papyrus.testobjects.Viewer;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class AbstractPlaceholderHandlerSweeperTest {

    @Test
    void sweep() {
        TestPlaceholderHandler handler = new TestPlaceholderHandler();
        TestPlaceholderHandlerSweeper sweeper = new TestPlaceholderHandlerSweeper(DefaultParamSerializers.defaults());
        PlaceholderContainer<Viewer> container = assertDoesNotThrow(() -> sweeper.sweep(handler));
        assertNotNull(container.getPlaceholderById("test", "string"));
        assertNotNull(container.getPlaceholderById("test", "integer"));
        assertNotNull(container.getPlaceholderById("test", "float"));
    }

    private static final class TestPlaceholderHandler implements PlaceholderHandler {
        @PlaceholderEntry(namespace = "test", key = "string")
        public CompletableFuture<String> string() {
            return CompletableFuture.completedFuture("hello world");
        }


        @PlaceholderEntry(namespace = "test", key = "integer")
        public CompletableFuture<Integer> integer() {
            return CompletableFuture.completedFuture(10);
        }


        @PlaceholderEntry(namespace = "test", key = "float")
        public CompletableFuture<Float> f() {
            return CompletableFuture.completedFuture(10f);
        }
    }
}