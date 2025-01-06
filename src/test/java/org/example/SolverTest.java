package org.example;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.util.InputParsingException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class SolverTest {

    private static Map<String, String> mockData;

    // capture original System.out and System.in and restore them after each test
    private static final PrintStream originalOut = System.out;
    private static final InputStream originalIn = System.in;

    @BeforeAll
    static void loadMockData() throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        InputStream inputStream = SolverTest.class.getClassLoader().getResourceAsStream("mock_inputs.json");
        if (inputStream == null) {
            throw new IOException("mock_inputs.json not found in test/resources");
        }

        mockData = objectMapper.readValue(inputStream, new TypeReference<Map<String, String>>() {
        });
    }

    @AfterEach
    void restoreStreams() {
        System.setOut(originalOut);
        System.setIn(originalIn);
    }

    @Test
    void testConstructorWithInvalidNumberOfArgumentsInFirstLine() {
        String input = mockData.get("invalid_number_of_arguments_first_line");
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Exception exception = assertThrows(InputParsingException.class, Solver::new);
        assertTrue(exception.getMessage().contains("Invalid number of arguments in first line"));
    }

    @Test
    void testConstructorWithInvalidFormatOfArgumentsInFirstLine() {
        String input = mockData.get("invalid_format_of_arguments_first_line");
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Exception exception = assertThrows(InputParsingException.class, Solver::new);
        assertTrue(exception.getMessage().contains("Invalid number format in first line"));
    }

    @Test
    void testParseInputWithInvalidActionSignature() {
        String input = mockData.get("invalid_action_signature");
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Exception exception = assertThrows(InputParsingException.class, Solver::new);
        assertTrue(exception.getMessage().contains("Invalid action signature"));
    }

    @Test
    void testParseInputWithInvalidActionFormat() {
        String input = mockData.get("invalid_action_format");
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Exception exception = assertThrows(InputParsingException.class, Solver::new);
        assertTrue(exception.getMessage().contains("Invalid action format"));
    }

    @Test
    void testParseInputWithInvalidActionParameterLength() {
        String input = mockData.get("invalid_action_parameter_length");
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Exception exception = assertThrows(InputParsingException.class, Solver::new);
        assertTrue(exception.getMessage().contains("Invalid action parameters length"));
    }

    @Test
    void testParseInputWithInvalidActionParameterFormat() {
        String input = mockData.get("invalid_action_parameter_format");
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Exception exception = assertThrows(InputParsingException.class, Solver::new);
        assertTrue(exception.getMessage().contains("Invalid parameter format in action"));
    }

    @Test
    void testParseInputWithInvalidFormatOfRoutes() {
        String input = mockData.get("invalid_format_of_routes");
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Exception exception = assertThrows(InputParsingException.class, Solver::new);
        assertTrue(exception.getMessage().contains("Invalid number format in routes"));
    }

    @Test
    void testParseInputWithInvalidNumberOfRoutes() {
        String input = mockData.get("invalid_number_of_routes");
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Exception exception = assertThrows(InputParsingException.class, Solver::new);
        assertTrue(exception.getMessage().contains("Invalid number of routes"));
    }

    @Test
    void testParseInputWithEmptyRoutes() {
        String input = mockData.get("empty_routes");
        System.setIn(new ByteArrayInputStream(input.getBytes()));

        Exception exception = assertThrows(InputParsingException.class, Solver::new);
        assertTrue(exception.getMessage().contains("Routes can't be empty"));
    }

    @Test
    void testSolveWithValidInput() {
        try {
            String input1 = mockData.get("valid_input_1");
            String output1 = executeSolverWithInput(input1);
            String expectedOutput1 = mockData.get("valid_input_1_output");
            assertEquals(normalizeEndings(expectedOutput1), normalizeEndings(output1));

            String input2 = mockData.get("valid_input_2");
            String output2 = executeSolverWithInput(input2);
            String expectedOutput2 = mockData.get("valid_input_2_output");
            assertEquals(normalizeEndings(expectedOutput2), normalizeEndings(output2));
        } catch (InputParsingException e) {
            fail("Unexpected InputParsingException");
        }
    }

    private String normalizeEndings(String input) {
        return input.replace("\r\n", "\n").replace("\r", "\n");
    }

    private String executeSolverWithInput(String input) throws InputParsingException {
        System.setIn(new ByteArrayInputStream(input.getBytes()));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        Solver solver = new Solver();
        solver.solve();
        return outputStream.toString();
    }
}

