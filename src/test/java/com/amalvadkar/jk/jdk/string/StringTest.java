package com.amalvadkar.jk.jdk.string;

import com.amalvadkar.jk.common.AbstractJavaTest;
import com.amalvadkar.jk.common.util.Objects;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class StringTest extends AbstractJavaTest {

    @Test
    void should_return_same_address_object_of_string_if_creates_using_literal_with_same_content() {
        String myFavouriteLanguage = "Java";
        String yourFavouriteLanguage = "Java";
        assertThat(Objects.hasSameAddress(myFavouriteLanguage, yourFavouriteLanguage)).isTrue();
    }

}
