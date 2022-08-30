package uia.test;

import uia.core.platform.ContextAWT;

public class ContextTest extends ContextAWT {

    public ContextTest(int x, int y) {
        super(x, y);
    }

    @Override
    public void asyncSetup(int x, int y) {
        storePage(new PageTest3(this));

        open(getStoredPage(0));
    }

    public static void main(String[] args) {
        int[] dim = getScreenSize();
        ContextTest contextTest = new ContextTest((int) (0.8f * dim[0]), (int) (0.8f * dim[1]));
        contextTest.start();

        //System.out.println(countLines(new File("src\\")));
    }

    /*public static int countLines(File file) {
        Stack<File> stack = new Stack<>();
        stack.push(file);

        int out = 0;
        while (!stack.empty()) {
            File pop = stack.pop();
            File[] list = pop.listFiles();

            if (list != null) {

                for (File i : list) {
                    stack.push(i);
                }
            } else {
                out += Utils.readLines(pop.getAbsolutePath()).size();
            }
        }

        return out;
    }*/
}