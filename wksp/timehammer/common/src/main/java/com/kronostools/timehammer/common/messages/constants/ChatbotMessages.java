package com.kronostools.timehammer.common.messages.constants;

import com.kronostools.timehammer.common.utils.CommonUtils;

public class ChatbotMessages {
    public static final String[] QUESTION_START = new String[] {
            "Ya va siendo hora de ponerse a currar no?"
    };
    public static final String QUESTION_START_OPTION_Y = "Se ha registrado correctamente la entrada.";

    public static final String[] QUESTION_LUNCH_START = new String[] {
            "Se come o qué?"
    };
    public static final String QUESTION_LUNCH_START_OPTION_Y = "Se ha registrado correctamente la pausa para la comida.";

    public static final String[] QUESTION_LUNCH_RESUME = new String[] {
            "Has terminado ya de comer?"
    };
    public static final String QUESTION_LUNCH_RESUME_OPTION_Y = "Se ha registrado correctamente el final de la pausa para la comida.";

    public static final String[] QUESTION_END = new String[] {
            "Habrá que irse ya para casa no?"
    };
    public static final String QUESTION_END_OPTION_Y = "Se ha registrado correctamente la salida.";

    public static final String ANSWER_Y = "La acción se ha registrado correctamente.";
    public static final String ANSWER_Y_BUTTON = "Sí";
    public static final String ANSWER_N5M = "Anotado queda. Dentro de 5 minutos te lo volveré a recordar.";
    public static final String ANSWER_N5M_BUTTON = "+5 m";
    public static final String ANSWER_N10M = "Anotado queda. Dentro de 10 minutos te lo volveré a recordar.";
    public static final String ANSWER_N10M_BUTTON = "+10 m";
    public static final String ANSWER_N15M = "Anotado queda. Dentro de 15 minutos te lo volveré a recordar.";
    public static final String ANSWER_N15M_BUTTON = "+15 m";
    public static final String ANSWER_N20M = "Anotado queda. Dentro de 20 minutos te lo volveré a recordar.";
    public static final String ANSWER_N20M_BUTTON = "+20 m";
    public static final String ANSWER_N = "Anotado queda. Hasta el próximo día laborable no se te volverá a notificar de lo mismo.";
    public static final String ANSWER_N_BUTTON = "No";

    public static final String COMMAND_REGISTRATION_REQUIRED = "El commando que intentas ejecutar requiere estar registrado. /register para registrarse.";
    public static final String COMMAND_UNRECOGNIZED = "No sé qué hacer con ese mensaje recibido. /help si necesitas ayuda sobre cómo usar este chatbot.";
    public static final String COMMAND_MISSING_REGISTERED = "No sé qué hacer con ese mensaje recibido. /help si necesitas ayuda sobre cómo usar este chatbot.";
    public static final String COMMAND_MISSING_UNREGISTERED = "No sé quién eres. Es necesario /register para usar este chatbot. /help para obtener más información.";

    public static final String COMMAND_START_UNREGISTERED = "¡Bienvenido! Este chatbot te facilitará la vida para cumplimentar los fichajes. Será necesario un registro para poder usarlo, por favor, envíe /register para registrarse. Para obtener más información sobre cómo funciona este chatbot, envíe /help.";
    public static final String COMMAND_START_REGISTERED = "Hola de nuevo. ¿Te acuerdas de mi? Para obtener más información sobre cómo funciona este chatbot, envíe /help.";
    private static final String COMMAND_UNREGISTER_INIT = "Para cancelar el registro accede [aquí]({}). Envía /register para volver a registrarse de nuevo.";
    private static final String COMMAND_REGISTER_INIT = "Para continuar con el registro accede [aquí]({})";
    private static final String COMMAND_REGISTER_REGISTERED = "Ya se ha hecho un registro para el usuario {}. Para registrarse con otro usurio, primero envía /unregister para cancelar el registro y a continuación, envía /register para registrarse de nuevo.";
    private static final String COMMAND_HELP = "Para conocer cómo funciona este chatbot accede al [FAQ]({}).";
    private static final String COMMAND_UPDATE_SETTINGS_INIT = "Para actualizar tus preferencias accede [aquí]({})";
    private static final String COMMAND_UPDATE_PASSWORD_INIT = "Para establecer la contraseña accede a [Establecer contraseña]({}).";
    public static final String COMMAND_UNIMPLEMENTED = "Este chatbot todavía se encuentra en fase de desarrollo. Esta funcionalidad todavía no está disponible.";

    public static final String SUCCESSFUL_REGISTRATION = "¡Enhorabuena! El registro se ha realizado satisfactoriamente. Si en algún momento quieres cancelar el registro, envía /unregister. Para conocer cómo funciona esto, envía /help.";

    public static final String MISSING_PASSWORD = "Necesitamos que vuelvas a establecer tu contraseña. Envía /updatePassword para establecerla. Lamentamos las molestias ocasionadas. Para más información sobre cómo se almacenan las contraseñas envía /help";

    public static String COMMAND_UNREGISTER_INIT(final String registrationUrl) {
        return CommonUtils.stringFormat(COMMAND_UNREGISTER_INIT, registrationUrl);
    }

    public static String COMMAND_UPDATE_SETTINGS_INIT(final String registrationUrl) {
        return CommonUtils.stringFormat(COMMAND_UPDATE_SETTINGS_INIT, registrationUrl);
    }

    public static String COMMAND_REGISTER_INIT(final String registrationUrl) {
        return CommonUtils.stringFormat(COMMAND_REGISTER_INIT, registrationUrl);
    }

    public static String COMMAND_REGISTER_REGISTERED(final String workerExernalId) {
        return CommonUtils.stringFormat(COMMAND_REGISTER_REGISTERED, workerExernalId);
    }

    public static String COMMAND_HELP(final String helpUrl) {
        return CommonUtils.stringFormat(COMMAND_HELP, helpUrl);
    }

    public static String COMMAND_UPDATE_PASSWORD_INIT(final String updatePasswordUrl) {
        return CommonUtils.stringFormat(COMMAND_UPDATE_PASSWORD_INIT, updatePasswordUrl);
    }
}