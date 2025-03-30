package com.laamella.snippets_test_junit5.table;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;

public class Table {
    public static final String SEPARATOR_REGEX = "\\|";
    public static final String SEPARATOR = "|";

    public List<String> header = new ArrayList<>();
    public List<Row> rows = new ArrayList<>();

    public Table(String... header) {
        this.header.addAll(asList(header));
    }

    public Table(List<String> header) {
        this.header = requireNonNull(header);
    }

    public static Table parse(String tableText) {
        String[] rows = tableText.split("\n");
        String[] headers = rows[0].split(SEPARATOR_REGEX);
        for (int i = 0; i < headers.length; i++) {
            headers[i] = headers[i].trim();
        }
        Table table = new Table(headers);
        for (int rowNr = 1; rowNr < rows.length; rowNr++) {
            Row row = new Row();
            String[] cells = rows[rowNr].split(SEPARATOR_REGEX);
            row.cellValues.addAll(asList(cells));
            table.rows.add(row);
        }
        return table;
    }

    @Override
    public String toString() {
        Stream<List<String>> fullTable = Stream.concat(
                Stream.of(header),
                rows.stream().map(r -> r.cellValues));

        // TODO calculate column widths

        return fullTable.map(r -> String.join(SEPARATOR, r)).collect(Collectors.joining("\n"));
    }

    public <T> Stream<T> map(Supplier<T> constructor, BiConsumer<Cell, T> mapper) {
        List<T> result = new ArrayList<>();
        for (Row row : rows) {
            T t = constructor.get();
            for (int i = 0; i < row.cellValues.size(); i++) {
                mapper.accept(new Cell(header.get(i), row.cellValues.get(i)), t);
            }
            result.add(t);
        }
        return result.stream();
    }

    public static <T> Table fromCollection(Collection<T> collection, Class<T> elementClass, String... fieldNames) {
        try {
            for (String fieldName : fieldNames) {
                elementClass.getDeclaredField(fieldName).setAccessible(true);
            }
            Table table = new Table(fieldNames);
            for (Object o : collection) {
                Row row = new Row();
                for (String s : fieldNames) {
                    Field field = elementClass.getDeclaredField(s);
                    Object value = field.get(o);
                    row.cellValues.add(value.toString());
                }
                table.rows.add(row);
            }
            return table;
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static class Row {
        public final List<String> cellValues;

        public Row() {
            cellValues = new ArrayList<>();
        }

        public Row(List<String> cellValues) {
            this.cellValues = cellValues;
        }
    }

    public static class Cell {
        final String name;
        private final String value;

        public Cell(String name, String value) {
            this.name = requireNonNull(name);
            this.value = requireNonNull(value).trim();
        }

        public Optional<String> value() {
            if (value.isEmpty()) {
                return Optional.empty();
            } else {
                return Optional.of(value);
            }
        }

        public Optional<Integer> intValue() {
            return value().map(Integer::parseInt);
        }
    }
}
