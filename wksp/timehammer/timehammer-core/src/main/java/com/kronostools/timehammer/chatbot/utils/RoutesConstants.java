package com.kronostools.timehammer.chatbot.utils;

public class RoutesConstants {

    public class Headers {
        public static final String TIMESTAMP = "timestamp";
        public static final String WORKER = "worker";
        public static final String WORKER_CURRENT_PREFERENCES = "worker-current-preferences";
        public static final String COMMAND_PRESENT = "command-present";
        public static final String COMMAND_RECOGNIZED = "command-recognized";
        public static final String COMMAND = "command";
        public static final String LOGGED_IN = "logged-in";
        public static final String QUESTION = "question";
        public static final String ANSWER = "answer";
    }

    public class Messages {
        public static final String DEMO_MODE_PREFIX = "*(demo mode)*\n";
    }

    public class Commands {
        public static final String COMMAND_START = "/";
    }

    public class Answers {
        public static final String SEPARATOR = "#";
    }
}