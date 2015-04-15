package dk.aau.sw402F15;

import java.util.ArrayList;
import java.util.List;

public class Scanner {
    public enum TokenType {
        semi, equals_operator, not_equals_operator, and_operator, or_operator, assignment_operator, true_keyword, false_keyword, decimal_literal, port_identifier, invalid
    }

    public class Token {
        private TokenType type;
        private String Text;

        public Token(TokenType type, String text) {
            this.setType(type);
            this.setText(text);
        }

        public String getText() {
            return Text;
        }

        public void setText(String text) {
            Text = text;
        }

        public TokenType getType() {
            return type;
        }

        public void setType(TokenType type) {
            this.type = type;
        }
    }

    private String m_input;
    private int m_next;
    private int m_end;
    public Scanner(String input) {
        this.m_input = input;
        this.m_next = 0;
        this.m_end = input.length();
    }

    public List<Token> readAll() {
        ArrayList<Token> tokens = new ArrayList<Token>();
        Token token;
        while ((token = read()) != null) {
            tokens.add(token);
        }
        return tokens;
    }

    public Token read() {
        // Check if we reached the end
        if (m_next == m_end)
            return null;

        // Skip whiltespaces
        skipWhitespace();

        // Scan token
        Token token = null;
        if (m_input.startsWith(";", m_next)) {
            token = new Token(TokenType.semi, m_input.substring(m_next, ++m_next));
        } else if (m_input.startsWith("==", m_next)) {
            token = new Token(TokenType.equals_operator, m_input.substring(m_next, m_next += 2));
        } else if (m_input.startsWith("!=", m_next)) {
            token = new Token(TokenType.not_equals_operator, m_input.substring(m_next, m_next += 2));
        } else if (m_input.startsWith("&&", m_next)) {
            token = new Token(TokenType.and_operator, m_input.substring(m_next, m_next += 2));
        } else if (m_input.startsWith("||", m_next)) {
            token = new Token(TokenType.or_operator, m_input.substring(m_next, m_next += 2));
        } else if (m_input.startsWith("=", m_next)) {
            token = new Token(TokenType.assignment_operator, m_input.substring(m_next, ++m_next));
        } else if (m_input.startsWith("true", m_next)) {
            token = new Token(TokenType.true_keyword, m_input.substring(m_next, m_next += 4));
        } else if (m_input.startsWith("false", m_next)) {
            token = new Token(TokenType.false_keyword, m_input.substring(m_next, m_next += 5));
        } else if (Character.isDigit(m_input.charAt(m_next))) {
            int end = m_next;
            Boolean isValid = true;

            do {
                if(!Character.isDigit(m_input.charAt(end))) {
                    isValid = false;
                }
                end++;
            } while (Character.isDigit(m_input.charAt(end)));

            if (m_input.charAt(end) != '.') {
                isValid = false;
            }
            end++;

            do {
                if(!Character.isDigit(m_input.charAt(end))) {
                    isValid = false;
                }
                end++;
            } while (end < m_end && Character.isDigit(m_input.charAt(end)));

            token = new Token(isValid ? TokenType.decimal_literal : TokenType.invalid, m_input.substring(m_next, end));
            m_next = end;
        }
        else if (m_input.startsWith("I#", m_next) || m_input.startsWith("Q#", m_next) || m_input.startsWith("M#", m_next)) {
            token = new Token(TokenType.or_operator, m_input.substring(m_next, m_next += 2));
        }
        else {
            token = new Token(TokenType.invalid, m_input.substring(m_next, ++m_next));
        }

        return token;
    }

    private void skipWhitespace() {
        while ((m_input.charAt(m_next) == ' ' ||
               m_input.charAt(m_next) == '\n' ||
               m_input.charAt(m_next) == '\r')
                && m_next != m_end) {
            m_next++;
        }
    }

}
