package dev.onesnzeroes.pt;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
        "spring.datasource.url=jdbc:h2:mem:ctx;DB_CLOSE_DELAY=-1",
        "coc.token="
})
class PohjantahtiApplicationTests {

    @Test
    void contextLoads() {
    }
}
