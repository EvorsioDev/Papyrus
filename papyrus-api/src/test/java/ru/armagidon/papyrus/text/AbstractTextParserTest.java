package ru.armagidon.papyrus.text;

import net.kyori.adventure.text.Component;
import org.junit.jupiter.api.Test;
import ru.armagidon.papyrus.placeholder.PlaceholderContainer;
import ru.armagidon.papyrus.placeholder.handler.TestPlaceholderHandlerSweeper;
import ru.armagidon.papyrus.placeholder.params.DefaultParamSerializers;
import ru.armagidon.papyrus.placeholder.reflective.handler.PlaceholderEntry;
import ru.armagidon.papyrus.placeholder.reflective.handler.PlaceholderHandler;
import ru.armagidon.papyrus.placeholder.reflective.handler.PlaceholderViewer;
import ru.armagidon.papyrus.testobjects.Viewer;

import java.util.concurrent.CompletableFuture;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class AbstractTextParserTest {

    @Test
    void parseNoArguments() {
        TestPlaceholderHandler handler = new TestPlaceholderHandler();
        TestPlaceholderHandlerSweeper sweeper = new TestPlaceholderHandlerSweeper(DefaultParamSerializers.defaults());
        PlaceholderContainer<Viewer> container = assertDoesNotThrow(() -> sweeper.sweep(handler));
        TestTextParser parser = TestTextParser.configurator().build();


        Component component =
                assertDoesNotThrow(() -> parser.parse(new Viewer(), "%test_string%", container).join());
        assertEquals(Component.text("hello world"), component);

    }

    @Test
    void parseWithArguments() {

        TestPlaceholderHandler handler = new TestPlaceholderHandler();
        TestPlaceholderHandlerSweeper sweeper = new TestPlaceholderHandlerSweeper(DefaultParamSerializers.defaults());
        PlaceholderContainer<Viewer> container = assertDoesNotThrow(() -> sweeper.sweep(handler));

        TestTextParser parser = TestTextParser.configurator().build();
        Component component =
                assertDoesNotThrow(() -> parser.parse(new Viewer(), "%test_string1_peter%", container).join());
        assertEquals(Component.text("hello world, peter"), component);
    }

    @Test
    void testCustomBorder() {
        TestTextParser parser = TestTextParser.configurator().border("**").build();
        TestPlaceholderHandler handler = new TestPlaceholderHandler();
        TestPlaceholderHandlerSweeper sweeper = new TestPlaceholderHandlerSweeper(DefaultParamSerializers.defaults());
        PlaceholderContainer<Viewer> container = assertDoesNotThrow(() -> sweeper.sweep(handler));
        Component component =
                assertDoesNotThrow(() -> parser.parse(new Viewer(), "**test_string**", container).join());
        assertEquals(Component.text("hello world"), component);
    }


    @Test
    void testCustomBorderSingle() {
        TestTextParser parser = TestTextParser.configurator().border("*").build();
        TestPlaceholderHandler handler = new TestPlaceholderHandler();
        TestPlaceholderHandlerSweeper sweeper = new TestPlaceholderHandlerSweeper(DefaultParamSerializers.defaults());
        PlaceholderContainer<Viewer> container = assertDoesNotThrow(() -> sweeper.sweep(handler));
        Component component =
                assertDoesNotThrow(() -> parser.parse(new Viewer(), "*test_string*", container).join());
        assertEquals(Component.text("hello world"), component);
    }


    @Test
    void testCustomSeparator() {
        TestTextParser parser = TestTextParser.configurator().separator("+").build();
        TestPlaceholderHandler handler = new TestPlaceholderHandler();
        TestPlaceholderHandlerSweeper sweeper = new TestPlaceholderHandlerSweeper(DefaultParamSerializers.defaults());
        PlaceholderContainer<Viewer> container = assertDoesNotThrow(() -> sweeper.sweep(handler));

        Component component =
                assertDoesNotThrow(() -> parser.parse(new Viewer(), "%test+string%", container).join());
        assertEquals(Component.text("hello world"), component);
    }

    @Test
    public void testWithViewer() {
        TestPlaceholderHandler handler = new TestPlaceholderHandler();
        TestPlaceholderHandlerSweeper sweeper = new TestPlaceholderHandlerSweeper(DefaultParamSerializers.defaults());
        PlaceholderContainer<Viewer> container = assertDoesNotThrow(() -> sweeper.sweep(handler));

        TestTextParser parser = TestTextParser.configurator().build();
        Component component =
                assertDoesNotThrow(() -> parser.parse(new Viewer(), "%test_viewer%", container).join());
        assertEquals(Component.text("hello world"), component);
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

        @PlaceholderEntry(namespace = "test", key = "string1")
        public CompletableFuture<String> string(String name) {
            return CompletableFuture.completedFuture("hello world, " + name);

        }

        @PlaceholderEntry(namespace = "test", key = "viewer")
        public CompletableFuture<String> viewer(@PlaceholderViewer Viewer viewer) {
            System.out.println(viewer);
            return CompletableFuture.completedFuture("hello world");
        }
    }
}