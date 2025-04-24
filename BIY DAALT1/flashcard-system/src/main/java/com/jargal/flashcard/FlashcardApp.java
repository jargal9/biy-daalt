package com.jargal.flashcard;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class FlashcardApp {
    private static final String HELP_MESSAGE =
            "Usage: flashcard <cards-file> [options]\n" +
            "Options:\n" +
            "  --help                         Тусламжийн мэдээлэл харуулах\n" +
            "  --order <order>                Зохион байгуулалтын төрөль default-аар random сонгогдсон байгаа.\n" +
            "                                 [Сонголтууд: \"random\", \"worst-first\", \"recent-mistakes-first\"]\n" +
            "  --repetitions <num>            Нэг картыг хэдэн удаа зөв хариулахыг шаардлага болгож тохируулна.\n" +
            "                                 Хэрвээ тохируулаагүй бол зөвхөн нэг удаа асууна.\n" +
            "  --invertCards                  Тохиргоо идэвхэжсэн бол картын асуулт, хариултыг сольж харуулна.\n" +
            "                                 Default: false";

    public static void main(String[] args) {
        String cardFilePath = null;
        String order = "random";
        int repetitions = 1;
        boolean invertCards = false;
        boolean showHelp = false;

        for (int i = 0; i < args.length; i++) {
            switch (args[i]) {
                case "--help":
                    showHelp = true;
                    break;
                case "--order":
                    if (i + 1 < args.length) {
                        order = args[++i];
                        if (!order.equals("random") && !order.equals("worst-first") && !order.equals("recent-mistakes-first")) {
                            System.err.println("Invalid order option: " + order);
                            System.err.println("Valid options are: \"random\", \"worst-first\", \"recent-mistakes-first\"");
                            System.exit(1);
                        }
                    } else {
                        System.err.println("Missing value for --order option");
                        System.exit(1);
                    }
                    break;
                case "--repetitions":
                    if (i + 1 < args.length) {
                        try {
                            repetitions = Integer.parseInt(args[++i]);
                            if (repetitions < 1) {
                                System.err.println("Repetitions must be at least 1");
                                System.exit(1);
                            }
                        } catch (NumberFormatException e) {
                            System.err.println("Invalid number for repetitions: " + args[i]);
                            System.exit(1);
                        }
                    } else {
                        System.err.println("Missing value for --repetitions option");
                        System.exit(1);
                    }
                    break;
                case "--invertCards":
                    invertCards = true;
                    break;
                default:
                    if (args[i].startsWith("--")) {
                        System.err.println("Unknown option: " + args[i]);
                        System.exit(1);
                    } else if (cardFilePath == null) {
                        cardFilePath = args[i];
                    } else {
                        System.err.println("Unexpected argument: " + args[i]);
                        System.exit(1);
                    }
            }
        }

        if (showHelp || cardFilePath == null) {
            System.out.println(HELP_MESSAGE);
            System.exit(0);
        }

        List<Card> cards = null;
        try {
            cards = loadCards(cardFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (cards.isEmpty()) {
            System.out.println("File hooson baina.");
            System.exit(0);
        }

        CardOrganizer organizer = getOrganizer(order);

        startFlashcardSession(cards, organizer, repetitions, invertCards);
    }

    private static List<Card> loadCards(String filePath) throws IOException {
        List<Card> cards = new ArrayList<>();
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        for (int i = 0; i < lines.size(); i += 2) {
            if (i + 1 < lines.size()) {
                String question = lines.get(i).trim();
                String answer = lines.get(i + 1).trim();
                if (!question.isEmpty() && !answer.isEmpty()) {
                    cards.add(new Card(question, answer));
                }
            }
        }
        return cards;
    }

    private static CardOrganizer getOrganizer(String order) {
        switch (order) {
            case "worst-first":
                return new WorstFirstSorter();
            case "recent-mistakes-first":
                return new RecentMistakesFirstSorter();
            case "random":
            default:
                return new RandomSorter();
        }
    }

    private static void startFlashcardSession(List<Card> cards, CardOrganizer organizer, int repetitions, boolean invertCards) {
        Scanner scanner = new Scanner(System.in);
        AchievementTracker achievementTracker = new AchievementTracker();
        boolean continuePractice = true;
        int round = 1;

        while (continuePractice) {
            System.out.println("\n=== Toirog " + round + " ===");
            List<Card> organizedCards = organizer.organize(cards);
            int correctAnswersInRound = 0;

            for (Card card : organizedCards) {
                String front = invertCards ? card.getAnswer() : card.getQuestion();
                String back = invertCards ? card.getQuestion() : card.getAnswer();

                System.out.println("\nCard: " + front);
                System.out.print("Tanii hariult: ");
                String userAnswer = scanner.nextLine().trim();

                card.incrementAttempts();

                boolean correct = userAnswer.equalsIgnoreCase(back);
                card.setAnsweredCorrectlyLastTime(correct);

                if (correct) {
                    System.out.println("Hariult zov baina!");
                    card.incrementCorrectAnswers();
                    correctAnswersInRound++;
                } else {
                    System.out.println("Hariult buruu baina!. Zov hariult ni: " + back);
                }

                if (card.getCorrectAnswers() >= repetitions) {
                    System.out.println("Ta ene cardiig buren ezemshjee! (Zov hariulsan too: " + card.getCorrectAnswers() + "/" + repetitions + ")");
                } else {
                    System.out.println("Yvts: " + card.getCorrectAnswers() + "/" + repetitions + " zov hariulsan baina.");
                }
            }

            System.out.println("\n=== Amjiltuud ===");
            achievementTracker.checkAchievements(cards);
            achievementTracker.displayAchievements();

            boolean allMastered = true;
            for (Card card : cards) {
                if (card.getCorrectAnswers() < repetitions) {
                    allMastered = false;
                    break;
                }
            }

            if (allMastered) {
                System.out.println("\nTa buh kartiig buren ezemshlee!");
                continuePractice = false;
            } else {
                System.out.print("\nUrgeljluuleh uu? (y/n): ");
                String response = scanner.nextLine().trim().toLowerCase();
                continuePractice = response.equals("y") || response.equals("yes");

                if (continuePractice) {
                    System.out.print("Orderoo oorchloh uu? (random/worst/recent/ugui): ");
                    String orderInput = scanner.nextLine().trim().toLowerCase();
                    switch (orderInput) {
                        case "random":
                            organizer = new RandomSorter();
                            break;
                        case "worst":
                            organizer = new WorstFirstSorter();
                            break;
                        case "recent":
                            organizer = new RecentMistakesFirstSorter();
                            break;
                        case "ugui":
                            break;
                        default:
                            System.out.println("Zov songolt oruulna uu.");
                    }
                }
            }

            round++;
        }

        scanner.close();
    }
}
