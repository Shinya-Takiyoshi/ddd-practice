package proxy;

import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
public class Proxy implements Server {
    private final Server server;

    private void authorize(String userId) throws Exception {
        final String[] authorizedUserId = {"1", "2", "3"};
        boolean isAuthorize = Arrays.stream(authorizedUserId).anyMatch(id -> id == userId);
        if (!isAuthorize) {
            throw new Exception("許可されていない操作になります");
        }
    }

    @Override
    public void handle(String userId) throws Exception {
        this.authorize(userId);
        System.out.println("処理開始");
        this.server.handle(userId);
        System.out.println("処理終了");
    }
}
