package study;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class JunitTest {
    @Test
    void simpleTest() {
        Assertions.assertThat(true).isTrue();
    }
}
