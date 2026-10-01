package application;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProgramInputTest {

    private Scanner scanner(String... lines) {
        String input = String.join(System.lineSeparator(), lines) + System.lineSeparator();
        return new Scanner(new ByteArrayInputStream(input.getBytes(StandardCharsets.UTF_8)));
    }

    @Test
    void readIntParsesInteger() {
        assertEquals(42, Program.readInt(scanner("42"), ""));
    }

    @Test
    void readDoubleAcceptsDotSeparator() {
        assertEquals(1000.5, Program.readDouble(scanner("1000.50"), ""));
    }

    @Test
    void readDoubleAcceptsCommaSeparator() {
        assertEquals(1000.5, Program.readDouble(scanner("1000,50"), ""));
    }

    @Test
    void readDoubleIsIndependentOfDefaultLocale() {
        Locale original = Locale.getDefault();
        try {
            Locale.setDefault(Locale.forLanguageTag("pt-BR"));
            assertEquals(0.02, Program.readDouble(scanner("0.02"), ""));

            Locale.setDefault(Locale.US);
            assertEquals(1000.5, Program.readDouble(scanner("1000.50"), ""));
        } finally {
            Locale.setDefault(original);
        }
    }

    @Test
    void readIntSkipsInvalidLines() {
        assertEquals(7, Program.readInt(scanner("abc", "", "7"), ""));
    }

    @Test
    void readDoubleSkipsInvalidLines() {
        assertEquals(2.5, Program.readDouble(scanner("abc", "2,5"), ""));
    }

    @Test
    void readLineTrimsSpaces() {
        assertEquals("Ana Silva", Program.readLine(scanner("  Ana Silva  "), ""));
    }
}