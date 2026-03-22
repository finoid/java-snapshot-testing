package io.github.finoid.snapshots.annotations;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

class MaskAnnotationUnitTest {

    @Test
    void givenMaskAnnotationOnField_whenCheckingValues_thenValuesAreCorrect() throws NoSuchFieldException {
        Field field = AnnotatedClass.class.getDeclaredField("maskedField");
        Mask mask = field.getAnnotation(Mask.class);

        assertThat(mask.value()).isEqualTo("$.path");
        assertThat(mask.maskValue()).isEqualTo("REDACTED");
    }

    @Test
    void givenMultipleMaskAnnotations_whenCheckingValues_thenAllAreStored() {
        Masks masks = AnnotatedClass.class.getAnnotation(Masks.class);
        assertThat(masks.value()).hasSize(2);
        assertThat(masks.value()[0].value()).isEqualTo("$.path1");
        assertThat(masks.value()[1].value()).isEqualTo("$.path2");
    }

    @Mask(value = "$.path1")
    @Mask(value = "$.path2")
    private static class AnnotatedClass {
        @Mask(value = "$.path", maskValue = "REDACTED")
        private String maskedField;
    }
}
