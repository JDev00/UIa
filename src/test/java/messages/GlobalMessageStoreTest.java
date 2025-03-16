package messages;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import uia.application.message.store.ConcreteMessageStore;
import uia.application.message.store.GlobalMessageStore;
import uia.core.basement.message.MessageStore;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GlobalMessageStoreTest {
    GlobalMessageStore globalMessageStore = GlobalMessageStore.getInstance();

    @BeforeEach
    void beforeEach() {
        MessageStore messageStore = new ConcreteMessageStore();
        globalMessageStore.mount(messageStore);
    }

    @Test
    void messageStoreShouldBeMountedAndUnmounted() {
        // setup
        MessageStore messageStoreToMount = new ConcreteMessageStore();

        // act
        globalMessageStore.mount(messageStoreToMount);
        MessageStore unmountedStore = globalMessageStore.unmount();

        // verify
        assertEquals(messageStoreToMount, unmountedStore);
    }
}
