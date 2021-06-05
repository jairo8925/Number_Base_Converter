package converter;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.*;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.print("Enter two numbers in format: {source base} {target base} (To quit type /exit): ");
            String command = scanner.nextLine();
            String[] commands = command.trim().split("\\s+");
            if ("/exit".equals(command)) {
                scanner.close();
                System.exit(0);
            } else {
                int sourceBase = Integer.parseInt(commands[0]);
                int targetBase = Integer.parseInt(commands[1]);
                while (true) {
                    System.out.print("Enter number in base " + sourceBase + " to convert to base " + targetBase + " (To go back type /back): ");
                    String input = scanner.nextLine();
                    if (input.equals("/back")) {
                        break;
                    } else {
                        String value = "";
                        if (input.contains(".")) {
                            String[] numbers = input.split("\\.");
                            BigInteger integer = new BigInteger(numbers[0], sourceBase);

                            String fraction = convertToDecimalFraction(numbers[1], sourceBase);
                            if (targetBase != 10) {
                                fraction = convertToAnyBaseFraction(fraction, targetBase);
                            }

                            value += integer.toString(targetBase);
                            System.out.println("Conversion result: " + value + "." + fraction);
                        } else {
                            BigInteger integer = new BigInteger(input, sourceBase);
                            value = integer.toString(targetBase);
                            System.out.println("Conversion result: " + value);
                        }
                    }
                }
            }
        }
    }

    public static String convertToDecimalFraction(String number, int base) {
        BigInteger integer = new BigInteger(number, base);
        // if fractional part is zero, then just return five zeroes
        if (integer.compareTo(BigInteger.ZERO) == 0) {
            return "00000";
        }
        BigDecimal result = BigDecimal.ZERO;
        for (int i = 0; i < number.length(); i++) {
            // number.charAt(0) * base^(-1) + number.charAt(1) * base^(-2) + ... + number.charAt(number.length()-1) * base^(number.length()-1)
            result = result.add((new BigDecimal(Character.getNumericValue(number.charAt(i))).divide(new BigDecimal(base).pow(i+1), 20, RoundingMode.HALF_UP)));
        }
        // we only want the numbers after the "."
        return result.toString().split("\\.")[1];
    }

    public static String convertToAnyBaseFraction(String number, int base) {
        number = "0." + number;
        StringBuilder result = new StringBuilder();
        BigDecimal num = new BigDecimal(number);
        int counter = 0;
        while (num.scale() > 0 || num.stripTrailingZeros().scale() > 0) {
            // we only want five digits in the fractional part
            if (counter == 5) {
                break;
            }
            num = num.multiply(new BigDecimal(base));
            num = num.setScale(5, RoundingMode.HALF_UP);
            result.append(num.toBigInteger().toString(base));
            num = num.subtract(new BigDecimal(num.toBigInteger()));
            counter++;
        }
        return result.toString();
    }

}
