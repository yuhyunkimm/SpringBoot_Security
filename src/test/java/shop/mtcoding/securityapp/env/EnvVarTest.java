package shop.mtcoding.securityapp.env;

import org.junit.jupiter.api.Test;

public class EnvVarTest {

    @Test
    private void secret_test() {
        String key = System.getenv("HS512_SECRET");
        System.out.println(key);
    }

}
