import java.util.*;
import java.io.*;

public class Program {
    public static void main(String[] args) throws Exception {
        //debug();
        //args = new String[1];
        //args[0] = "C:\\Users\\MSi\\Documents\\Ionvop\\Documents\\VSCode Projects\\majdata converter\\test.txt";

        String data = "";

        switch (args.length) {
            case 0:
                print("Simai Plus transpiler 2023 Ionvop");
                break;
            case 1:
                data = readFile(args[0]);
                break;
        }

        String result = convertSimai(data);
        File file = new File("output.txt");
        file.createNewFile();
        FileWriter fileWrite = new FileWriter("output.txt");
        fileWrite.write(result);
        fileWrite.close();
    }

    public static String directory = System.getProperty("user.dir");

    public static String convertSimai(String data) {
        String[] dataLines = data.split("\\R");
        String result;
        double bpm = Integer.parseInt(data.substring(data.indexOf('(') + 1, data.indexOf(')')));
        int measure = 1;
        int originalMeasure = 0;
        int level = 0;
        int alteredMeasureLevel = 0;

        for (int i = 0; i < dataLines.length; i++) {
            for (int j = 0; j < dataLines[i].length(); j++) {
                char element = dataLines[i].charAt(j);

                switch (element) {
                    case '{':
                        if (level != 0) {
                            measure *= 2;
                        }
            
                        level++;

                        if (j < dataLines[i].length() - 1) {
                            if (dataLines[i].charAt(j + 1) == '-') {
                                originalMeasure = measure;
                                alteredMeasureLevel = level;
                                measure -= countContinuousChar(dataLines[i], j + 1);
                                dataLines[i] = replaceIndex(dataLines[i], j + 1, j + countContinuousChar(dataLines[i], j + 1) + 1, "");
                            }
                        }

                        result = "{" + measure + "}";
                        dataLines[i] = replaceIndex(dataLines[i], j, result);
                        j += result.length() - 1;
                        break;
                    case '}':
                        level--;

                        if (originalMeasure != 0 && level + 1 == alteredMeasureLevel) {
                            measure = originalMeasure / 2;
                            originalMeasure = 0;
                            alteredMeasureLevel = 0;
                        } else {
                            measure /= 2;
                        }

                        result = "{" + measure + "}";
                        dataLines[i] = replaceIndex(dataLines[i], j, result);
                        j += result.length() - 1;
                        break;
                    case '[':
                        String slide = dataLines[i].substring(j + 1, getCharIndex(dataLines[i], j, ']'));

                        if (slide.contains("###")) {
                            slide = convertSlide(slide, bpm);
                        }

                        dataLines[i] = replaceIndex(dataLines[i], j + 1, getCharIndex(dataLines[i], j, ']'), slide);
                        j += slide.length() + 1;

                        break;
                    case '/':
                        if (dataLines[i].substring(j, j + 2).equals("//")) {
                            dataLines[i] = dataLines[i].substring(0, j);
                        }

                        break;
                }
            }
        }

        result = "";

        for (String element : dataLines) {
            result += element + "\r\n";
        }

        return result;
    }

    public static int getCharIndex(String input, int start, char end) {
        for (int i = start + 1; i < input.length(); i++) {
            if (input.charAt(i) == end) {
                return i;
            }
        }

        return -1;
    }

    public static String convertSlide(String input, double bpm) {
        double beat = 240.0 / bpm;
        String[] data = input.split("###");
        int numerator = Integer.parseInt(data[0].split(":")[1]);
        int denominator = Integer.parseInt(data[0].split(":")[0]);
        double wait = beat * ((numerator + 0.0) / (denominator + 0.0));
        wait = Math.floor((wait * 1000)) / 1000;
        numerator = Integer.parseInt(data[1].split(":")[1]);
        denominator = Integer.parseInt(data[1].split(":")[0]);
        double length = beat * ((numerator + 0.0) / (denominator + 0.0));
        length = Math.floor((length * 1000)) / 1000;
        return wait + "##" + length;
    }

    public static String replaceIndex(String input, int start, int end, String replace) {
        return input.substring(0, start) + replace + input.substring(end);
    }

    public static String replaceIndex(String input, int index, String replace) {
        return input.substring(0, index) + replace + input.substring(index + 1);
    }

    public static int countContinuousChar(String input, int index) {
        int result = 0;
        char find = input.charAt(index);

        while (input.charAt(index) == find) {
            result++;
            index++;

            if (index >= input.length()) {
                break;
            }
        }

        return result;
    }

    public static String readFile(String path) throws Exception {
        File file = new File(path);
        Scanner sc = new Scanner(file);
        sc.useDelimiter("\\Z");
        String result = sc.next();
        sc.close();
        return result;
    }

    public static void print(String message) {
        System.out.println(message);
    }

    public static void breakpoint(String message) {
        print(message);
        System.exit(0);
    }

    public static void debug() {
        String test = "Hello, world!";
        String test2 = replaceIndex(test, 2, 6, "foo");
        breakpoint(test2);
        System.exit(0);
    }
}