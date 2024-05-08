/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2024 Yegor Bugayenko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.yegor256;

import com.yegor256.farea.Farea;
import java.nio.file.Path;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;

/**
 * Test case for {@link com.yegor256.JpingMojo}.
 *
 * @since 0.0.1
 */
final class JpingMojoTest {

    @Test
    @ExtendWith(WeAreOnline.class)
    void testSimpleScenario(@TempDir final Path temp) throws Exception {
        new Farea(temp).together(
            f -> {
                f.build()
                    .plugins()
                    .appendItself()
                    .execution("x")
                    .goals("jping")
                    .configuration()
                    .set("propertyName", "foo")
                    .set("failWhenOffline", "true");
                f.exec("initialize");
                MatcherAssert.assertThat(
                    "Sets property correctly",
                    f.log(),
                    Matchers.containsString("${foo} set to \"true\"")
                );
            }
        );
    }

    @Test
    void testWhenOffline(@TempDir final Path temp) throws Exception {
        new Farea(temp).together(
            f -> {
                f.build()
                    .plugins()
                    .appendItself()
                    .execution("x")
                    .goals("jping")
                    .configuration()
                    .set("propertyName", "bar")
                    .set("failWhenOffline", "false");
                f.exec("initialize");
                MatcherAssert.assertThat(
                    "Sets property correctly (even when offline)",
                    f.log(),
                    Matchers.containsString("${bar} set to ")
                );
            }
        );
    }

}
