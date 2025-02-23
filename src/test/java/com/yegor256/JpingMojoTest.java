/*
 * SPDX-FileCopyrightText: Copyright (c) 2024-2025 Yegor Bugayenko
 * SPDX-License-Identifier: MIT
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
                    Matchers.containsString("Property ${bar} ")
                );
            }
        );
    }

}
