package com.plugin.drool;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the unterminated-string predicates in {@link DroolsAnnotator}.
 *
 * <p>Covers the cases from design §Unit Tests:
 * <ul>
 *   <li>{@code ""} → well-formed (empty string)</li>
 *   <li>{@code "abc"} → well-formed</li>
 *   <li>{@code "a\""} → well-formed (escaped quote)</li>
 *   <li>{@code "a\\\""} → well-formed (escaped backslash then escaped quote)</li>
 *   <li>{@code "abc} → unterminated</li>
 *   <li>{@code "abc\\} → unterminated (ends with escape)</li>
 * </ul>
 *
 * <p><b>Validates: Requirements 2.3, 2.4, 3.6</b>
 */
class DroolsAnnotatorStringPredicateTest {

    // ========================================================================
    // Tests for isWellFormedStringLiteral
    // ========================================================================

    @Nested
    @DisplayName("isWellFormedStringLiteral")
    class IsWellFormedStringLiteralTests {

        @Test
        @DisplayName("empty string literal \"\" is well-formed")
        void emptyStringLiteral() {
            assertTrue(DroolsAnnotator.isWellFormedStringLiteral("\"\""));
        }

        @Test
        @DisplayName("\"abc\" is well-formed")
        void simpleStringLiteral() {
            assertTrue(DroolsAnnotator.isWellFormedStringLiteral("\"abc\""));
        }

        @Test
        @DisplayName("\"a\\\"\" is well-formed (escaped quote)")
        void escapedQuoteStringLiteral() {
            // Java string: "a\"" → represents the text: "a\""
            assertTrue(DroolsAnnotator.isWellFormedStringLiteral("\"a\\\"\""));
        }

        @Test
        @DisplayName("\"a\\\\\\\"\" is well-formed (escaped backslash then escaped quote)")
        void escapedBackslashThenEscapedQuote() {
            // Java string: "a\\\"" → represents the text: "a\\\""
            assertTrue(DroolsAnnotator.isWellFormedStringLiteral("\"a\\\\\\\"\""));
        }

        @Test
        @DisplayName("\"abc is unterminated")
        void unterminatedString() {
            assertFalse(DroolsAnnotator.isWellFormedStringLiteral("\"abc"));
        }

        @Test
        @DisplayName("\"abc\\\\ is unterminated (ends with escape)")
        void unterminatedEndsWithEscape() {
            // Java string: "abc\\" → represents the text: "abc\\
            assertFalse(DroolsAnnotator.isWellFormedStringLiteral("\"abc\\\\"));
        }

        @Test
        @DisplayName("null is not well-formed")
        void nullInput() {
            assertFalse(DroolsAnnotator.isWellFormedStringLiteral(null));
        }

        @Test
        @DisplayName("single quote character is not well-formed")
        void singleQuote() {
            assertFalse(DroolsAnnotator.isWellFormedStringLiteral("\""));
        }

        @Test
        @DisplayName("empty string is not well-formed")
        void emptyInput() {
            assertFalse(DroolsAnnotator.isWellFormedStringLiteral(""));
        }

        @Test
        @DisplayName("string not starting with quote is not well-formed")
        void noLeadingQuote() {
            assertFalse(DroolsAnnotator.isWellFormedStringLiteral("abc\""));
        }
    }

    // ========================================================================
    // Tests for endsWithUnescapedQuote
    // ========================================================================

    @Nested
    @DisplayName("endsWithUnescapedQuote")
    class EndsWithUnescapedQuoteTests {

        @Test
        @DisplayName("\"\" ends with unescaped quote")
        void emptyStringLiteral() {
            assertTrue(DroolsAnnotator.endsWithUnescapedQuote("\"\""));
        }

        @Test
        @DisplayName("\"abc\" ends with unescaped quote")
        void simpleString() {
            assertTrue(DroolsAnnotator.endsWithUnescapedQuote("\"abc\""));
        }

        @Test
        @DisplayName("\"a\\\"\" ends with unescaped quote (escaped quote before final quote)")
        void escapedQuoteBeforeFinal() {
            // Java: "a\"" → text: "a\""  — the \" is an escaped quote, then the final " is unescaped
            assertTrue(DroolsAnnotator.endsWithUnescapedQuote("\"a\\\"\""));
        }

        @Test
        @DisplayName("\"a\\\\\" ends with unescaped quote (even backslashes before quote)")
        void evenBackslashesBeforeQuote() {
            // Java: "a\\\\" → text: "a\\"  — two backslashes (even), then final "
            // Wait, we need the text to end with ". Let me reconsider.
            // Java string: "a\\\\\"" → text: a\\" — that's: a, \\, " — the \\ is even, so " is unescaped
            assertTrue(DroolsAnnotator.endsWithUnescapedQuote("\"a\\\\\""));
        }

        @Test
        @DisplayName("\"abc\\ does not end with quote")
        void doesNotEndWithQuote() {
            // Java: "abc\\" → text: "abc\  — does not end with "
            assertFalse(DroolsAnnotator.endsWithUnescapedQuote("\"abc\\"));
        }

        @Test
        @DisplayName("\"a\\\" does not end with unescaped quote (odd backslash before quote)")
        void oddBackslashBeforeQuote() {
            // Java: "a\\\"" → text: "a\" — one backslash before quote means it's escaped
            // Wait, this is tricky. Let me think carefully.
            // Java string literal "\"a\\\"" has chars: ", a, \, "
            // So the text is: "a\" — ends with " but preceded by one \ (odd), so it's escaped
            assertFalse(DroolsAnnotator.endsWithUnescapedQuote("\"a\\\""));
        }

        @Test
        @DisplayName("null returns false")
        void nullInput() {
            assertFalse(DroolsAnnotator.endsWithUnescapedQuote(null));
        }

        @Test
        @DisplayName("empty string returns false")
        void emptyInput() {
            assertFalse(DroolsAnnotator.endsWithUnescapedQuote(""));
        }

        @Test
        @DisplayName("string not ending with quote returns false")
        void noTrailingQuote() {
            assertFalse(DroolsAnnotator.endsWithUnescapedQuote("\"abc"));
        }
    }
}
