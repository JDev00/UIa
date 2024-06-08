package test.__tests__.messageStore;

import test.core.BeforeEachTest;
import test.core.Test;
import test.core.TestAssertion;
import test.core.TestExecutor;
import uia.physical.message.store.ConcreteMessageStore;
import uia.physical.message.store.GlobalMessageStore;
import uia.core.message.MessageStore;

/**
 * Unit tests.
 */

public class testGlobalMessageStore {
    GlobalMessageStore globalMessageStore = GlobalMessageStore.getInstance();

    @BeforeEachTest
    public void beforeEach() {
        MessageStore messageStore = new ConcreteMessageStore();
        globalMessageStore.mount(messageStore);
    }

    @Test
    public void messageStoreShouldBeMountedAndUnmounted(TestAssertion assertion) {
        assertion.assertions(1);

        MessageStore messageStoreToMount = new ConcreteMessageStore();
        globalMessageStore.mount(messageStoreToMount);

        MessageStore unmountedStore = globalMessageStore.unmount();
        assertion.expect(unmountedStore).toBe(messageStoreToMount);
    }

    public static void main(String[] args) {
        TestExecutor.runTests(new testGlobalMessageStore());
    }
}
