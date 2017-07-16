package io.emccarthy.common.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.FixtureHelpers;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public abstract class AbstractRepresentationTest<T> {

    private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

    private T instanceOne;
    private T instanceTwo;

    @Before
    public void setUp() {
        instanceOne = newInstanceOne();
        instanceTwo = newInstanceTwo();
    }

    protected abstract T newInstanceOne();

    protected abstract T newInstanceTwo();

    @Test
    public void testEquals() {
        assertEquals(instanceOne, instanceOne);
        assertEquals(instanceTwo, instanceTwo);
        assertNotEquals(instanceOne, instanceTwo);
    }

    @Test
    public void testHashCode() {
        assertEquals(instanceOne.hashCode(), instanceOne.hashCode());
        assertEquals(instanceTwo.hashCode(), instanceTwo.hashCode());
        assertNotEquals(instanceOne.hashCode(), instanceTwo.hashCode());
    }

    @Test
    public void testToString() {
        assertEquals(instanceOne.toString(), instanceOne.toString());
        assertEquals(instanceTwo.toString(), instanceTwo.toString());
        assertNotEquals(instanceOne.toString(), instanceTwo.toString());
    }

    @Test
    public void serializeToJSON() throws Exception {
        final String expected = MAPPER.writeValueAsString(
                MAPPER.readValue(FixtureHelpers.fixture(getFixtureJSON()), getType()));
        assertEquals(expected, MAPPER.writeValueAsString(this.instanceOne));
    }

    @Test
    public void deserializeFromJSON() throws Exception {
        T actual = MAPPER.readValue(FixtureHelpers.fixture(getFixtureJSON()), getType());
        TestCase.assertEquals(this.instanceOne, actual);
    }

    protected abstract Class<T> getType();

    protected abstract String getFixtureJSON();
}
