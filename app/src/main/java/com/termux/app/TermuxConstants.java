package com.termux.app;

import android.annotation.SuppressLint;

import java.io.File;

/*
 * Version: v0.11.0
 *
 * Changelog
 *
 * - 0.1.0 (2021-03-08)
 *      - Initial Release.
 *
 * - 0.2.0 (2021-03-11)
 *      - Added `_DIR` and `_FILE` substrings to paths.
 *      - Added `INTERNAL_PRIVATE_APP_DATA_DIR*`, `TERMUX_CACHE_DIR*`, `TERMUX_DATABASES_DIR*`,
 *          `TERMUX_SHARED_PREFERENCES_DIR*`, `TERMUX_BIN_PREFIX_DIR*`, `TERMUX_ETC_DIR*`,
 *          `TERMUX_INCLUDE_DIR*`, `TERMUX_LIB_DIR*`, `TERMUX_LIBEXEC_DIR*`, `TERMUX_SHARE_DIR*`,
 *          `TERMUX_TMP_DIR*`, `TERMUX_VAR_DIR*`, `TERMUX_STAGING_PREFIX_DIR*`,
 *          `TERMUX_STORAGE_HOME_DIR*`, `TERMUX_DEFAULT_PREFERENCES_FILE_BASENAME*`,
 *          `TERMUX_DEFAULT_PREFERENCES_FILE`.
 *      - Renamed `DATA_HOME_PATH` to `TERMUX_DATA_HOME_DIR_PATH`.
 *      - Renamed `CONFIG_HOME_PATH` to `TERMUX_CONFIG_HOME_DIR_PATH`.
 *      - Updated javadocs and spacing.
 *
 * - 0.3.0 (2021-03-12)
 *      - Remove `TERMUX_CACHE_DIR_PATH*`, `TERMUX_DATABASES_DIR_PATH*`,
 *          `TERMUX_SHARED_PREFERENCES_DIR_PATH*` since they may not be consistent on all devices.
 *      - Renamed `TERMUX_DEFAULT_PREFERENCES_FILE_BASENAME` to
 *          `TERMUX_DEFAULT_PREFERENCES_FILE_BASENAME_WITHOUT_EXTENSION`. This should be used for
 *           accessing shared preferences between Termux app and its plugins if ever needed by first
 *           getting shared package context with {@link Context.createPackageContext(String,int}).
 *
 * - 0.4.0 (2021-03-16)
 *      - Added `BROADCAST_TERMUX_OPENED`,
 *          `TERMUX_API_DEFAULT_PREFERENCES_FILE_BASENAME_WITHOUT_EXTENSION`
 *          `TERMUX_BOOT_DEFAULT_PREFERENCES_FILE_BASENAME_WITHOUT_EXTENSION`,
 *          `TERMUX_FLOAT_DEFAULT_PREFERENCES_FILE_BASENAME_WITHOUT_EXTENSION`,
 *          `TERMUX_STYLING_DEFAULT_PREFERENCES_FILE_BASENAME_WITHOUT_EXTENSION`,
 *          `TERMUX_TASKER_DEFAULT_PREFERENCES_FILE_BASENAME_WITHOUT_EXTENSION`,
 *          `TERMUX_WIDGET_DEFAULT_PREFERENCES_FILE_BASENAME_WITHOUT_EXTENSION`.
 *
 * - 0.5.0 (2021-03-16)
 *      - Renamed "Termux Plugin app" labels to "Termux Tasker app".
 *
 * - 0.6.0 (2021-03-16)
 *      - Added `TERMUX_FILE_SHARE_URI_AUTHORITY`.
 *
 * - 0.7.0 (2021-03-17)
 *      - Fixed javadocs.
 *
 * - 0.8.0 (2021-03-18)
 *      - Fixed Intent extra types javadocs.
 *      - Added following to `TERMUX_SERVICE`:
 *          `EXTRA_PENDING_INTENT`, `EXTRA_RESULT_BUNDLE`,
 *          `EXTRA_STDOUT`, `EXTRA_STDERR`, `EXTRA_EXIT_CODE`,
 *          `EXTRA_ERR`, `EXTRA_ERRMSG`.
 *
 * - 0.9.0 (2021-03-18)
 *      - Fixed javadocs.
 *
 * - 0.10.0 (2021-03-19)
 *      - Added following to `TERMUX_SERVICE`:
 *          `EXTRA_SESSION_ACTION`,
 *          `VALUE_EXTRA_SESSION_ACTION_SWITCH_TO_NEW_SESSION_AND_OPEN_ACTIVITY`,
 *          `VALUE_EXTRA_SESSION_ACTION_KEEP_CURRENT_SESSION_AND_OPEN_ACTIVITY`,
 *          `VALUE_EXTRA_SESSION_ACTION_SWITCH_TO_NEW_SESSION_AND_DONT_OPEN_ACTIVITY`
 *          `VALUE_EXTRA_SESSION_ACTION_KEEP_CURRENT_SESSION_AND_DONT_OPEN_ACTIVITY`.
 *      - Added following to `RUN_COMMAND_SERVICE`:
 *          `EXTRA_SESSION_ACTION`.
 *
 * - 0.11.0 (2021-03-24)
 *      - Added following to `TERMUX_SERVICE`:
 *          `EXTRA_COMMAND_LABEL`, `EXTRA_COMMAND_DESCRIPTION`, `EXTRA_COMMAND_HELP`, `EXTRA_PLUGIN_API_HELP`.
 *      - Added following to `RUN_COMMAND_SERVICE`:
 *          `EXTRA_COMMAND_LABEL`, `EXTRA_COMMAND_DESCRIPTION`, `EXTRA_COMMAND_HELP`.
 *      - Updated `RESULT_BUNDLE` related extras with `PLUGIN_RESULT_BUNDLE` prefixes.
 */

/**
 * A class that defines shared constants of the Termux app and its plugins.
 * This class will be hosted by termux-app and should be imported by other termux plugin apps as is
 * instead of copying constants to random classes. The 3rd party apps can also import it for
 * interacting with termux apps. If changes are made to this file, increment the version number
 * and add an entry in the Changelog section above.
 *
 * Termux app default package name is "com.termux" and is used in {@link #TERMUX_PREFIX_DIR_PATH}.
 * The binaries compiled for termux have {@link #TERMUX_PREFIX_DIR_PATH} hardcoded in them but it
 * can be changed during compilation.
 *
 * The {@link #TERMUX_PACKAGE_NAME} must be the same as the applicationId of termux-app build.gradle
 * since its also used by {@link #TERMUX_FILES_DIR_PATH}.
 * If {@link #TERMUX_PACKAGE_NAME} is changed, then binaries, specially used in bootstrap need to be
 * compiled appropriately. Check https://github.com/termux/termux-packages/wiki/Building-packages
 * for more info.
 *
 * Ideally the only places where changes should be required if changing package name are the following:
 * - The {@link #TERMUX_PACKAGE_NAME} in {@link TermuxConstants}.
 * - The "applicationId" in "build.gradle". This is package name that android and app stores will
 *       use and is also the final package name stored in "AndroidManifest.xml".
 * - The "manifestPlaceholders" values for {@link #TERMUX_PACKAGE_NAME} and *_APP_NAME in "build.gradle".
 * - The "ENTITY" values for {@link #TERMUX_PACKAGE_NAME} and *_APP_NAME in "strings.xml".
 * - The "shortcut.xml" files like in termux-app since dynamic variables don't work in it.
 * - Optionally the "package" in "AndroidManifest.xml" if modifying project structure. This is
 *      package name for java classes project structure and is prefixed if activity and service
 *      names use dot (.) notation.
 * - Optionally the *_PATH variables in {@link TermuxConstants} containing the string "termux".
 *
 * Check https://developer.android.com/studio/build/application-id for info on "package" in
 * "AndroidManifest.xml" and "applicationId" in "build.gradle".
 *
 * {@link #TERMUX_PACKAGE_NAME} must be used in source code of Termux app and its plugins instead of
 * hardcoded "com.termux" paths.
 */
public final class TermuxConstants {

    /*
     * Termux and its plugin app and package names.
     */

    /** Termux app name */
    public static final String TERMUX_APP_NAME = "Termux"; // Default: "Termux"
    /** Termux app package name */
    public static final String TERMUX_PACKAGE_NAME = "com.termux"; // Default: "com.termux"


    /** Termux API app name */
    public static final String TERMUX_API_APP_NAME = "Termux:API"; // Default: "Termux:API"
    /** Termux API app package name */
    public static final String TERMUX_API_PACKAGE_NAME = TERMUX_PACKAGE_NAME + ".api"; // Default: "com.termux.api"


    /** Termux Boot app name */
    public static final String TERMUX_BOOT_APP_NAME = "Termux:Boot"; // Default: "Termux:Boot"
    /** Termux Boot app package name */
    public static final String TERMUX_BOOT_PACKAGE_NAME = TERMUX_PACKAGE_NAME + ".boot"; // Default: "com.termux.boot"


    /** Termux Float app name */
    public static final String TERMUX_FLOAT_APP_NAME = "Termux:Float"; // Default: "Termux:Float"
    /** Termux Float app package name */
    public static final String TERMUX_FLOAT_PACKAGE_NAME = TERMUX_PACKAGE_NAME + ".window"; // Default: "com.termux.window"


    /** Termux Styling app name */
    public static final String TERMUX_STYLING_APP_NAME = "Termux:Styling"; // Default: "Termux:Styling"
    /** Termux Styling app package name */
    public static final String TERMUX_STYLING_PACKAGE_NAME = TERMUX_PACKAGE_NAME + ".styling"; // Default: "com.termux.styling"


    /** Termux Tasker app name */
    public static final String TERMUX_TASKER_APP_NAME = "Termux:Tasker"; // Default: "Termux:Tasker"
    /** Termux Tasker app package name */
    public static final String TERMUX_TASKER_PACKAGE_NAME = TERMUX_PACKAGE_NAME + ".tasker"; // Default: "com.termux.tasker"


    /** Termux Widget app name */
    public static final String TERMUX_WIDGET_APP_NAME = "Termux:Widget"; // Default: "Termux:Widget"
    /** Termux Widget app package name */
    public static final String TERMUX_WIDGET_PACKAGE_NAME = TERMUX_PACKAGE_NAME + ".widget"; // Default: "com.termux.widget"





    /*
     * Termux app core directory paths.
     */

    /** Termux app internal private app data directory path */
    @SuppressLint("SdCardPath")
    public static final String INTERNAL_PRIVATE_APP_DATA_DIR_PATH = "/data/data/" + TERMUX_PACKAGE_NAME; // Default: "/data/data/com.termux"
    /** Termux app internal private app data directory */
    public static final File INTERNAL_PRIVATE_APP_DATA_DIR = new File(INTERNAL_PRIVATE_APP_DATA_DIR_PATH);



    /** Termux app Files directory path */
    public static final String TERMUX_FILES_DIR_PATH = INTERNAL_PRIVATE_APP_DATA_DIR_PATH + "/files"; // Default: "/data/data/com.termux/files"
    /** Termux app Files directory */
    public static final File TERMUX_FILES_DIR = new File(TERMUX_FILES_DIR_PATH);



    /** Termux app $PREFIX directory path */
    public static final String TERMUX_PREFIX_DIR_PATH = TERMUX_FILES_DIR_PATH + "/usr"; // Default: "/data/data/com.termux/files/usr"
    /** Termux app $PREFIX directory */
    public static final File TERMUX_PREFIX_DIR = new File(TERMUX_PREFIX_DIR_PATH);


    /** Termux app $PREFIX/bin directory path */
    public static final String TERMUX_BIN_PREFIX_DIR_PATH = TERMUX_PREFIX_DIR_PATH + "/bin"; // Default: "/data/data/com.termux/files/usr/bin"
    /** Termux app $PREFIX/bin directory */
    public static final File TERMUX_BIN_PREFIX_DIR = new File(TERMUX_BIN_PREFIX_DIR_PATH);


    /** Termux app $PREFIX/etc directory path */
    public static final String TERMUX_ETC_PREFIX_DIR_PATH = TERMUX_PREFIX_DIR_PATH + "/etc"; // Default: "/data/data/com.termux/files/usr/etc"
    /** Termux app $PREFIX/etc directory */
    public static final File TERMUX_ETC_DIR = new File(TERMUX_ETC_PREFIX_DIR_PATH);


    /** Termux app $PREFIX/include directory path */
    public static final String TERMUX_INCLUDE_PREFIX_DIR_PATH = TERMUX_PREFIX_DIR_PATH + "/include"; // Default: "/data/data/com.termux/files/usr/include"
    /** Termux app $PREFIX/include directory */
    public static final File TERMUX_INCLUDE_DIR = new File(TERMUX_INCLUDE_PREFIX_DIR_PATH);


    /** Termux app $PREFIX/lib directory path */
    public static final String TERMUX_LIB_PREFIX_DIR_PATH = TERMUX_PREFIX_DIR_PATH + "/lib"; // Default: "/data/data/com.termux/files/usr/lib"
    /** Termux app $PREFIX/lib directory */
    public static final File TERMUX_LIB_DIR = new File(TERMUX_LIB_PREFIX_DIR_PATH);


    /** Termux app $PREFIX/libexec directory path */
    public static final String TERMUX_LIBEXEC_PREFIX_DIR_PATH = TERMUX_PREFIX_DIR_PATH + "/libexec"; // Default: "/data/data/com.termux/files/usr/libexec"
    /** Termux app $PREFIX/libexec directory */
    public static final File TERMUX_LIBEXEC_DIR = new File(TERMUX_LIBEXEC_PREFIX_DIR_PATH);


    /** Termux app $PREFIX/share directory path */
    public static final String TERMUX_SHARE_PREFIX_DIR_PATH = TERMUX_PREFIX_DIR_PATH + "/share"; // Default: "/data/data/com.termux/files/usr/share"
    /** Termux app $PREFIX/share directory */
    public static final File TERMUX_SHARE_DIR = new File(TERMUX_SHARE_PREFIX_DIR_PATH);


    /** Termux app $PREFIX/tmp and $TMP directory path */
    public static final String TERMUX_TMP_PREFIX_DIR_PATH = TERMUX_PREFIX_DIR_PATH + "/tmp"; // Default: "/data/data/com.termux/files/usr/tmp"
    /** Termux app $PREFIX/tmp and $TMP directory */
    public static final File TERMUX_TMP_DIR = new File(TERMUX_TMP_PREFIX_DIR_PATH);


    /** Termux app $PREFIX/var directory path */
    public static final String TERMUX_VAR_PREFIX_DIR_PATH = TERMUX_PREFIX_DIR_PATH + "/var"; // Default: "/data/data/com.termux/files/usr/var"
    /** Termux app $PREFIX/var directory */
    public static final File TERMUX_VAR_DIR = new File(TERMUX_VAR_PREFIX_DIR_PATH);



    /** Termux app usr-staging directory path */
    public static final String TERMUX_STAGING_PREFIX_DIR_PATH = TERMUX_FILES_DIR_PATH + "/usr-staging"; // Default: "/data/data/com.termux/files/usr-staging"
    /** Termux app usr-staging directory */
    public static final File TERMUX_STAGING_PREFIX_DIR = new File(TERMUX_STAGING_PREFIX_DIR_PATH);



    /** Termux app $HOME directory path */
    public static final String TERMUX_HOME_DIR_PATH = TERMUX_FILES_DIR_PATH + "/home"; // Default: "/data/data/com.termux/files/home"
    /** Termux app $HOME directory */
    public static final File TERMUX_HOME_DIR = new File(TERMUX_HOME_DIR_PATH);


    /** Termux app config home directory path */
    public static final String TERMUX_CONFIG_HOME_DIR_PATH = TERMUX_HOME_DIR_PATH + "/.config/termux"; // Default: "/data/data/com.termux/files/home/.config/termux"
    /** Termux app config home directory */
    public static final File TERMUX_CONFIG_HOME_DIR = new File(TERMUX_CONFIG_HOME_DIR_PATH);


    /** Termux app data home directory path */
    public static final String TERMUX_DATA_HOME_DIR_PATH = TERMUX_HOME_DIR_PATH + "/.termux"; // Default: "/data/data/com.termux/files/home/.termux"
    /** Termux app data home directory */
    public static final File TERMUX_DATA_HOME_DIR = new File(TERMUX_DATA_HOME_DIR_PATH);


    /** Termux app storage home directory path */
    public static final String TERMUX_STORAGE_HOME_DIR_PATH = TERMUX_HOME_DIR_PATH + "/storage"; // Default: "/data/data/com.termux/files/home/storage"
    /** Termux app storage home directory */
    public static final File TERMUX_STORAGE_HOME_DIR = new File(TERMUX_STORAGE_HOME_DIR_PATH);





    /*
     * Termux app and plugin preferences and properties file paths.
     */

    /** Termux app default SharedPreferences file basename without extension */
    public static final String TERMUX_DEFAULT_PREFERENCES_FILE_BASENAME_WITHOUT_EXTENSION = TERMUX_PACKAGE_NAME + "_preferences"; // Default: "com.termux_preferences"

    /** Termux API app default SharedPreferences file basename without extension */
    public static final String TERMUX_API_DEFAULT_PREFERENCES_FILE_BASENAME_WITHOUT_EXTENSION = TERMUX_API_PACKAGE_NAME + "_preferences"; // Default: "com.termux.api_preferences"

    /** Termux Boot app default SharedPreferences file basename without extension */
    public static final String TERMUX_BOOT_DEFAULT_PREFERENCES_FILE_BASENAME_WITHOUT_EXTENSION = TERMUX_BOOT_PACKAGE_NAME + "_preferences"; // Default: "com.termux.boot_preferences"

    /** Termux Float app default SharedPreferences file basename without extension */
    public static final String TERMUX_FLOAT_DEFAULT_PREFERENCES_FILE_BASENAME_WITHOUT_EXTENSION = TERMUX_FLOAT_PACKAGE_NAME + "_preferences"; // Default: "com.termux.window_preferences"

    /** Termux Styling app default SharedPreferences file basename without extension */
    public static final String TERMUX_STYLING_DEFAULT_PREFERENCES_FILE_BASENAME_WITHOUT_EXTENSION = TERMUX_STYLING_PACKAGE_NAME + "_preferences"; // Default: "com.termux.styling_preferences"

    /** Termux Tasker app default SharedPreferences file basename without extension */
    public static final String TERMUX_TASKER_DEFAULT_PREFERENCES_FILE_BASENAME_WITHOUT_EXTENSION = TERMUX_TASKER_PACKAGE_NAME + "_preferences"; // Default: "com.termux.tasker_preferences"

    /** Termux Widget app default SharedPreferences file basename without extension */
    public static final String TERMUX_WIDGET_DEFAULT_PREFERENCES_FILE_BASENAME_WITHOUT_EXTENSION = TERMUX_WIDGET_PACKAGE_NAME + "_preferences"; // Default: "com.termux.widget_preferences"


    /** Termux app termux.properties primary file path */
    public static final String TERMUX_PROPERTIES_PRIMARY_FILE_PATH = TERMUX_DATA_HOME_DIR_PATH + "/termux.properties"; // Default: "/data/data/com.termux/files/home/.termux/termux.properties"
    /** Termux app termux.properties primary file */
    public static final File TERMUX_PROPERTIES_PRIMARY_FILE = new File(TERMUX_PROPERTIES_PRIMARY_FILE_PATH);

    /** Termux app termux.properties secondary file path */
    public static final String TERMUX_PROPERTIES_SECONDARY_FILE_PATH = TERMUX_CONFIG_HOME_DIR_PATH + "/termux.properties"; // Default: "/data/data/com.termux/files/home/.config/termux/termux.properties"
    /** Termux app termux.properties secondary file */
    public static final File TERMUX_PROPERTIES_SECONDARY_FILE = new File(TERMUX_PROPERTIES_SECONDARY_FILE_PATH);


    /** Termux app and Termux:Styling colors.properties file path */
    public static final String TERMUX_COLOR_PROPERTIES_FILE_PATH = TERMUX_DATA_HOME_DIR_PATH + "/colors.properties"; // Default: "/data/data/com.termux/files/home/.termux/colors.properties"
    /** Termux app and Termux:Styling colors.properties file */
    public static final File TERMUX_COLOR_PROPERTIES_FILE = new File(TERMUX_COLOR_PROPERTIES_FILE_PATH);

    /** Termux app and Termux:Styling font.ttf file path */
    public static final String TERMUX_FONT_FILE_PATH = TERMUX_DATA_HOME_DIR_PATH + "/font.ttf"; // Default: "/data/data/com.termux/files/home/.termux/font.ttf"
    /** Termux app and Termux:Styling font.ttf file */
    public static final File TERMUX_FONT_FILE = new File(TERMUX_FONT_FILE_PATH);





    /*
     * Termux app plugin specific paths.
     */

     /** Termux app directory path to store scripts to be run at boot by Termux:Boot */
    public static final String TERMUX_BOOT_SCRIPTS_DIR_PATH = TERMUX_DATA_HOME_DIR_PATH + "/boot"; // Default: "/data/data/com.termux/files/home/.termux/boot"
    /** Termux app directory to store scripts to be run at boot by Termux:Boot */
    public static final File TERMUX_BOOT_SCRIPTS_DIR = new File(TERMUX_BOOT_SCRIPTS_DIR_PATH);


    /** Termux app directory path to store foreground scripts that can be run by the termux launcher widget provided by Termux:Widget */
    public static final String TERMUX_SHORTCUT_SCRIPTS_DIR_PATH = TERMUX_DATA_HOME_DIR_PATH + "/shortcuts"; // Default: "/data/data/com.termux/files/home/.termux/shortcuts"
    /** Termux app directory to store foreground scripts that can be run by the termux launcher widget provided by Termux:Widget */
    public static final File TERMUX_SHORTCUT_SCRIPTS_DIR = new File(TERMUX_SHORTCUT_SCRIPTS_DIR_PATH);


     /** Termux app directory path to store background scripts that can be run by the termux launcher widget provided by Termux:Widget */
    public static final String TERMUX_SHORTCUT_TASKS_SCRIPTS_DIR_PATH = TERMUX_DATA_HOME_DIR_PATH + "/shortcuts/tasks"; // Default: "/data/data/com.termux/files/home/.termux/shortcuts/tasks"
    /** Termux app directory to store background scripts that can be run by the termux launcher widget provided by Termux:Widget */
    public static final File TERMUX_SHORTCUT_TASKS_SCRIPTS_DIR = new File(TERMUX_SHORTCUT_TASKS_SCRIPTS_DIR_PATH);


    /** Termux app directory path to store scripts to be run by 3rd party twofortyfouram locale plugin host apps like Tasker app via the Termux:Tasker plugin client */
    public static final String TERMUX_TASKER_SCRIPTS_DIR_PATH = TERMUX_DATA_HOME_DIR_PATH + "/tasker"; // Default: "/data/data/com.termux/files/home/.termux/tasker"
    /** Termux app directory to store scripts to be run by 3rd party twofortyfouram locale plugin host apps like Tasker app via the Termux:Tasker plugin client */
    public static final File TERMUX_TASKER_SCRIPTS_DIR = new File(TERMUX_TASKER_SCRIPTS_DIR_PATH);





    /*
     * Termux app and plugins miscellaneous variables.
     */

     /** Android OS permission declared by Termux app in AndroidManifest.xml which can be requested by 3rd party apps to run various commands in Termux app context */
    public static final String PERMISSION_RUN_COMMAND = TERMUX_PACKAGE_NAME + ".permission.RUN_COMMAND"; // Default: "com.termux.permission.RUN_COMMAND"

     /** Termux property defined in termux.properties file as a secondary check to PERMISSION_RUN_COMMAND to allow 3rd party apps to run various commands in Termux app context */
    public static final String PROP_ALLOW_EXTERNAL_APPS = "allow-external-apps"; // Default: "allow-external-apps"
    /** Default value for {@link #PROP_ALLOW_EXTERNAL_APPS} */
    public static final String PROP_DEFAULT_VALUE_ALLOW_EXTERNAL_APPS = "false"; // Default: "false"

    /** The broadcast action sent when Termux App opens */
    public static final String BROADCAST_TERMUX_OPENED = TERMUX_PACKAGE_NAME + ".app.OPENED";

    /** The Uri authority for Termux app file shares */
    public static final String TERMUX_FILE_SHARE_URI_AUTHORITY = TERMUX_PACKAGE_NAME + ".files"; // Default: "com.termux.files"





    /**
     * Termux app constants.
     */
    public static final class TERMUX_APP {

        /** Termux app core activity name. */
        public static final String TERMUX_ACTIVITY_NAME = TERMUX_PACKAGE_NAME + ".app.TermuxActivity"; // Default: "com.termux.app.TermuxActivity"

        /**
         * Termux app core activity.
         */
        public static final class TERMUX_ACTIVITY {

            /** Intent action to start termux failsafe session */
            public static final String ACTION_FAILSAFE_SESSION = TermuxConstants.TERMUX_PACKAGE_NAME + ".app.failsafe_session"; // Default: "com.termux.app.failsafe_session"


            /** Intent action to make termux reload its termux session styling */
            public static final String ACTION_RELOAD_STYLE = TermuxConstants.TERMUX_PACKAGE_NAME + ".app.reload_style"; // Default: "com.termux.app.reload_style"
            /** Intent {@code String} extra for what to reload for the TERMUX_ACTIVITY.ACTION_RELOAD_STYLE intent */
            public static final String EXTRA_RELOAD_STYLE = TermuxConstants.TERMUX_PACKAGE_NAME + ".app.reload_style"; // Default: "com.termux.app.reload_style"

        }





        /** Termux app core service name. */
        public static final String TERMUX_SERVICE_NAME = TERMUX_PACKAGE_NAME + ".app.TermuxService"; // Default: "com.termux.app.TermuxService"

        /**
         * Termux app core service.
         */
        public static final class TERMUX_SERVICE {

            /** Intent action to stop TERMUX_SERVICE */
            public static final String ACTION_STOP_SERVICE = TERMUX_PACKAGE_NAME + ".service_stop"; // Default: "com.termux.service_stop"


            /** Intent action to make TERMUX_SERVICE acquire a wakelock */
            public static final String ACTION_WAKE_LOCK = TERMUX_PACKAGE_NAME + ".service_wake_lock"; // Default: "com.termux.service_wake_lock"


            /** Intent action to make TERMUX_SERVICE release wakelock */
            public static final String ACTION_WAKE_UNLOCK = TERMUX_PACKAGE_NAME + ".service_wake_unlock"; // Default: "com.termux.service_wake_unlock"


            /** Intent action to execute command with TERMUX_SERVICE */
            public static final String ACTION_SERVICE_EXECUTE = TERMUX_PACKAGE_NAME + ".service_execute"; // Default: "com.termux.service_execute"
            /** Uri scheme for paths sent via intent to TERMUX_SERVICE */
            public static final String URI_SCHEME_SERVICE_EXECUTE = TERMUX_PACKAGE_NAME + ".file"; // Default: "com.termux.file"
            /** Intent {@code String[]} extra for command arguments for the TERMUX_SERVICE.ACTION_SERVICE_EXECUTE intent */
            public static final String EXTRA_ARGUMENTS = TERMUX_PACKAGE_NAME + ".execute.arguments"; // Default: "com.termux.execute.arguments"
            /** Intent {@code String} extra for command current working directory for the TERMUX_SERVICE.ACTION_SERVICE_EXECUTE intent */
            public static final String EXTRA_WORKDIR = TERMUX_PACKAGE_NAME + ".execute.cwd"; // Default: "com.termux.execute.cwd"
            /** Intent {@code boolean} extra for command background mode for the TERMUX_SERVICE.ACTION_SERVICE_EXECUTE intent */
            public static final String EXTRA_BACKGROUND = TERMUX_PACKAGE_NAME + ".execute.background"; // Default: "com.termux.execute.background"
            /** Intent {@code String} extra for session action for foreground commands for the TERMUX_SERVICE.ACTION_SERVICE_EXECUTE intent */
            public static final String EXTRA_SESSION_ACTION = TERMUX_PACKAGE_NAME + ".execute.session_action"; // Default: "com.termux.execute.session_action"
            /** Intent {@code Parcelable} extra containing pending intent for the execute command caller */
            public static final String EXTRA_PENDING_INTENT = "pendingIntent"; // Default: "pendingIntent"
            /** Intent {@code String} extra for label of the command for the TERMUX_SERVICE.ACTION_SERVICE_EXECUTE intent */
            public static final String EXTRA_COMMAND_LABEL = TERMUX_PACKAGE_NAME + ".execute.command_label"; // Default: "com.termux.execute.command_label"
            /** Intent markdown {@code String} extra for description of the command for the TERMUX_SERVICE.ACTION_SERVICE_EXECUTE intent */
            public static final String EXTRA_COMMAND_DESCRIPTION = TERMUX_PACKAGE_NAME + ".execute.command_description"; // Default: "com.termux.execute.command_description"
            /** Intent markdown {@code String} extra for help of the command for the TERMUX_SERVICE.ACTION_SERVICE_EXECUTE intent */
            public static final String EXTRA_COMMAND_HELP = TERMUX_PACKAGE_NAME + ".execute.command_help"; // Default: "com.termux.execute.command_help"
            /** Intent markdown {@code String} extra for help of the plugin API for the TERMUX_SERVICE.ACTION_SERVICE_EXECUTE intent (Internal Use Only) */
            public static final String EXTRA_PLUGIN_API_HELP = TERMUX_PACKAGE_NAME + ".execute.plugin_api_help"; // Default: "com.termux.execute.plugin_help"



            /** The value for {@link #EXTRA_SESSION_ACTION} extra that will set the new session as
             * the current session and will start {@link TermuxActivity} if its not running to bring
             * the new session to foreground.
             */
            public static final int VALUE_EXTRA_SESSION_ACTION_SWITCH_TO_NEW_SESSION_AND_OPEN_ACTIVITY = 0;

            /** The value for {@link #EXTRA_SESSION_ACTION} extra that will keep any existing session
             * as the current session and will start {@link TermuxActivity} if its not running to
             * bring the existing session to foreground. The new session will be added to the left
             * sidebar in the sessions list.
             */
            public static final int VALUE_EXTRA_SESSION_ACTION_KEEP_CURRENT_SESSION_AND_OPEN_ACTIVITY = 1;

            /** The value for {@link #EXTRA_SESSION_ACTION} extra that will set the new session as
             * the current session but will not start {@link TermuxActivity} if its not running
             * and session(s) will be seen in Termux notification and can be clicked to bring new
             * session to foreground. If the {@link TermuxActivity} is already running, then this
             * will behave like {@link #VALUE_EXTRA_SESSION_ACTION_KEEP_CURRENT_SESSION_AND_OPEN_ACTIVITY}.
             */
            public static final int VALUE_EXTRA_SESSION_ACTION_SWITCH_TO_NEW_SESSION_AND_DONT_OPEN_ACTIVITY = 2;

            /** The value for {@link #EXTRA_SESSION_ACTION} extra that will keep any existing session
             * as the current session but will not start {@link TermuxActivity} if its not running
             * and session(s) will be seen in Termux notification and can be clicked to bring
             * existing session to foreground. If the {@link TermuxActivity} is already running,
             * then this will behave like {@link #VALUE_EXTRA_SESSION_ACTION_KEEP_CURRENT_SESSION_AND_OPEN_ACTIVITY}.
             */
            public static final int VALUE_EXTRA_SESSION_ACTION_KEEP_CURRENT_SESSION_AND_DONT_OPEN_ACTIVITY = 3;



            /** Intent {@code Bundle} extra to store result of execute command that is sent back for the
             * TERMUX_SERVICE.ACTION_SERVICE_EXECUTE intent if the {@link #EXTRA_PENDING_INTENT} is not
             * {@code null} */
            public static final String EXTRA_PLUGIN_RESULT_BUNDLE = "result"; // Default: "result"
            /** Intent {@code String} extra for stdout value of execute command of the {@link #EXTRA_PLUGIN_RESULT_BUNDLE} */
            public static final String EXTRA_PLUGIN_RESULT_BUNDLE_STDOUT = "stdout"; // Default: "stdout"
            /** Intent {@code String} extra for stderr value of execute command of the {@link #EXTRA_PLUGIN_RESULT_BUNDLE} */
            public static final String EXTRA_PLUGIN_RESULT_BUNDLE_STDERR = "stderr"; // Default: "stderr"
            /** Intent {@code int} extra for exit code value of execute command of the {@link #EXTRA_PLUGIN_RESULT_BUNDLE} */
            public static final String EXTRA_PLUGIN_RESULT_BUNDLE_EXIT_CODE = "exitCode"; // Default: "exitCode"
            /** Intent {@code int} extra for err value of execute command of the {@link #EXTRA_PLUGIN_RESULT_BUNDLE} */
            public static final String EXTRA_PLUGIN_RESULT_BUNDLE_ERR = "err"; // Default: "err"
            /** Intent {@code String} extra for errmsg value of execute command of the {@link #EXTRA_PLUGIN_RESULT_BUNDLE} */
            public static final String EXTRA_PLUGIN_RESULT_BUNDLE_ERRMSG = "errmsg"; // Default: "errmsg"

        }





        /** Termux app run command service name. */
        public static final String RUN_COMMAND_SERVICE_NAME = TERMUX_PACKAGE_NAME + ".app.RunCommandService"; // Termux app service to receive commands from 3rd party apps "com.termux.app.RunCommandService"

        /**
         * Termux app run command service to receive commands sent by 3rd party apps.
         */
        public static final class RUN_COMMAND_SERVICE {

            /** Intent action to execute command with RUN_COMMAND_SERVICE */
            public static final String ACTION_RUN_COMMAND = TERMUX_PACKAGE_NAME + ".RUN_COMMAND"; // Default: "com.termux.RUN_COMMAND"
            /** Intent {@code String} extra for absolute path of command for the RUN_COMMAND_SERVICE.ACTION_RUN_COMMAND intent */
            public static final String EXTRA_COMMAND_PATH = TERMUX_PACKAGE_NAME + ".RUN_COMMAND_PATH"; // Default: "com.termux.RUN_COMMAND_PATH"
            /** Intent {@code String[]} extra for any arguments to pass to command for the RUN_COMMAND_SERVICE.ACTION_RUN_COMMAND intent */
            public static final String EXTRA_ARGUMENTS = TERMUX_PACKAGE_NAME + ".RUN_COMMAND_ARGUMENTS"; // Default: "com.termux.RUN_COMMAND_ARGUMENTS"
            /** Intent {@code String} extra for current working directory of command for the RUN_COMMAND_SERVICE.ACTION_RUN_COMMAND intent */
            public static final String EXTRA_WORKDIR = TERMUX_PACKAGE_NAME + ".RUN_COMMAND_WORKDIR"; // Default: "com.termux.RUN_COMMAND_WORKDIR"
            /** Intent {@code boolean} extra for whether to run command in background or foreground terminal session for the RUN_COMMAND_SERVICE.ACTION_RUN_COMMAND intent */
            public static final String EXTRA_BACKGROUND = TERMUX_PACKAGE_NAME + ".RUN_COMMAND_BACKGROUND"; // Default: "com.termux.RUN_COMMAND_BACKGROUND"
            /** Intent {@code String} extra for session action of foreground commands for the RUN_COMMAND_SERVICE.ACTION_RUN_COMMAND intent */
            public static final String EXTRA_SESSION_ACTION = TERMUX_PACKAGE_NAME + ".RUN_COMMAND_SESSION_ACTION"; // Default: "com.termux.RUN_COMMAND_SESSION_ACTION"
            /** Intent {@code String} extra for label of the command for the RUN_COMMAND_SERVICE.ACTION_RUN_COMMAND intent */
            public static final String EXTRA_COMMAND_LABEL = TERMUX_PACKAGE_NAME + ".RUN_COMMAND_COMMAND_LABEL"; // Default: "com.termux.RUN_COMMAND_COMMAND_LABEL"
            /** Intent markdown {@code String} extra for description of the command for the RUN_COMMAND_SERVICE.ACTION_RUN_COMMAND intent */
            public static final String EXTRA_COMMAND_DESCRIPTION = TERMUX_PACKAGE_NAME + ".RUN_COMMAND_COMMAND_DESCRIPTION"; // Default: "com.termux.RUN_COMMAND_COMMAND_DESCRIPTION"
            /** Intent markdown {@code String} extra for help of the command for the RUN_COMMAND_SERVICE.ACTION_RUN_COMMAND intent */
            public static final String EXTRA_COMMAND_HELP = TERMUX_PACKAGE_NAME + ".RUN_COMMAND_COMMAND_HELP"; // Default: "com.termux.RUN_COMMAND_COMMAND_HELP"

        }
    }





    /**
     * Termux:Styling app constants.
     */
    public static final class TERMUX_STYLING {

        /** Termux:Styling app core activity name. */
        public static final String TERMUX_STYLING_ACTIVITY_NAME = TERMUX_STYLING_PACKAGE_NAME + ".TermuxStyleActivity"; // Default: "com.termux.styling.TermuxStyleActivity"

    }

}
