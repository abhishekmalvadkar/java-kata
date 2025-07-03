package com.amalvadkar.jk;

import com.amalvadkar.jk.common.AbstractUT;
import org.junit.jupiter.api.Test;

import static java.util.List.of;
import static org.assertj.core.api.Assertions.assertThat;

public class WarmUpTest extends AbstractUT {

    @Test
    void warmUp() {
        assertThat(of(1, 2, 3)).containsAnyOf(1, 2);
    }
}
