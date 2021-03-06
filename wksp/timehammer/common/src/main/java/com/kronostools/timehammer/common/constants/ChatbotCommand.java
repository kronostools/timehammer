package com.kronostools.timehammer.common.constants;

import java.util.Optional;
import java.util.stream.Stream;

public enum ChatbotCommand {
    START("/start", "", Boolean.FALSE, Boolean.FALSE),
    HELP("/help", "Obtener ayuda", Boolean.TRUE, Boolean.FALSE),
    REGISTER("/register", "Registrarse", Boolean.TRUE, Boolean.FALSE),
    UNREGISTER("/unregister", "Cancelar el registro", Boolean.TRUE, Boolean.TRUE),
    UPDATE_PASSWORD("/update_password", "Actualizar la contraseña", Boolean.TRUE, Boolean.TRUE),
    UPDATE_SETTINGS("/update_settings", "Cambiar la configuración", Boolean.TRUE, Boolean.TRUE),
    TODAY_SETTINGS("/today_settings", "Ver la configuración para hoy", Boolean.TRUE, Boolean.TRUE);


    private final String commandText;
    private final String commandDescription;
    private final Boolean visible;
    private final Boolean authenticationRequired;

    ChatbotCommand(final String commandText, final String commandDescription, final Boolean visible, final Boolean authenticationRequired) {
        this.commandText = commandText;
        this.commandDescription = commandDescription;
        this.visible = visible;
        this.authenticationRequired = authenticationRequired;
    }

    public String getCommandText() {
        return commandText;
    }

    public String getDescription() {
        return commandDescription;
    }

    public Boolean isVisible() { return visible; }

    public Boolean isAuthenticationRequired() {
        return authenticationRequired;
    }

    public static Optional<ChatbotCommand> fromCommandText(final String commandText) {
        return Stream.of(ChatbotCommand.values())
                .filter(c -> c.getCommandText().equals(commandText))
                .findFirst();
    }
}
