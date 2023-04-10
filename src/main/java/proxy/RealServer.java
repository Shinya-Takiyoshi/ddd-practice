package proxy;

public class RealServer implements Server {
    @Override
    public void handle(String userId) {
        System.out.println(userId + "の処理を実行中");
    }
}
