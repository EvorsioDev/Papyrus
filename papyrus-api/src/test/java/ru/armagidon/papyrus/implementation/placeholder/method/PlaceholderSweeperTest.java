package ru.armagidon.papyrus.implementation.placeholder.method;

import org.junit.jupiter.api.Test;
import ru.armagidon.papyrus.implementation.placeholder.method.parameters.DefaultParamSerializers;
import ru.armagidon.papyrus.placeholder.PlaceholderContainer;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.*;

class PlaceholderSweeperTest {

    @Test
    void sweep() {
        TestPlaceholderHandler handler = new TestPlaceholderHandler();
        PlaceholderSweeper sweeper = new PlaceholderSweeper();
        PlaceholderContainer container = assertDoesNotThrow(() -> sweeper.sweep(handler, DefaultParamSerializers.defaults()));
        assertNotNull(container.getPlaceholder("test", "string"));
        assertNotNull(container.getPlaceholder("test", "integer"));
        assertNotNull(container.getPlaceholder("test", "float"));
    }

    @PlaceholderNamespace("test")
    private static final class TestPlaceholderHandler implements PlaceholderSweeperTarget {


        @PlaceholderKey("string")
        public CompletableFuture<String> string() {
            return CompletableFuture.completedFuture("hello world");
        }


        @PlaceholderKey("integer")
        public CompletableFuture<Integer> integer() {
            return CompletableFuture.completedFuture(10);
        }


        @PlaceholderKey("float")
        public CompletableFuture<Float> f() {
            return CompletableFuture.completedFuture(10f);
        }
    }
}