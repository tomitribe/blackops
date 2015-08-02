package com.tomitribe.blackops;

import com.amazonaws.services.ec2.model.Filter;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import java.lang.reflect.Modifier;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.IntSupplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class OperationIdTest extends Assert {

    /**
     * Each generated OperationId should be unique
     */
    @Test
    public void testGenerate() throws Exception {
        final int expected = 10000;

        final Set<OperationId> ids = Stream.generate(OperationId::generate)
                .limit(expected)
                .collect(Collectors.toSet());

        assertEquals(expected, ids.size());
    }

    /**
     * It should be possible to convert an OperationId
     * to and from a string
     */
    @Test
    public void testParse() throws Exception {
        Stream.generate(OperationId::generate)
                .limit(1000)
                .forEach(id -> {
                    final OperationId parsed = OperationId.parse(id.get());
                    assertEquals(id, parsed);
                });
    }

    /**
     * We should reject badly formatted OperationIds to prevent
     * bad IDs from floating around the system
     */
    @Test
    public void testParseNegative() throws Exception {
        final Class<IllegalArgumentException> expected = IllegalArgumentException.class;

        assertException(expected, OperationId::parse, "");
        assertException(expected, OperationId::parse, "op-");
        assertException(expected, OperationId::parse, "mnbvcxz123456");

        // too short
        assertException(expected, OperationId::parse, "op-mnbvcxz12345");

        // too long
        assertException(expected, OperationId::parse, "op-mnbvcxz1234533");

        // wrong prefix
        assertException(expected, OperationId::parse, "po-cxzmnbv456aaa");

        // wrong case
        assertException(expected, OperationId::parse, "po-CXZMNBV456AAA");

        final Pattern allowed = Pattern.compile("[a-z0-9]");

        // Illegal characters
        IntStream.generate(new IntSequence())
                .limit(254)
                .mapToObj(value -> new String(new char[]{(char) value}))
                .filter(allowed.asPredicate().negate())
                .forEach(s -> {
                    assertException(expected, OperationId::parse, String.format("op-%saaaaaaaaaaaa", s));
                    assertException(expected, OperationId::parse, String.format("op-aa%saaaaaaaaaa", s));
                    assertException(expected, OperationId::parse, String.format("op-aaaaa%saaaaaaa", s));
                    assertException(expected, OperationId::parse, String.format("op-aaaaaaaa%saaaa", s));
                    assertException(expected, OperationId::parse, String.format("op-aaaaaaaaaaaa%s", s));
                });
        ;
    }

    /**
     * You should only be able to get an OperationId by calling
     * parse() or generate()
     */
    @Test
    @Ignore("Still have code using the public constructor")
    public void testNoPublicConstructor() throws Exception {
        Stream.of(OperationId.class.getDeclaredConstructors())
                .forEach(constructor -> assertFalse(Modifier.isPublic(constructor.getModifiers())));
    }

    /**
     * Get should return the plain string version of the ID
     */
    @Test
    public void testGet() throws Exception {
        final OperationId id = OperationId.parse("op-cxzmnbv456aaa");
        assertEquals("op-cxzmnbv456aaa", id.get());
    }

    /**
     * toString should return the plain string version of the ID
     */
    @Test
    public void testToString() throws Exception {
        final OperationId id = OperationId.parse("op-gfdm75ner6add");
        assertEquals("op-gfdm75ner6add", id.toString());
    }

    /**
     * You should be able to get a valid EC2 Filter from an OperationId
     */
    @Test
    public void testAsFilter() throws Exception {
        final OperationId id = OperationId.parse("op-mnbvcxz123456");

        final Filter filter = id.asFilter();

        assertEquals("tag:operation-id", filter.getName());
        assertEquals("op-mnbvcxz123456", filter.getValues().get(0));
        assertEquals(1, filter.getValues().size());
    }

    /**
     * You should be able to get a valid EC2 Tag from an OperationId
     */
    @Test
    public void testAsTag() throws Exception {
        final OperationId id = OperationId.parse("op-vh3iyfasdn2fd");

        final com.amazonaws.services.ec2.model.Tag tag = id.asTag();

        assertEquals("operation-id", tag.getKey());
        assertEquals("op-vh3iyfasdn2fd", tag.getValue());
    }

    /**
     * You should be able to use OperationId in a Set or Map
     *
     * Equals should match using the string value
     */
    @Test
    public void testEquals() throws Exception {
        final OperationId one = OperationId.parse("op-aaaaaaaaaaaaa");
        final OperationId two = OperationId.parse("op-aaaaaaaaaaaaa");
        final OperationId three = OperationId.parse("op-aaaaaaaaaaaab");

        assertEquals(one, one);
        assertEquals(one, one);
        assertEquals(one, two);
        assertNotEquals(one, three);
        assertNotEquals(one, null);
        assertNotEquals(one, "wrong type");
    }

    /**
     * You should be able to use OperationId in a Set or Map
     *
     * hashCode should match using the string value
     */
    @Test
    public void testHashCode() throws Exception {
        final OperationId one = OperationId.parse("op-aaaaaaaaaaaaa");
        final OperationId two = OperationId.parse("op-aaaaaaaaaaaaa");
        final OperationId three = OperationId.parse("op-aaaaaaaaaaaab");

        assertEquals(one.hashCode(), one.hashCode());
        assertEquals(one.hashCode(), two.hashCode());
        assertNotEquals(one.hashCode(), three.hashCode());
    }

    private static class IntSequence implements IntSupplier {
        int i = 0;

        @Override
        public int getAsInt() {
            return i++;
        }
    }

    private <T> void assertException(final Class<? extends Throwable> expected, final Consumer<T> consumer, final T t) {
        try {
            consumer.accept(t);
            fail(String.format("Expected %s with input '%s'", expected.getSimpleName(), t));
        } catch (Exception e) {
            assertEquals(e.getClass(), expected);
        }
    }
}