package com.jargal.flashcard;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AchievementTracker {
    private Set<Achievement> unlockedAchievements;

    public AchievementTracker() {
        this.unlockedAchievements = new HashSet<>();
    }

    public void checkAchievements(List<Card> cardsThisRound, long totalTimeInSeconds) {
        if (cardsThisRound.isEmpty()) return;
    
        boolean allCorrectThisRound = true;
        for (Card card : cardsThisRound) {
            if (!card.isAnsweredCorrectlyLastTime()) {
                allCorrectThisRound = false;
                break;
            }
        }
        if (allCorrectThisRound) {
            unlockedAchievements.add(Achievement.CORRECT);
        }
    
        for (Card card : cardsThisRound) {
            if (card.getAttempts() > 5) {
                unlockedAchievements.add(Achievement.REPEAT);
                break; 
            }
        }
    
        for (Card card : cardsThisRound) {
            if (card.getCorrectAnswers() >= 3) {
                unlockedAchievements.add(Achievement.CONFIDENT);
                break;
            }
        }
    }
    

    public Set<Achievement> getUnlockedAchievements() {
        return new HashSet<>(unlockedAchievements);
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