package com.jargal.flashcard;

import java.util.ArrayList;
import java.util.List;

public class AchievementTracker {
    private List<Achievement> unlockedAchievements;

    public AchievementTracker() {
        this.unlockedAchievements = new ArrayList<>();
    }

    public void checkAchievements(List<Card> cardsThisRound) {
        if (cardsThisRound.isEmpty()) return;

        boolean allCorrectThisRound = true;
        for (Card card : cardsThisRound) {
            if (!card.isAnsweredCorrectlyLastTime()) {
                allCorrectThisRound = false;
                break;
            }
        }
        if (allCorrectThisRound) {
            addAchievementIfNotExists(Achievement.CORRECT);
        }

        for (Card card : cardsThisRound) {
            if (card.getAttempts() > 5) {
                addAchievementIfNotExists(Achievement.REPEAT);
                break;
            }
        }

        for (Card card : cardsThisRound) {
            if (card.getCorrectAnswers() >= 3) {
                addAchievementIfNotExists(Achievement.CONFIDENT);
                break;
            }
        }
    }

    private void addAchievementIfNotExists(Achievement achievement) {
        if (!unlockedAchievements.contains(achievement)) {
            unlockedAchievements.add(achievement);
        }
    }

    public List<Achievement> getUnlockedAchievements() {
        return new ArrayList<>(unlockedAchievements);
    }

    public void displayAchievements() {
        if (unlockedAchievements.isEmpty()) {
            System.out.println("Odoogoor ymar negen amjilt baihgui baina.");
            return;
        }

        for (Achievement achievement : unlockedAchievements) {
            System.out.println("- " + achievement);
        }
    }
}
