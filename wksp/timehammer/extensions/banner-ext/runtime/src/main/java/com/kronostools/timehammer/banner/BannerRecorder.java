package com.kronostools.timehammer.banner;

import io.quarkus.runtime.annotations.Recorder;

@Recorder
public class BannerRecorder {

    public void print() {
        System.err.println("  _______ _                _    _                                     ");
        System.err.println(" |__   __(_)              | |  | |                                    ");
        System.err.println("    | |   _ _ __ ___   ___| |__| | __ _ _ __ ___  _ __ ___   ___ _ __ ");
        System.err.println("    | |  | | '_ ` _ \\ / _ \\  __  |/ _` | '_ ` _ \\| '_ ` _ \\ / _ \\ '__|");
        System.err.println("    | |  | | | | | | |  __/ |  | | (_| | | | | | | | | | | |  __/ |   ");
        System.err.println("    |_|  |_|_| |_| |_|\\___|_|  |_|\\__,_|_| |_| |_|_| |_| |_|\\___|_|   ");
        System.err.println("                                                                      ");
    }
}
