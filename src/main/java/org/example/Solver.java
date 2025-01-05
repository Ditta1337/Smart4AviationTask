package org.example;

import org.example.util.InputParsingException;
import org.example.util.Tuple;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

public class Solver {
    private final List<Tuple<Character, List<Long>>> actions = new ArrayList<>();
    private final List<List<Tuple<Long, Integer>>> routes = new ArrayList<>();

    private static final int READ_ALL_ACTIONS = -1;

    // map of legal actions and their parameter counts for exception handling
    private static final Map<Character, Integer> LEGAL_ACTIONS = Map.of(
            'Q', 3,
            'A', 3,
            'C', 2,
            'P', 3
    );

    /**
     * Initialise Solver with input from standard input
     */
    public Solver() throws InputParsingException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        try {
            String firstLine = reader.readLine();
            String[] firstLineParts = firstLine.split(" ");

            int numberOfRoutes = Integer.parseInt(firstLineParts[0]);
            int numberOfActions = Integer.parseInt(firstLineParts[1]);

            parseInput(reader, numberOfRoutes, numberOfActions);
        } catch (IOException e) {
            throw new InputParsingException("Error reading input.", e);
        } catch (NumberFormatException e) {
            throw new InputParsingException("Invalid number format in first line" + e);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new InputParsingException("Invalid number of arguments in first line" + e +
                    ". Should be <numOfRoutes> <numOfActions>.");
        }
    }

    /**
     * Initialise Solver with input from file
     *
     * @param inputPath Path to input file.
     */
    public Solver(String inputPath) throws InputParsingException {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inputPath));

            String firstLine = reader.readLine();
            String[] firstLineParts = firstLine.split(" ");

            int numberOfRoutes = Integer.parseInt(firstLineParts[0]);

            parseInput(reader, numberOfRoutes, READ_ALL_ACTIONS);
        } catch (IOException e) {
            throw new InputParsingException("Error reading input.", e);
        } catch (NumberFormatException e) {
            throw new InputParsingException("Invalid number format in first line" + e);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new InputParsingException("Invalid number of arguments in first line" + e +
                    ". Should be <numOfRoutes> <numOfActions>.");
        }
    }

    /**
     * Solve the instantiated problem.
     */
    public void solve() {
        for (Tuple<Character, List<Long>> action : actions) {
            List<Long> params = action.second();
            switch (action.first()) {
                case 'Q':
                    long sum = 0;
                    for (int plane = params.get(0).intValue(); plane <= params.get(1).intValue(); plane++) {
                        long time = params.get(2);
                        for (Tuple<Long, Integer> change : routes.get(plane - 1).reversed()) {
                            if (change.second() == 0) break;
                            sum += (time - change.first()) * change.second();
                            time = change.first();
                        }
                    }

                    System.out.println(sum);
                    break;
                case 'A', 'P':
                    Tuple<Long, Integer> change = new Tuple<>(params.get(2), params.get(1).intValue());
                    routes.get(params.get(0).intValue() - 1).add(change);
                    break;
                case 'C':
                    Tuple<Long, Integer> cancel = new Tuple<>(params.get(1), 0);
                    routes.get(params.get(0).intValue() - 1).add(cancel);
                    break;
            }
        }
    }

    /**
     * Parse input from BufferedReader.
     *
     * @param reader          BufferedReader
     * @param numberOfRoutes  Number of routes specified in the first line.
     * @param numberOfActions Number of lines to read; use ALL_LINES to read all lines.
     * @throws InputParsingException if an error occurs during parsing.
     */
    private void parseInput(BufferedReader reader, int numberOfRoutes, int numberOfActions) throws InputParsingException {
        int lineCount = 0;

        try {
            String line = reader.readLine();
            if (line == null || line.isBlank()) {
                throw new InputParsingException("Routes can't be empty.");
            }

            try {
                routes.addAll(Arrays.stream(line.split(" "))
                        .mapToInt(Integer::parseInt)
                        .mapToObj(i -> {
                            List<Tuple<Long, Integer>> route = new ArrayList<>();
                            route.add(new Tuple<>((long) 0, i));
                            return route;
                        })
                        .toList());

            } catch (NumberFormatException e) {
                throw new InputParsingException("Invalid number format in routes: " + line, e);
            }

            if (routes.size() != numberOfRoutes) {
                throw new InputParsingException("Invalid number of routes. Should be " + numberOfRoutes + ", as specified in first line: " + line);
            }


            while (lineCount < numberOfActions || numberOfActions == READ_ALL_ACTIONS) {
                line = reader.readLine();
                if (line == null) break;

                Tuple<Character, List<Long>> action = parseActionLine(line);
                actions.add(action);

                lineCount++;
            }
        } catch (IOException e) {
            throw new InputParsingException("Error reading input.", e);
        }
    }

    /**
     * Parse a single action line.
     *
     * @param line Action line to parse.
     * @return Parsed action as a Tuple<Character, List<Long>>.
     * @throws InputParsingException if the line format is invalid.
     */
    private Tuple<Character, List<Long>> parseActionLine(String line) throws InputParsingException {
        String[] parts = line.split(" ");

        if (parts.length < 2) {
            throw new InputParsingException("Invalid action format: " + line);
        }

        char signature = parts[0].charAt(0);
        if (!LEGAL_ACTIONS.containsKey(signature)) {
            throw new InputParsingException("Invalid action signature: " + line);
        }

        if (parts.length - 1 != LEGAL_ACTIONS.get(signature)) {
            throw new InputParsingException("Invalid action parameters length: " + line + ". Should be: "
                    + signature + " <long> ".repeat(LEGAL_ACTIONS.get(signature)));
        }

        try {
            List<Long> params = Arrays.stream(parts, 1, parts.length)
                    .map(Long::parseLong)
                    .collect(Collectors.toList());
            return new Tuple<>(signature, params);
        } catch (NumberFormatException e) {
            throw new InputParsingException("Invalid parameter format in action: " + line, e);
        }
    }
}
