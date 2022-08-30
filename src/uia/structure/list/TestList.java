package uia.structure.list;

public class TestList {

    public static void main(String[] args) {
        char[] array = "provacomeva?".toCharArray();
        int[] indices = {1, 5, 9, 11};

        BChar bChar = new BChar(1000);
        bChar.add(0, array, 0, array.length);
        bChar.add(indices, 0, indices.length, ' ');

        System.out.println(bChar);
    }
}
