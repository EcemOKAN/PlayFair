package com.ecemokan.playfair;

public class PlayfairMain {
    private static final int TABLE_COLUMN_COUNT = 5;
    private static final int TABLE_SIZE = TABLE_COLUMN_COUNT * TABLE_COLUMN_COUNT;


    public String getTable(String key) {
        StringBuffer table = new StringBuffer(key);
        int i = 0;
        int tableLength = table.length();
        while (i < tableLength) {
            char c = table.charAt(i);
            // Check alphabetic characters
            if (c < 'A' || c > 'Z') {
                // Non alphabetic character;
                table.deleteCharAt(i);
                tableLength--;
                i--;
            } else if (table.indexOf(String.valueOf(c)) != table.lastIndexOf(String.valueOf(c))) {
                // Alphabetic character, but not only one in table, so need to remove one from the last
                table.deleteCharAt(table.lastIndexOf(table.substring(i, i + 1)));
                tableLength--;
            }

            i++;
        }

        String halfTable = table.toString();
        int j;
        // Add character to fill the table to 25 characters from A to Z orderly
        for (i = halfTable.length(), j = 0; i < TABLE_SIZE; i++, j++) {
            int unicodeOfUpcaseA = 'A';
            char c = (char) (unicodeOfUpcaseA + j);

            while (j < TABLE_SIZE) {
                if (needSkip(halfTable, c)) {
                    // Move to next character
                    c = (char) (unicodeOfUpcaseA + (++j));
                } else {
                    // Current character is not in table, break while loop and add
                    break;
                }
            }
            halfTable += c;
        }
        return halfTable;
    }


    private boolean needSkip(String halfTable, char c) {
        if (halfTable.indexOf(c) != -1)
            return true;
        if (c == 'I' && halfTable.indexOf('J') != -1) {
            return true;
        } else if (c == 'J' && halfTable.indexOf('I') != -1) {
            return true;
        }
        return false;
    }


    public String getInfo(String words) {
        String info = "";

        for (int j = 0; j < words.length(); j++) {
            // Check alphabetic characters. Do nothing if non alphabetic character
            char c = words.charAt(j);
            if (c >= 'A' && c <= 'Z') {
                // If the current character is neither the first in the target string, nor the same as the last, add 'X' instead
                if (info.length() - 1 != -1 && info.charAt(info.length() - 1) != ' '
                        && c == info.charAt(info.length() - 1)) {
                    c = 'X';
                    j--;
                }
                info += c;

                // Each pair will be separated by one space
                if (info.length() % 3 == 2) {
                    info += ' ';
                }
            }
        }
        if (info.length() % 3 == 1) {
            // Add 'X' to the last pair while the number of infomation's character is odd
            info += 'X';
        } else if (info.charAt(info.length() - 1) == ' ') {
            // Sometimes there will be an external space at the end that needs to be removed
            info = info.substring(0, info.length() - 1);
        }

        return info;
    }


    public String getCiphertext(String table, String info) {
        String ciphertext = "";
        for (int i = 0; i < info.length(); i += 3) {
            int firstIndex = table.indexOf(info.charAt(i));
            int firstPoint[] = { firstIndex / TABLE_COLUMN_COUNT, firstIndex % TABLE_COLUMN_COUNT };
            int secondIndex = table.indexOf(info.charAt(i + 1));
            int secondPoint[] = { secondIndex / TABLE_COLUMN_COUNT,
                    secondIndex % TABLE_COLUMN_COUNT };

            // In the different column or row
            if ((firstPoint[0] != secondPoint[0]) && (firstPoint[1] != secondPoint[1])) {
                firstIndex = (firstPoint[0] * TABLE_COLUMN_COUNT) + secondPoint[1];
                secondIndex = (secondPoint[0] * TABLE_COLUMN_COUNT) + firstPoint[1];
            }
            // In the same row, shift right by 1
            else if (firstPoint[0] == secondPoint[0]) {
                // Shift right first
                firstIndex += 1;
                secondIndex += 1;

                // Check overflow on the right side
                if (firstPoint[1] == TABLE_COLUMN_COUNT - 1) {
                    // Wrapping around to the left
                    firstIndex = firstPoint[0] * TABLE_COLUMN_COUNT;
                } else if (secondPoint[1] == TABLE_COLUMN_COUNT - 1) {
                    // Wrapping around to the left
                    secondIndex = secondPoint[0] * TABLE_COLUMN_COUNT;
                }
            }
            // In the same column, shift below by 1
            else {
                // Shift below first
                firstIndex += TABLE_COLUMN_COUNT;
                secondIndex += TABLE_COLUMN_COUNT;

                // Check overflow on the bottom side
                if (firstPoint[0] == TABLE_COLUMN_COUNT - 1) {
                    // Wrapping around to the top
                    firstIndex = firstPoint[1];
                } else if (secondPoint[0] == TABLE_COLUMN_COUNT - 1) {
                    // Wrapping around to the top
                    secondIndex = secondPoint[1];
                }
            }
            // Add "" in front of each pair to convert char to String
            ciphertext += "" + table.charAt(firstIndex) + table.charAt(secondIndex) + " ";
        }
        return ciphertext = ciphertext.substring(0, ciphertext.length() - 1);
    }

    public String getPlaintext(String table, String ciphertext) {
        String revertCipher = "";

        for (int i = 0; i < ciphertext.length(); i += 3) {
            int firstIndex = table.indexOf(ciphertext.charAt(i));
            int firstPoint[] = { firstIndex / TABLE_COLUMN_COUNT, firstIndex % TABLE_COLUMN_COUNT };
            int secondIndex = table.indexOf(ciphertext.charAt(i + 1));
            int secondPoint[] = { secondIndex / TABLE_COLUMN_COUNT,
                    secondIndex % TABLE_COLUMN_COUNT };

            // In the different column or row
            if ((firstPoint[0] != secondPoint[0]) && (firstPoint[1] != secondPoint[1])) {
                firstIndex = (firstPoint[0] * TABLE_COLUMN_COUNT) + secondPoint[1];
                secondIndex = (secondPoint[0] * TABLE_COLUMN_COUNT) + firstPoint[1];
            }
            // In the same row, shift left by 1
            else if (firstPoint[0] == secondPoint[0]) {
                // Shift left first
                firstIndex -= 1;
                secondIndex -= 1;

                // Check overflow on the left side
                if (firstPoint[1] == 0) {
                    // Wrapping around to the right
                    firstIndex = firstPoint[0] * TABLE_COLUMN_COUNT + (TABLE_COLUMN_COUNT - 1);
                } else if (secondPoint[1] == 0) {
                    // Wrapping around to the right
                    secondIndex = secondPoint[0] * TABLE_COLUMN_COUNT + (TABLE_COLUMN_COUNT - 1);
                }
            }
            // In the same column, shift top by 1
            else {
                // Shift top first
                firstIndex -= TABLE_COLUMN_COUNT;
                secondIndex -= TABLE_COLUMN_COUNT;

                // Check overflow on the top side
                if (firstPoint[0] == 0) {
                    // Wrapping around to the bottom
                    firstIndex = ((TABLE_COLUMN_COUNT - 1) * TABLE_COLUMN_COUNT) + firstPoint[1];
                } else if (secondPoint[0] == 0) {
                    // Wrapping around to the bottom
                    secondIndex = ((TABLE_COLUMN_COUNT - 1) * TABLE_COLUMN_COUNT) + secondPoint[1];
                }
            }

            // Logger.d(getClass(), "" + mInfo.charAt(i) + mInfo.charAt(i + 1) + " => " + firstCipher + secondCipher);
            // Add "" in front of each pair to convert char to String
            revertCipher += "" + table.charAt(firstIndex) + table.charAt(secondIndex) + " ";
        }

        String plaintext = "";

        for (int j = 0; j < revertCipher.length(); j++) {
            // Check alphabetic characters. Do nothing if non alphabetic character
            char cur = revertCipher.charAt(j);
            // Skip spaces between each pair
            if (cur != ' ') {
                // Deal with special character 'X'
                if (cur == 'X') {
                    // 'X' is the last word, ignore and end the process
                    if (j + 1 == revertCipher.length()) {
                        break;
                    }
                    // Check the meaningless character in cipher
                    else if (j - 1 != -1 && revertCipher.charAt(j - 1) != ' ') {
                        continue;
                    }
                }
                plaintext += cur;
            }
        }

        return plaintext;
    }

}