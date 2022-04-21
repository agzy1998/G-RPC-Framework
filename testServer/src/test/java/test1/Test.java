package test1;

public class Test {
    public static void main(String[] args) {
        Test test = new Test();
        Son son = new Son();
        Sing sing = son;
        Dance dance = son;
        sing.sing();
        dance.dance();
    }
}

interface Sing{
    void sing();
}
interface Dance{
    void dance();
}

class Son implements Sing, Dance{
    String id = "1";
    String name = "2";

    @Override
    public void sing() {
        System.out.println("son sing now");
    }

    @Override
    public void dance() {
        System.out.println("son dance now");
    }
}