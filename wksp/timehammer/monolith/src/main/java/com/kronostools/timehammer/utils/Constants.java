package com.kronostools.timehammer.utils;

public class Constants {
    public static final String NO_SSID = "NA";

    public static final Long WORKER_STATUS_INITIAL_AVG_PROCESSING_TIME = 1000L;
    public static final String HIDDEN_PASSWORD = "*****";
    public static final String DEMO_PASSWORD = "demo";
    public static final String LINE_BREAK = "\n";
    public static final String TAB = "\t";
    public static final int DEFAULT_TEXT_TRUNCATE_WIDTH = 100;

    public class Caches {
        public static final String COMUNYTEK_SESSION = "comunytek-session-cache";
        public static final String COMUNYTEK_STATUS = "comunytek-status-cache";
        public static final String WORKER_BY_INTERNAL_ID_AS_WORKERVO = "worker-by-internalid-as-workervo-cache";
        public static final String WORKER_BY_CHATID_AS_WORKERVO = "worker-by-chatid-as-workervo-cache";
        public static final String ALL_WORKERS_CURRENT_PREFERENCES = "all-workers-current-preferences-cache";
        public static final String WORKER_PREFERENCES = "worker-preferences-cache";
        public static final String WORKER_CURRENT_PREFERENCES = "worker-current-preferences-cache";
        public static final String WORKER_HOLIDAYS = "worker-holidays-cache";
        public static final String WORKER_CHATS = "worker-chats-cache";
        public static final String CITY_LIST = "city-list-cache";
        public static final String CITY_BY_CODE = "city-by-code-cache";
        public static final String CITY_HOLIDAYS = "city-holidays-cache";
    }

    public class Buses {
        public static final String UPDATE_WORKER_SSID_TRACKING_INFO = "updateWorkerSsidTrackingInfo";
        public static final String ADD_SSID_TRACKING_EVENT = "addSsidTrackingEvent";
        public static final String ADD_TRASH_MESSAGE = "addTrashMessage";
    }

    public class GenericErrors {
        public static final String INCORRECT_CREDENTIALS = "Contraseña no válida.";
        public static final String UNEXPEDTED_ERROR = "Ha ocurrido un error inesperado mientras procesábamos su solicitud de registro, por favor, inténtelo de nuevo";
    }

    public class RegistrationFormFields {
        public static final String EXTERNAL_PASSWORD = "externalPassword";
    }

    public class RegistrationErrorMessages {
        public static final String REGISTRATION_EXPIRED = "La solicitud de registro ha expirado, por favor, vuelva a iniciar otra.";
    }

    public class UpdatePasswordFormFields {
        public static final String EXTERNAL_PASSWORD = "externalPassword";
    }
}
