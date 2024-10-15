package com.telebot.common;

import java.util.Random;

public class PhraseGenerator {

    private static final String[] helloPhrases = {
            "Well, look who it is! The legend returns.",
            "Welcome back! Did you bring any cookies?",
            "Long time no see! Where have you been hiding?",
            "Is it a bird? Is it a plane? No, it's you!",
            "You've arrived! We've been expecting you.",
            "Alert! VIP access granted. Welcome aboard!",
            "Greetings! Your favorite app has been patiently waiting for you.",
    };

    private static final String[] buttonPhrases = {
            "_Did you lose your way to the button?_",
            "_Hmm.. You're apparently not very smart. Click the button._",
            "_I see you're avoiding the button. It doesn't bite._",
            "_The button is feeling lonely. Maybe give it a click?_",
            "_Buttons don't press themselves... yet._",
            "_Ignoring the button won't make it go away!_",
            "_Are we playing 'hard to get' with the button?_",
            "_The button called—it misses you._",
            "_So close to the button, yet so far._",
            "_Button pressing: highly recommended for the full experience._",
            "_Is the button invisible? Oh wait, it's right there!_"
    };

    private static final String[] fPhrases = {
            "_I see you're fluent in 'Expressive Vocabulary'._",
            "_Such passion! Now, how about channeling that energy into studying?_",
            "_Let's save the sailor talk for the high seas._",
            "_Keyboard acting up? It seems to be adding extra words._",
            "_If frustration could press buttons, we'd be golden._",
            "_I appreciate your enthusiasm, but let's tone down the language._",
            "_Such expressive language! Ever considered poetry?_",
            "_Interesting word choice. Care to rephrase that?_",
            "_Expressive, aren't we? Let's try a different approach._",
    };


    private static final Random random = new Random();

    public static String getRandomButtonPhrase() {
        int index = random.nextInt(buttonPhrases.length);
        return buttonPhrases[index];
    }

    public static String getRandomFWordPhrase() {
        int index = random.nextInt(fPhrases.length);
        return fPhrases[index];
    }

    public static String getRandomHalloPhrase() {
        int index = random.nextInt(helloPhrases.length);
        return helloPhrases[index];
    }


}
