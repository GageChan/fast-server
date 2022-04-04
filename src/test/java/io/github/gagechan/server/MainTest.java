package io.github.gagechan.server;

import org.junit.jupiter.api.Test;

import io.github.gagechan.server.annotation.EnableIoc;

/**
 * The type Main test.
 *
 * @author : GageChan
 * @version : MainTest.java, v 0.1 2022年04月04 22:10 GageChan
 */
@EnableIoc
public class MainTest {

    /**
     * Run.
     */
    @Test
    public void run() {
        Main.run(MainTest.class);
    }

}
