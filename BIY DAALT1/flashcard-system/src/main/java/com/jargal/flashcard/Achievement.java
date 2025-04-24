package com.jargal.flashcard;

public enum Achievement {
    CORRECT("Suuliin toirogt buh kart zov hariulsan."),
    REPEAT("Neg kartad 5s olon udaa hariulsan."),
    CONFIDENT("Neg kartad dor hayj 3 udaa zov hariulsan.");

    private final String description;

    Achievement(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return name() + ": " + description;
    }
}

