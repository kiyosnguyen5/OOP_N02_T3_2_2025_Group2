public class Number {
    public int i;

    public static void main(String[] args) {
        Number n1 = new Number();
        Number n2 = new Number();
        n1.i = 2;
        n2.i = 5;
        n2.i = 10;
        System.out.println(n2.i);
        System.out.println(n1.i);
    }
}
